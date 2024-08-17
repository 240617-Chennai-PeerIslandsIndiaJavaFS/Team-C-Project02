package com.example.demo.Service;

import com.example.demo.Models.Task;
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
}
