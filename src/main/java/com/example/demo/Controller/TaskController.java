package com.example.demo.Controller;

import com.example.demo.Models.Task;
import com.example.demo.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/by-username/{username}")
    public List<Task> getTasksByUsername(@PathVariable String username) {
        return taskService.getTasksByUsername(username);
    }

    @PutMapping("/update-milestone")
    public Task updateTaskMilestone(
            @RequestParam int taskId,
            @RequestParam int milestoneId) {
        return taskService.updateTaskMilestone(taskId, milestoneId);
    }
}