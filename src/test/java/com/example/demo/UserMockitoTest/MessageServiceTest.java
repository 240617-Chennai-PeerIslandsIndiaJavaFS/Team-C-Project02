package com.example.demo.UserMockitoTest;

import com.example.demo.Models.Message;
import com.example.demo.Models.User;
import com.example.demo.Repository.MessageRepository;
import com.example.demo.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User receiver = new User();
        receiver.setUserid(1); // Assuming the User class has an ID field
        receiver.setUsername("John Doe"); // Adjust according to your User class fields

        message = new Message();
        message.setMessageId(1);
        message.setReceiver(receiver); // Set the User object instead of a String
    }
    @Test
    void testCreateMessage() {
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message createdMessage = messageService.createMessage(message);

        assertNotNull(createdMessage);
        assertEquals(message.getMessageId(), createdMessage.getMessageId());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testGetAllMessages() {
        when(messageRepository.findAll()).thenReturn(Arrays.asList(message));

        List<Message> messages = messageService.getAllMessages();

        assertNotNull(messages);
        assertEquals(1, messages.size());
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    void testGetMessageById() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        Message foundMessage = messageService.getMessageById(1L);

        assertNotNull(foundMessage);
        assertEquals(message.getMessageId(), foundMessage.getMessageId());
        verify(messageRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllMessagesByReceiverName() {
        when(messageRepository.findAllMessagesByReceiverName("John Doe")).thenReturn(Arrays.asList(message));

        List<Message> messages = messageService.getAllMessagesByReceiverName("John Doe");

        assertNotNull(messages);
        assertEquals(1, messages.size());
        verify(messageRepository, times(1)).findAllMessagesByReceiverName("John Doe");
    }

    @Test
    void testDeleteUserById_Success() {
        when(messageRepository.existsById(1L)).thenReturn(true);

        messageService.deleteUserById(1);

        verify(messageRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserById_NotFound() {
        when(messageRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            messageService.deleteUserById(1);
        });

        assertEquals("message not found with ID: 1", exception.getMessage());
        verify(messageRepository, never()).deleteById(1L);
    }
}
