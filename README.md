# O Catálogo do Sábio

> **O Projeto “O Catálogo do Sábio”** é uma API REST responsável por buscar informações de livros, filtrá-los por gênero e autor, e também disponibiliza autenticação e registro de livros vistos recentemente.

---

## 📋 Sumário

- [Descrição Geral](#descrição-geral)  
- [Arquitetura de Solução e Arquitetura Técnica](#i-arquitetura-de-solução-e-arquitetura-técnica)  
- [Explicação sobre o Case Desenvolvido (Plano de Implementação)](#ii-explicação-sobre-o-case-desenvolvido-plano-de-implementação)  
- [Melhorias e Considerações Finais](#iii-melhorias-e-considerações-finais)  
- [Como Executar](#como-executar)  

---

## Descrição Geral

O “Catálogo do Sábio” é uma API REST desenvolvida para facilitar a busca e consulta de livros de uma livraria independente.  
Principais funcionalidades:
- Busca de livros por título, gênero e autor.  
- Busca de livros vistos recentemente por usuário.  
- Sistema de autenticação (JWT e Spring Security).  

---

## Arquitetura de Solução e Arquitetura Técnica

Nesta seção será apresentado o desenho da solução implementada, as tecnologias escolhidas e as principais decisões de design:

### 1. Visão Geral da Solução  
- Desenho de alto nível: Microserviço/API REST para catálogo de livros com sistema de autenticação JWT
- Módulos principais:
  - Catálogos de livros: CRUD de busca de livros baseado em dados capturados de um dataset Kaggle e sistema de registro de livros baseado em seu ISBN na OpenLibrary API
  - Vistos recentemente: Cache Redis para rastreamento de livros acessados pelo id
  - Autenticação: Sistema de autenticação completo com Registro e Login com JWT Tokens
- Fluxo de requisição: Cliente → Spring Security Filter → JWT Auth Filter → Controller → Service → Repository → MongoDB/Redis 

### 2. Tecnologias Usadas  
- Linguagem: Java 17
- Framework: Spring Boot com Spring Security, Spring Data MongoDB, Spring Data Redis
- Banco de dados: MongoDB (dados principais) + Redis (cache de visualizações recentes)  
- Autenticação: JWT (JSON Web Token) com refresh tokens 
- Documentação de API: Swagger/OpenAPI 3 (SpringDoc)
- Containerização: Docker + Docker Compose
- Build: Maven

### 3. Decisões de Design  
- **Escolha do Banco de Dados**: 
    - MongoDB foi escolhido levando em conta a flexibilidade dos dados referente a livros, pela performance e facilidade de desenvolvimento da solução por conta da sua integração com o Spring Framework e simplicidade de paginação.
    - Redis utilizado como cache para melhorar a latencia geral nos endpoints:
      - /books
      - /books/{id}
      - /books/author/{author}
      - /books/genre/{genre}
      e para armazenar dados de livros vistos recentemente no endpoint:
       - /books/user/recently-viewed
- **Escolha do Kaggle como solução de aquisição de dados**: O data set [books-dataset](https://www.kaggle.com/datasets/abdallahwagih/books-dataset) foi escolhido com base na qualidade de dados que o mesmo possui. Por ter o ISBN(International Standard Book Number) é requisito para o enriquecimento do banco de dados futuro, tendo em base que a busca do OpenLibraryAPI utiliza o mesmo como parameter.
- **Gerenciamento de dependências**: Maven  
- **Estrutura de Pastas**: 
    - controller → service → repository
    - entity -> para modelos de dados, dto -> para transferência de dados
    - security -> para autenticação/autorização, config -> para configurações
    - mapper -> para conversão entre entidades e DTOs
- **Tratamento de Erros**: GlobalExceptionHandler do spring para tratativa de erros gerais da aplicação
- **Tecnologias utilizadas para testes**: 
  - JUnit 5
  - Mockito
  - SpringBootTest

---

## Explicação sobre o Case Desenvolvido (Plano de Implementação)

Para a implementação do projeto, diversas decisões técnicas foram tomadas.

    1. Carga de Dados:
    A carga inicial de dados foi realizada a partir do dataset [books-dataset](https://www.kaggle.com/datasets/abdallahwagih/books-dataset). A escolha se deu por ele conter as informações essenciais para a solução (gênero, título e autor) e, crucialmente, o ISBN (International Standard Book Number). A inclusão do ISBN foi um fator que facilitou a futura integração com a OpenLibrary API, planejada para enriquecer o banco de dados com informações de livros faltantes.

    2. Banco de Dados:
    Como banco de dados, o MongoDB foi selecionado devido à sua alta performance e à flexibilidade que oferece para tratar a estrutura de dados dos livros.

    3. Cache e Performance:
    Para o gerenciamento de cache, optou-se pelo Redis com o objetivo principal de diminuir a latência e aumentar a disponibilidade dos dados. Sua estrutura de chave-valor também se mostrou ideal para facilitar a construção do endpoint de "vistos recentemente".

    4. Autenticação e Segurança:
    A implementação de funcionalidades por usuário, como a de "vistos recentemente", tornou a autenticação um requisito funcional. Para atender a essa necessidade, foi escolhida a combinação do Spring Security com tokens JWT (JSON Web Token).



### 1. Endpoints Principais  
| Método | Rota                   | Descrição                                    |
| ------ | ---------------------- | -------------------------------------------- |
| GET   | `/api/v1/books`          | Lista de livros paginada|
| GET    | `/api/v1/books/{id}`               | Detalhes de um livro|
| GET    | `/api/v1/books/author/{author}`           | Lista de livros paginada filtrada por autor|
| GET    | `/api/v1/books/genre/{genre}`        | Lista de livros paginada filtrada por genero|
| GET    | `/api/v1/books/user/recently-viewed`        | Livros vistos recentemente|
| POST    | `/api/v1/auth/signin`        | Login|
| POST    | `/api/v1/auth/signup`        | Registro|

###  2. Parâmetros de Query Suportados
| Rota | Parâmetros                   | Descrição                                    |
| ------ | ---------------------- | -------------------------------------------- |
| `/api/v1/books`   | page=0, size=10          | Paginação da lista de livros|
| `/api/v1/books/genre/{genre}`     | page=0, size=10               | Paginação por gênero|
| `/api/v1/books/author/{author}`    | page=0, size=10           | Paginação por autor|


### 2. Lógica de Negócio  
- Autenticação: verificação de credenciais, emissão e validação de token.  
- Busca de livros: consultas de livros.  
- Histórico: histórico de livros visualizados pelo usuário.

### 3. Estrutura de Dados  

A aplicação possui swagger embutido para visualização dos curls e estruturas de dados, para acessa-lo basta bater no seguinte endpoint:
[Swagger Doc](http://localhost:8080/wise-man-bookstore-catalog/swagger-ui/index.html) após a execução da aplicação.

```json
// Exemplo de estrutura de dados de um livro
{
  "id": "507f1f77bcf86cd799439011",
  "isbn13": "9780142000083",
  "isbn10": "0142000086",
  "title": "The Catcher in the Rye",
  "subtitle": "A Classic Coming-of-Age Novel",
  "author": "J.D. Salinger",
  "genre": "Fiction",
  "description": "The story of Holden Caulfield, a teenager who has been expelled from prep school and is wandering around New York City.",
  "publishedYear": "1951",
  "averageRating": "4.2"
}
```

```json
// Exemplo de estrutura de dados de um usuario
{
  "id": "507f1f77bcf86cd799439012",
  "username": "user1",
  "email": "user1@example.com",
  "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFTjKiLpxKhiTh3ZSYd6Z3.",
  "roles": ["USER", "ADMIN"]
}
```


---

## Melhorias e Considerações Finais

### 1. Possíveis Melhorias  
- Recomendações: Sistema de recomendação baseado em histórico de visualizações
- Rate Limiting: Proteção contra abuse de API com Spring Cloud Gateway
- Logging Estruturado: implementação de sistemas de obsvervabilidade como o (ELK) Elasticsearch, Kibana & Logstash


### 2. Desafios Encontrados  
- Session Management: Gerenciamento de sessões distribuídas sem estado compartilhado
- Integridade dos datasets do Kaggle.

---

## Como Executar o projeto


**COM DOCKER**
--
Pré-requisitos:
 - Java 17+ instalado
 - Maven 3.6+ instalado
 - Docker e Docker Compose instalados
 - Git para clonar o repositório

 1. Clonar o repository
   ```bash
   git clone https://github.com/AdilerPerez/wise-man-bookstore-catalog.git
   
   ```
2. Em um cmd vá a pasta root do projeto e inicie o container da aplicação
  ```bash
    cd wise-man-bookstore-catalog
    docker-compose up --build -d
   ```
3. Acesse a documentação em `http://localhost:8080/wise-man-bookstore-catalog/swagger-ui/index.html`

**SEM DOCKER**
--
Pré-requisitos:
 - Java 17+ instalado
 - Maven 3.6+ instalado
 - Driver do MongoDB
 - Redis Local instalado
 - Git para clonar o repositório

 1. Clonar o repository
   ```bash
   git clone https://github.com/AdilerPerez/wise-man-bookstore-catalog.git
   
   ```
2. Em um cmd vá a pasta root do projeto e execute os comandos do maven
  ```bash
    cd wise-man-bookstore-catalog
    mvn spring-boot:build
    mvn spring-boot:run
   ```
3. Acesse a documentação em `http://localhost:8080/wise-man-bookstore-catalog/swagger-ui/index.html`

