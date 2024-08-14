package com.example.demo.Controller;

import com.example.demo.Models.User;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
            @RequestParam(required = false) String newEmail) {

        try {
            User updatedUser = userService.updateUser(userId, newName, newEmail);
            return new ResponseEntity<>("User updated successfully: " + updatedUser.getUsername(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
