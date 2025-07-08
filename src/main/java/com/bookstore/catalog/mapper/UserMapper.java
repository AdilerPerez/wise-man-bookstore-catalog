package com.bookstore.catalog.mapper;


import com.bookstore.catalog.dto.UserResponseDTO;
import com.bookstore.catalog.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponse(UserEntity user);
}
