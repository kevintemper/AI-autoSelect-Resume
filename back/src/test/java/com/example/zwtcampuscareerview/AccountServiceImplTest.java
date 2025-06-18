package com.example.zwtcampuscareerview;

import com.example.zwtcampuscareerview.models.User;
import com.example.zwtcampuscareerview.repositories.interfaces.IUserRepository;
import com.example.zwtcampuscareerview.services.implementations.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

class AccountServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void testRegisterUserSuccess() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        accountService.register("testUser", "test@example.com", "password");

        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testRegisterUserUsernameExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register("testUser", "test@example.com", "password");
        });

        assertEquals("用户名已存在！", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        boolean loginResult = accountService.login("testUser", "password");

        assertTrue(loginResult);
    }

    @Test
    void testLoginUsernameNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.login("testUser", "password");
        });

        assertEquals("用户名不存在！", exception.getMessage());
    }

    @Test
    void testLoginIncorrectPassword() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.login("testUser", "password");
        });

        assertEquals("密码错误！", exception.getMessage());
    }

    @Test
    void testChangePasswordSuccess() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        accountService.changePassword("testUser", "oldPassword", "newPassword");

        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void testChangePasswordOldPasswordIncorrect() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePassword("testUser", "oldPassword", "newPassword");
        });

        assertEquals("旧密码不正确！", exception.getMessage());
    }
}
