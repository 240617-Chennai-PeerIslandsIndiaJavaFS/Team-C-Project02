package com.example.demo.UserMockitoTest;

import com.example.demo.Models.Milestone;
import com.example.demo.Models.Project;
import com.example.demo.Models.Task;
import com.example.demo.Models.User;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Service.MilestoneService;
import com.example.demo.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MilestoneService milestoneService;

    private Task task;
    private Milestone milestone;
    private Project project;
    private User assignedTo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = new Project();
        project.setProjectId(1);

        assignedTo = new User();

        milestone = new Milestone();
        milestone.setMilestoneId(1);

        task = new Task();
        task.setTaskId(1);
        task.setTaskName("Sample Task");
        task.setTaskDetails("Details of the task");
        task.setStartDate(new Date());
        task.setDueDate(new Date());
        task.setProject(project);
        task.setAssignedTo(assignedTo);
        task.setMilestone(milestone);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);
        assertNotNull(result);
        assertEquals(task, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));

        List<Task> tasks = taskService.getAllTasks();
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTasksByUsername() {
        String username = "testuser";
        when(taskRepository.findTasksByUsername(username)).thenReturn(Collections.singletonList(task));

        List<Task> tasks = taskService.getTasksByUsername(username);
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
        verify(taskRepository, times(1)).findTasksByUsername(username);
    }

    @Test
    void testUpdateTaskMilestone() {
        int taskId = 1;
        int milestoneId = 1;

        when(taskRepository.findByTaskId(taskId)).thenReturn(task);
        when(milestoneService.getMilestoneById(milestoneId)).thenReturn(milestone);
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = taskService.updateTaskMilestone(taskId, milestoneId);
        assertNotNull(updatedTask);
        assertEquals(milestone, updatedTask.getMilestone());
        verify(taskRepository, times(1)).findByTaskId(taskId);
        verify(milestoneService, times(1)).getMilestoneById(milestoneId);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTaskMilestone_TaskNotFound() {
        int taskId = 1;
        int milestoneId = 1;

        when(taskRepository.findByTaskId(taskId)).thenReturn(null);

        Task updatedTask = taskService.updateTaskMilestone(taskId, milestoneId);
        assertNull(updatedTask);
        verify(taskRepository, times(1)).findByTaskId(taskId);
        verify(milestoneService, never()).getMilestoneById(anyInt());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testUpdateTaskMilestone_MilestoneNotFound() {
        int taskId = 1;
        int milestoneId = 1;

        when(taskRepository.findByTaskId(taskId)).thenReturn(task);
        when(milestoneService.getMilestoneById(milestoneId)).thenReturn(null);

        Task updatedTask = taskService.updateTaskMilestone(taskId, milestoneId);
        assertNull(updatedTask);
        verify(taskRepository, times(1)).findByTaskId(taskId);
        verify(milestoneService, times(1)).getMilestoneById(milestoneId);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
