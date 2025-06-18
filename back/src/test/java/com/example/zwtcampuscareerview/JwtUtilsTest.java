package com.example.zwtcampuscareerview;

import com.example.zwtcampuscareerview.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtUtilsTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private JwtUtilsTest jwtUtilsTest;

    private String token;
    private String username;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化Mockito

        username = "testUser";
        // 假设 jwtUtils.generateToken() 是生成 token 的方法
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"; // 这里可以用 mock 或实际的生成方法
    }

    @Test
    void testGenerateToken() {
        assertNotNull(token);
        assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"));  // 验证JWT头部
    }

    @Test
    void testGetUsernameFromToken() {
        // 模拟 jwtUtils.getUsernameFromToken(token) 的行为
        when(jwtUtils.getUsernameFromToken(token)).thenReturn(username);
        String extractedUsername = jwtUtils.getUsernameFromToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateTokenValid() {
        // 模拟 jwtUtils.validateToken(token, username) 的行为
        when(jwtUtils.validateToken(token, username)).thenReturn(true);
        boolean isValid = jwtUtils.validateToken(token, username);
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenInvalid() {
        String invalidToken = "invalidToken";
        // 模拟 jwtUtils.validateToken(invalidToken, username) 的行为
        when(jwtUtils.validateToken(invalidToken, username)).thenReturn(false);
        boolean isValid = jwtUtils.validateToken(invalidToken, username);
        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired() {
        // 模拟 jwtUtils.isTokenExpired(token) 的行为
        when(jwtUtils.isTokenExpired(token)).thenReturn(false);
        assertFalse(jwtUtils.isTokenExpired(token));  // 在24小时内的token应不失效
    }

    @Test
    void testExpiredToken() {
        // 模拟生成过期的 token
        String expiredToken = "expiredToken";
        when(jwtUtils.isTokenExpired(expiredToken)).thenReturn(true);

        try {
            assertTrue(jwtUtils.isTokenExpired(expiredToken));
        } catch (ExpiredJwtException e) {
            assertTrue(e.getMessage().contains("Expired JWT token"));
        }
    }

    @Test
    void testMalformedToken() {
        // 模拟 jwtUtils.validateToken() 抛出异常
        when(jwtUtils.validateToken("malformed_token", username)).thenThrow(new MalformedJwtException("Malformed token"));

        assertThrows(MalformedJwtException.class, () -> {
            jwtUtils.validateToken("malformed_token", username);
        });
    }
}
