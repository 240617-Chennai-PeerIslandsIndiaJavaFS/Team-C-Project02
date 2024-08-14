package com.example.demo.UserTestCase;

import com.example.demo.Controller.UserController;
import com.example.demo.Models.User;
import com.example.demo.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("abcd");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"abcd\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created successfully: abcd"));
    }

    @Test
    void testCreateUserFailure() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Creation error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"abcd\" }"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error creating user: Creation error"));
    }
}
