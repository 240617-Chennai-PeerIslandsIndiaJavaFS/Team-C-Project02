package com.example.demo.UserMockitoTest;

import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.UserService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Any setup needed before each test can go here
    }

    @Test
    public void testCreateUser() throws MessagingException {
        User user = new User();
        user.setUsername("abcd");
        user.setEmail("abcd@gmail.com");
        user.setPassword("password123");

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(user);

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    public void testUpdateUser_Success() throws MessagingException {
        int userId = 1;
        String newName = "abc";
        String newEmail = "abc@gmail.com";

        User existingUser = new User();
        existingUser.setUsername("cba");
        existingUser.setEmail("cba@gmail.com");

        User updatedUser = new User();
        updatedUser.setUsername(newName);
        updatedUser.setEmail(newEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, newName, newEmail);

        assertEquals(newName, result.getUsername());
        assertEquals(newEmail, result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(eq(newEmail), anyString(), anyString());
    }

    @Test
    public void testUpdateUser_Failure() throws MessagingException {
        int userId = 1;
        String newName = "abc";
        String newEmail = "abc@gmail.com";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userId, newName, newEmail);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("User1");

        User user2 = new User();
        user2.setUsername("User2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        assertEquals(2, userService.getAllUsers().size());
        verify(userRepository, times(1)).findAll();
    }
}
