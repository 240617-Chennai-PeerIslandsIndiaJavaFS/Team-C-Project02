package com.example.demo.Service;

import com.example.demo.Models.Milestone;
import com.example.demo.Models.Task;
import com.example.demo.Models.User;
import com.example.demo.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MilestoneService milestoneService;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUsername(String username) {
        return taskRepository.findTasksByUsername(username);
    }

    public Task updateTaskMilestone(int taskId, int milestoneId) {
        Task task = taskRepository.findByTaskId(taskId);
        if (task != null) {
            Milestone milestone = milestoneService.getMilestoneById(milestoneId);
            if (milestone != null) {
                task.setMilestone(milestone);
                return taskRepository.save(task);
            }
        }
        return null;
    }

    public List<Task> getTasksAssignedToUser(User user) {
        return taskRepository.findByAssignedTo(user);
    }
}
