package com.example.demo.DAO;

import com.example.demo.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    @Modifying
    @Query("UPDATE Task t SET t.milestone.id = :milestoneId WHERE t.id = :taskId")
    void updateTaskMilestoneId(@Param("taskId") Long taskId, @Param("milestoneId") Long milestoneId);
}