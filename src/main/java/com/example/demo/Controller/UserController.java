package com.example.demo.Controller;

import com.example.demo.Enums.Status;
import com.example.demo.Models.Client;
import com.example.demo.Models.Task;
import com.example.demo.Models.User;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.OtpService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        try {
            User user = userService.authenticateUser(email, password);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error during authentication: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        System.out.println("Received user: " + user);
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>("User created successfully: " + createdUser.getUsername(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable int userId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) Status newStatus) {

        try {
            User updatedUser = userService.updateUser(userId, newName, newEmail, newStatus);
            return new ResponseEntity<>("User updated successfully: " + updatedUser.getUsername(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/assign-role/{userId}")
    public ResponseEntity<String> assignAccessLevel(
            @PathVariable int userId,
            @RequestBody Map<String, String> body) {

        String newRole = body.get("newRole");

        try {
            User updatedUser = userService.assignAccessLevel(userId, newRole);
            return new ResponseEntity<>("Role updated successfully to: " + updatedUser.getRole(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/team-members")
    public List<User> getAllTeamMembers() {
        return userService.getAllTeamMembers(); // Call service method to fetch team members
    }

    @GetMapping("/project-managers")
    public List<User> getAllProjectManagers() {
        return userService.getProjectManagers();
    }

    @GetMapping("/by-username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userid}/password")
    public String resetPassword(@PathVariable int userid, @RequestParam String newPassword) {
        return userService.resetPassword(userid, newPassword);
    }

    @GetMapping("/team-members-by-manager")
    public Set<User> getTeamMembersByManager(@RequestParam String managerName) {
        return userService.getTeamMembersByManagerName(managerName);
    }

    @GetMapping("/clients-by-manager")
    public Set<Client> getClientsByManager(@RequestParam String managerName) {
        return userService.getClientsByManagerName(managerName);
    }

    @GetMapping("/tasks-by-manager")
    public Set<Task> getTasksByManager(@RequestParam String managerName) {
        return userService.getTasksByManagerName(managerName);
    }

}