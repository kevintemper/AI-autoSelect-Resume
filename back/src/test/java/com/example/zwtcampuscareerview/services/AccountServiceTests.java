package com.example.zwtcampuscareerview.services;

import com.example.zwtcampuscareerview.models.User;
import com.example.zwtcampuscareerview.repositories.interfaces.IUserRepository;
import com.example.zwtcampuscareerview.services.implementations.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceTests {

    @InjectMocks
    private AccountServiceImpl accountService; // 注入带有 Mock 依赖的服务

    @Mock
    private IUserRepository userRepository; // 模拟 IUserRepository

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // 初始化 @Mock 和 @InjectMocks 注解
    }

    @Test
    void testRegisterUserSuccess() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act
        accountService.register("testuser", "test@example.com", "password123");

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserDuplicateUsername() {
        // Arrange
        User existingUser = new User(1, "testuser", "existing@example.com", "password123");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.register("testuser", "test@example.com", "password123");
        });
    }

    @Test
    void testLoginInvalidPassword() {
        // Arrange
        User user = new User(1, "testuser", "test@example.com", "correct_password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.login("testuser", "wrong_password");
        });
    }
}


