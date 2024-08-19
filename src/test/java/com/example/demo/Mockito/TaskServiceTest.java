package com.example.demo.Mockito;

import com.example.demo.DAO.TaskRepository;
import com.example.demo.Models.Milestone;
import com.example.demo.Models.Project;
import com.example.demo.Models.Task;
import com.example.demo.Services.MessageService;
import com.example.demo.Services.MilestoneService;
import com.example.demo.Services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MessageService messageService;

    @Mock
    private MilestoneService milestoneService;

    @InjectMocks
    private TaskService taskService;


    @Test
    void getTasksByProjectId() {
        // Given
        Long projectId = 1L;
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.findByProjectId(projectId)).thenReturn(tasks);

        // When
        List<Task> result = taskService.getTasksByProjectId(projectId);

        // Then
        assertEquals(tasks, result);
        verify(taskRepository).findByProjectId(projectId);
    }


    @Test
    public void testUpdateTaskMilestoneId_ChangeMilestone() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        Long taskId = 1L;
        Long newMilestoneId = 2L;
        Milestone oldMilestone = new Milestone(1L, "Old Milestone");
        Task task = new Task(taskId, "Test Task", "Description", "Pending");
        task.setMilestone(oldMilestone);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        taskService.updateTaskMilestoneId(taskId, newMilestoneId);

        // Assert
        assertNotNull(task.getMilestone());
        assertEquals(newMilestoneId, task.getMilestone().getId());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
        verify(messageService).createMessage(
                eq("akashpsgct01@gmail.com.com"),
                eq("rupiyo00@gmail.com.com"),
                eq("Task Milestone Updated"),
                eq(String.format("Task '%s' has been updated from milestone '%d' to '%d'.",
                        task.getTaskName(), oldMilestone.getId(), newMilestoneId))
        );
    }

    @Test
    public void testUpdateTaskMilestoneId_NoChangeInMilestone() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        Long taskId = 1L;
        Long newMilestoneId = 1L; // Same as old milestone
        Milestone oldMilestone = new Milestone(1L, "Old Milestone");
        Task task = new Task(taskId, "Test Task", "Description", "Pending");
        task.setMilestone(oldMilestone);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        taskService.updateTaskMilestoneId(taskId, newMilestoneId);

        // Assert
        assertNotNull(task.getMilestone());
        assertEquals(newMilestoneId, task.getMilestone().getId());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
        verify(messageService, never()).createMessage(any(), any(), any(), any()); // Ensure no message is created
    }

    @Test
    public void testUpdateTaskMilestoneId_TaskNotFound() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        Long taskId = 1L;
        Long newMilestoneId = 2L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        taskService.updateTaskMilestoneId(taskId, newMilestoneId);

        // Assert
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class)); // Ensure save is not called
        verify(messageService, never()).createMessage(any(), any(), any(), any()); // Ensure no message is created
    }


}
