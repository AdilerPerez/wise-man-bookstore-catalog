package com.bookstore.catalog.mapper;

import com.bookstore.catalog.dto.UserResponseDTO;
import com.bookstore.catalog.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Should map UserEntity to UserResponseDTO correctly")
    void mapUserEntityToUserResponseDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1L");
        userEntity.setUsername("Test User");
        userEntity.setEmail("test@test.com");

        UserResponseDTO responseDTO = userMapper.toUserResponse(userEntity);

        assertNotNull(responseDTO);
        assertEquals("1L", responseDTO.getId());
        assertEquals("Test User", responseDTO.getUsername());
        assertEquals("test@test.com", responseDTO.getEmail());
    }

    @Test
    @DisplayName("Should return null when mapping null UserEntity")
    void mapNullUserEntityToUserResponseDTO() {
        UserResponseDTO responseDTO = userMapper.toUserResponse(null);

        assertNull(responseDTO);
    }

    @Test
    @DisplayName("Should handle UserEntity with missing fields gracefully")
    void mapUserEntityWithMissingFieldsToUserResponseDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(null);
        userEntity.setUsername(null);
        userEntity.setEmail(null);

        UserResponseDTO responseDTO = userMapper.toUserResponse(userEntity);

        assertNotNull(responseDTO);
        assertNull(responseDTO.getId());
        assertNull(responseDTO.getUsername());
        assertNull(responseDTO.getEmail());
    }
}