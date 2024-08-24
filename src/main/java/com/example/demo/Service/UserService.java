package com.example.demo.Service;

import com.example.demo.Enums.Role;
import com.example.demo.Enums.Status;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Models.Client;
import com.example.demo.Models.Project;
import com.example.demo.Models.Task;
import com.example.demo.Models.User;
import com.example.demo.Repository.ProjectRepository;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                if (user.getStatus() == Status.ACTIVE) {
                    return user;
                } else {
                    throw new RuntimeException("User is not active.");
                }
            }
        }
        return null;
    }

    public User createUser(User user) throws MessagingException {
        // Generate a random password
        String password = generateRandomPassword();

        // Set the password for the user
        user.setPassword(password);

        // Save the user to the database
        User createdUser = userRepository.save(user);

        sendWelcomeEmail(user.getEmail(), user.getUsername(), password);

        return createdUser;
    }


    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@*";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void sendWelcomeEmail(String email, String username, String password) throws MessagingException {
        String subject = "Welcome to Our Platform!";
        String body = "Hello " + username + ",\n\nYour account has been created successfully.\nYour password is: " + password + "\n\nBest regards,\nThe Team";
        emailService.sendEmail(email, subject, body);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(int userId, String newName, String newEmail) throws MessagingException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            boolean nameChanged = false;
            boolean emailChanged = false;

            if (newName != null && !newName.isEmpty() && !newName.equals(user.getUsername())) {
                user.setUsername(newName);
                nameChanged = true;
            }

            if (newEmail != null && !newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
                user.setEmail(newEmail);
                emailChanged = true;
            }

            User updatedUser = userRepository.save(user);

            if (nameChanged || emailChanged) {
                String subject = "Account Information Update Notification";
                StringBuilder body = new StringBuilder("Hello " + user.getUsername() + ",\n\nYour account information has been successfully updated.");

                if (nameChanged) {
                    body.append("\n\nNew Name: ").append(user.getUsername());
                }
                if (emailChanged) {
                    body.append("\n\nNew Email: ").append(user.getEmail());
                }

                emailService.sendEmail(user.getEmail(), subject, body.toString());
            }

            return updatedUser;
        } else {
            throw new RuntimeException("User not found");
        }
    }


    public User assignAccessLevel(int userId, String newRole) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            boolean roleChanged = false;

            if (!newRole.equalsIgnoreCase(user.getRole().name())) {
                user.setRole(Role.valueOf(newRole.toUpperCase()));
                roleChanged = true;
            }

            User updatedUser = userRepository.save(user);

            if (roleChanged) {
                String subject = "Role Update Notification";
                String body = "Hello " + user.getUsername() + ",\n\nYour role has been updated.\n\n" +
                        "New Role: " + user.getRole().name();

                try {
                    emailService.sendEmail(user.getEmail(), subject, body);
                } catch (MessagingException e) {
                    throw new RuntimeException("Failed to send email: " + e.getMessage());
                }
            }

            return updatedUser;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User findByNameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }


    public void updatePassword(int userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPassword(newPassword);

        userRepository.save(user);
    }

    public List<User> getAllTeamMembers() {
        return userRepository.findByRole(Role.TEAM_MEMBER); // Fetch users with TEAM_MEMBER role
    }

    public List<User> getProjectManagers() {
        return userRepository.findByRole(Role.PROJECT_MANAGER);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public String resetPassword(int userid, String newPassword) {
        int updatedRows = userRepository.resetPasswordById(userid, newPassword);
        if (updatedRows > 0) {
            return "Password updated successfully!";
        } else {
            return "User not found!";
        }
    }

    public Set<User> getTeamMembersByManagerName(String managerName) {
        List<Project> projects = projectRepository.findProjectsByProjectManagerName(managerName);
        Set<User> teamMembers = new HashSet<>();

        for (Project project : projects) {
            teamMembers.addAll(project.getTeamMembers());
        }

        return teamMembers;
    }

    public Set<Client> getClientsByManagerName(String managerName) {
        List<Project> projects = projectRepository.findProjectsByProjectManagerName(managerName);
        Set<Client> clients = new HashSet<>();

        for (Project project : projects) {
            clients.add(project.getClient());
        }

        return clients;
    }

    public Set<Task> getTasksByManagerName(String managerName) {
        List<Project> projects = projectRepository.findProjectsByProjectManagerName(managerName);
        Set<Task> tasks = new HashSet<>();

        for (Project project : projects) {
            tasks.addAll(project.getTasks());
        }

        return tasks;
    }

    @Transactional
    public void reassignTasksAndDeleteUser(int oldUserId, int newUserId) {
        // Fetch the user to delete
        User userToDelete = userRepository.findById(oldUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the new user to whom tasks will be reassigned
        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new RuntimeException("New user not found"));

        // Find all tasks assigned to the user to be deleted
        List<Task> tasksToReassign = taskRepository.findByAssignedTo(userToDelete);

        // Reassign each task to the new user
        for (Task task : tasksToReassign) {
            task.setAssignedTo(newUser);
            taskRepository.save(task);
        }

        // Delete the old user
        userRepository.delete(userToDelete);
    }

    @Transactional
    public void deleteUserById(int userId) {
        // Check if the user exists before attempting to delete
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    public User deactivateUser(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(Status.INACTIVE); // Set status to INACTIVE
            return userRepository.save(user); // Save the updated user
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User activateUser(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(Status.ACTIVE);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<User> getInactiveUsers() {
        return userRepository.findByStatus(Status.INACTIVE);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
}
