package com.bookstore.catalog.security.impl;

import com.bookstore.catalog.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    @DisplayName("Should build UserDetails from UserEntity")
    void testBuild() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setUsername("testuser");
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("password");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        userEntity.setRoles(roles);
        UserDetailsImpl userDetails = UserDetailsImpl.build(userEntity);
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertFalse(userDetails.getAuthorities().isEmpty());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Should build UserDetails with multiple roles")
    void testBuildWithMultipleRoles() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setUsername("testuser");
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("password");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        userEntity.setRoles(roles);

        UserDetailsImpl userDetails = UserDetailsImpl.build(userEntity);

        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should implement UserDetails interface methods correctly")
    void testUserDetailsImplementation() {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));
        UserDetailsImpl userDetails = new UserDetailsImpl(
                "1", "testuser", "test@test.com", "password", authorities);

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));
        UserDetailsImpl userDetails1 = new UserDetailsImpl(
                "1", "testuser", "test@test.com", "password", authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(
                "1", "testuser", "test@test.com", "password", authorities);
        UserDetailsImpl differentUser = new UserDetailsImpl(
                "2", "otheruser", "other@test.com", "password", authorities);

        assertEquals(userDetails1, userDetails2);
        assertNotEquals(userDetails1, differentUser);
        assertEquals(userDetails1.hashCode(), userDetails2.hashCode());
        assertNotEquals(userDetails1.hashCode(), differentUser.hashCode());
        assertNotEquals(userDetails1, null);
        assertNotEquals(userDetails1, new Object());
    }

    @Test
    @DisplayName("Should create UserDetails with empty authorities")
    void testCreateWithEmptyAuthorities() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                "1", "testuser", "test@test.com", "password", Collections.emptyList());

        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("Should get email")
    void testGetEmail() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                "1", "testuser", "test@test.com", "password", Collections.emptyList());

        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    @DisplayName("Should get id")
    void testGetId() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                "1", "testuser", "test@test.com", "password", Collections.emptyList());

        assertEquals("testuser", userDetails.getUsername());
    }
}
