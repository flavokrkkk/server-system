package com.mono.service;

import com.mono.models.User;
import com.mono.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepo userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(UserRepo userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(System.currentTimeMillis() + 3600000);
            userRepository.save(user);

            String resetLink = "http://url-front/reset-password?token=" + token;
            String message = "Чтобы сбросить свой пароль, перейдите по ссылке ниже:\n" + resetLink;
            emailService.sendEmail(email, "Сброс пароля", message);
        }
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getResetTokenExpiry() > System.currentTimeMillis()) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetToken(null);
                user.setResetTokenExpiry(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Срок действия токена сброса истек");
            }
        } else {
            throw new RuntimeException("Недопустимый токен сброса");
        }
    }
}
