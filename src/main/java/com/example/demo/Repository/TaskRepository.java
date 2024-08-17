package com.example.demo.Repository;

import com.example.demo.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE t.assignedTo.username = :username")
    List<Task> findTasksByUsername(@Param("username") String username);

//    Task findByTaskId(int taskId);
}