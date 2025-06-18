package com.example.zwtcampuscareerview.services.implementations;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟一个用户加载逻辑
        if ("testuser".equals(username)) {
            return User.builder()
                    .username("testuser")
                    .password("{noop}password") // {noop} 表示未加密密码，仅用于测试
                    .authorities(Collections.emptyList())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
