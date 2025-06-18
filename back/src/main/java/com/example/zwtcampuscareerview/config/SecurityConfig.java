package com.example.zwtcampuscareerview.config;

import com.example.zwtcampuscareerview.utils.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF 并允许指定接口通过
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/Account/Register",
                                "/api/Account/Login",
                                "/api/data",
                                "/api/jobtrendchart"
                        ) // 忽略这些接口
                        .disable()
                )
                // 配置跨域
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 配置接口的权限访问
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/Account/Register",
                                "/api/Account/Login",
                                "/api/passwordreset",
                                "/error",
                                "/api/account/ChangePassword",
                                "/api/data",
                                "/api/jobtrendchart",
                                "/api/data2/**"
                        ).permitAll() // 这些接口允许匿名访问
                        .requestMatchers("/v1/chat/completions").authenticated() // GPT 接口需要认证
                        .anyRequest().authenticated() // 其他接口需要认证
                )
                // 无状态会话配置
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 添加 JWT 过滤器
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的跨域来源
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5185",
                "http://127.0.0.1:8080",
                "https://aigc.x-see.cn" // GPT 接口的域名
        ));
        // 允许的跨域方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 应用跨域配置到所有接口
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
