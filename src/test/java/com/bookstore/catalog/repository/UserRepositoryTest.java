package com.bookstore.catalog.repository;

import com.bookstore.catalog.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    Optional<UserEntity> expectedResponseId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expectedResponseId = Optional.of(new UserEntity());
    }

    @Test
    @DisplayName("Find user by username")
    void findBookByIdTest() {
        when(userRepository.findByUsername("someUsername")).thenReturn(expectedResponseId);

        Optional<UserEntity> result = userRepository.findByUsername("someUsername");

        assertNotNull(result);
        assertEquals(expectedResponseId, result);
    }

}