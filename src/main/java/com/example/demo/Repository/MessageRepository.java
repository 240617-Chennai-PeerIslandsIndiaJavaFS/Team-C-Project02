package com.example.demo.Repository;

import com.example.demo.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.receiver.username = :receiverName")
    List<Message> findAllMessagesByReceiverName(@Param("receiverName") String receiverName);

}