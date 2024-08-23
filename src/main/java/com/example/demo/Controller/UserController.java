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
            if (user != null && user.getStatus() == Status.ACTIVE) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else if (user != null && user.getStatus() != Status.ACTIVE) {
                return new ResponseEntity<>("Your account is inactive. Please contact support.", HttpStatus.FORBIDDEN);
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable int userId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newEmail) {

        try {
            User updatedUser = userService.updateUser(userId, newName, newEmail);
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

    @DeleteMapping("/reassign-tasks-and-delete/{oldUserId}/{newUserId}")
    public ResponseEntity<String> reassignTasksAndDeleteUser(
            @PathVariable int oldUserId,
            @PathVariable int newUserId) {
        try {
            userService.reassignTasksAndDeleteUser(oldUserId, newUserId);
            return ResponseEntity.ok("Tasks reassigned and user deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/inactive")
    public List<User> getInactiveUsers() {
        return userService.getInactiveUsers();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable int userId) {
        try {
            User deactivatedUser = userService.deactivateUser(userId);
            return new ResponseEntity<>("User deactivated successfully: " + deactivatedUser.getUsername(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @PutMapping("/activate/{userId}")
    public ResponseEntity<String> activateUser(@PathVariable int userId) {
        try {
            User activatedUser = userService.activateUser(userId);
            return new ResponseEntity<>("User activated successfully: " + activatedUser.getUsername(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}