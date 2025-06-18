package com.example.zwtcampuscareerview.services.implementations;

import com.example.zwtcampuscareerview.models.User;
import com.example.zwtcampuscareerview.repositories.interfaces.IUserRepository;
import com.example.zwtcampuscareerview.services.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(IUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(String username, String email, String password) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("用户名已存在！");
        }

        // 检查邮箱是否已注册
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("邮箱已注册！");
        }

        // 加密密码并保存用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password)); // 加密密码

        userRepository.save(newUser); // 保存到数据库
    }

    @Override
    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户名不存在！"));

        // 校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("密码错误！");
        }
        return true; // 返回登录成功
    }




    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        // 根据用户名查询用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在！"));

        // 校验旧密码是否正确
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("旧密码不正确！");
        }

        // 更新为新密码（加密）
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
