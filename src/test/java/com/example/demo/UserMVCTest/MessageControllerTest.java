package com.example.demo.UserMVCTest;

import com.example.demo.Controller.MessageController;
import com.example.demo.Models.Message;
import com.example.demo.Models.User;
import com.example.demo.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private Message message;

    @BeforeEach
    void setUp() {
        User receiver = new User();
        receiver.setUserid(1);
        receiver.setUsername("John Doe");

        message = new Message();
        message.setMessageId(1);
        message.setReceiver(receiver);
        message.setContext("Test message content");
    }

    @Test
    void testCreateMessage() throws Exception {
        when(messageService.createMessage(any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/api/messages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId").value(1L))
                .andExpect(jsonPath("$.receiver.username").value("John Doe"))
                .andExpect(jsonPath("$.context").value("Test message content"));  // Updated this line
    }


    @Test
    void testGetAllMessages() throws Exception {
        List<Message> messages = Arrays.asList(message);
        when(messageService.getAllMessages()).thenReturn(messages);

        mockMvc.perform(get("/api/messages/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].messageId").value(1L))
                .andExpect(jsonPath("$[0].receiver.username").value("John Doe"));  // Updated this line
    }

    @Test
    void testGetMessageById_Success() throws Exception {
        when(messageService.getMessageById(1L)).thenReturn(message);

        mockMvc.perform(get("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId").value(1L))
                .andExpect(jsonPath("$.receiver.username").value("John Doe"))
                .andExpect(jsonPath("$.context").value("Test message content")); // Corrected to match the actual field name
    }



    @Test
    void testGetMessageById_NotFound() throws Exception {
        when(messageService.getMessageById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMessagesByReceiverName() throws Exception {
        List<Message> messages = Arrays.asList(message);
        when(messageService.getAllMessagesByReceiverName("John Doe")).thenReturn(messages);

        mockMvc.perform(get("/api/messages/getMessagesByReceiverName")
                        .param("receiverName", "John Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())  // Logs the response body to the console
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].receiver.username").value("John Doe"));  // Update based on actual structure
    }


    @Test
    void testDeleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/messages/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("message deleted successfully"));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        doThrow(new RuntimeException("message not found with ID: 1"))
                .when(messageService).deleteUserById(1);

        mockMvc.perform(delete("/api/messages/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("message not found with ID: 1"));
    }
}
