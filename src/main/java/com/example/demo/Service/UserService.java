package com.example.demo.Service;

import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;  // Inject EmailService

    public User createUser(User user) throws MessagingException {
        String password = generateRandomPassword();
        user.setPassword(password);
        User createdUser = userRepository.save(user);
        sendWelcomeEmail(user.getEmail(), password);
        return createdUser;
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void sendWelcomeEmail(String username, String password) throws MessagingException {
        String subject = "Welcome to Our Platform!";
        String body = "Hello " + username + ",\n\nYour account has been created successfully.\nYour password is: " + password + "\n\nBest regards,\nThe Team";
        emailService.sendEmail(username,password,body);
    }
}
