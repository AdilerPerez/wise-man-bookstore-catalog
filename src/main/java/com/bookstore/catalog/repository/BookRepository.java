package com.bookstore.catalog.repository;


import com.bookstore.catalog.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<BookEntity, String> {
    Page<BookEntity> findByGenreContainingIgnoreCase(@Param("genre") String genre, Pageable pageable);
    Page<BookEntity> findByAuthorContainingIgnoreCase(@Param("author")String author, Pageable pageable);
    boolean existsByIsbn10OrIsbn10(String isbn, String s);
}
