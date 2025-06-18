package com.example.zwtcampuscareerview.controllers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.zwtcampuscareerview.DTO.ResetPasswordRequest;
import com.example.zwtcampuscareerview.models.User;
import com.example.zwtcampuscareerview.repositories.interfaces.IUserRepository;

import java.security.SecureRandom;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/passwordreset")
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    private final JavaMailSender mailSender;
    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetController(JavaMailSender mailSender, IUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        // 验证邮箱固定为 3422965463@qq.com
        if (!"3422965463@qq.com".equals(email)) {
            logger.warn("提供的邮箱不正确: {}", email);
            return ResponseEntity.badRequest().body("邮箱不正确，请提供正确的邮箱！");
        }

        // 验证用户名和邮箱是否匹配
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty() || !userOptional.get().getEmail().equals(email)) {
            logger.warn("未找到匹配的用户名和邮箱: 用户名={}, 邮箱={}", username, email);
            return ResponseEntity.badRequest().body("未找到匹配的用户名和邮箱！");
        }

        User user = userOptional.get();

        // 生成新密码
        String newPassword = generateNewPassword();
        logger.info("生成的新密码为: {}", newPassword);

        // 更新数据库中的密码
        user.setPassword(passwordEncoder.encode(newPassword)); // 加密新密码
        userRepository.save(user); // 保存到数据库
        logger.info("用户 {} 的密码已更新。", user.getUsername());

        // 发送新密码到邮箱
        try {
            sendEmail(email, newPassword);
            logger.info("新密码已成功发送至邮箱: {}", email);
            return ResponseEntity.ok("新密码已发送至邮箱：" + email);
        } catch (MessagingException e) {
            logger.error("发送邮件失败，邮箱: {}, 错误信息: {}", email, e.getMessage());
            return ResponseEntity.status(500).body("发送邮件失败，请稍后重试！");
        } catch (Exception e) {
            logger.error("发生未知错误，邮箱: {}, 错误信息: {}", email, e.getMessage());
            return ResponseEntity.status(500).body("发送邮件失败，发生未知错误！");
        }
    }

    /**
     * 生成安全随机的新密码
     */
    private String generateNewPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    /**
     * 发送重置密码邮件
     *
     * @param email       接收方邮箱
     * @param newPassword 新密码
     * @throws MessagingException 邮件发送异常
     */
    private void sendEmail(String email, String newPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("3422965463@qq.com"); // 发件人邮箱
        helper.setTo(email);
        helper.setSubject("密码重置");
        helper.setText("您的新密码为：" + newPassword, false);

        mailSender.send(message);
    }
}
