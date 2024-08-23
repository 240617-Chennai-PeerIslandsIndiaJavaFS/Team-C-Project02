package com.example.demo.Service;

import com.example.demo.Models.Message;
import com.example.demo.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> getAllMessagesByReceiverName(String receiverName) {
        return messageRepository.findAllMessagesByReceiverName(receiverName);
    }

    @Transactional
    public void deleteUserById(int message_id) {
        if (messageRepository.existsById((long) message_id)) {
            messageRepository.deleteById((long) message_id);
        } else {
            throw new RuntimeException("message not found with ID: " + message_id);
        }
    }

}