package com.example.demo.Services;

import com.example.demo.DAO.TaskRepository;
import com.example.demo.Models.Milestone;
import com.example.demo.Models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;


    @Autowired
    private MessageService messageService;

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
    public void updateTaskMilestoneId(Long taskId, Long milestoneId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            Long oldMilestoneId = task.getMilestone() != null ? task.getMilestone().getId() : null;
            task.setMilestone(new Milestone(milestoneId, null)); // Update task milestone
            taskRepository.save(task);

            if (oldMilestoneId != null && !oldMilestoneId.equals(milestoneId)) {
                // Create a message
                String messageContext = String.format("Task '%s' has been updated from milestone '%d' to '%d'.",
                        task.getTaskName(), oldMilestoneId, milestoneId);
                messageService.createMessage(
                        "akashpsgct01@gmail.com.com", // Example sender email
                        "rupiyo00@gmail.com.com", // Example recipient email
                        "Task Milestone Updated",
                        messageContext
                );
            }
        }
    }


    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }
    public void updateTask(Task task) {
        taskRepository.save(task);
    }
}

