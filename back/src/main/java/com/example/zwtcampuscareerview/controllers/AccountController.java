package com.example.zwtcampuscareerview.controllers;

import com.example.zwtcampuscareerview.DTO.ChangePasswordRequest;
import com.example.zwtcampuscareerview.models.User;
import com.example.zwtcampuscareerview.services.interfaces.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 用户注册接口
    @PostMapping("/Register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        try {
            accountService.register(user.getUsername(), user.getEmail(), user.getPassword());
            return ResponseEntity.ok("注册成功！");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 用户登录接口
    @PostMapping("/Login")
    public ResponseEntity<String> login(@RequestBody com.example.zwtcampuscareerview.DTO.LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            // 调用服务层的 login 方法
            accountService.login(username, password);
            return ResponseEntity.ok("登录成功！");
        } catch (IllegalArgumentException e) {
            // 返回错误信息
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/ChangePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            accountService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("密码修改成功。");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("服务器发生错误。");
        }
    }


}

