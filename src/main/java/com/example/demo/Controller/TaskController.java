package com.example.demo.Controllers;

import com.example.demo.Models.Milestone;
import com.example.demo.Models.Task;
import com.example.demo.Services.MilestoneService;
import com.example.demo.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private MilestoneService milestoneService;

    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProject(@PathVariable Long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }
    @PutMapping("/{id}/milestone")
    public ResponseEntity<String> updateTaskMilestone(@PathVariable Long id, @RequestBody Long newMilestoneId) {
        System.out.println(id+newMilestoneId);
        Task task = taskService.getTaskById(id);
        System.out.println(task);
        if (task != null) {
            Milestone newMilestone = milestoneService.getMilestoneById(newMilestoneId);
            System.out.println(newMilestone);
            if (newMilestone != null) {
                task.setMilestone(newMilestone);
                taskService.updateTask(task);
                System.out.println(task);
                return new ResponseEntity<>("Task milestone updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Milestone not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }
    }
}