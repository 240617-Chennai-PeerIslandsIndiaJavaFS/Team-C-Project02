package com.example.demo.Controller;


import com.example.demo.Models.Project;
import com.example.demo.Service.ProjectService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-username")
    public List<Project> getProjectsByUsername(@RequestParam("username") String username) {
        return projectService.getProjectsByUsername(username); // Call service method to fetch projects
    }

    @PutMapping("/{projectId}/add-team-member/{userId}")
    public ResponseEntity<String> addTeamMemberToProject(
            @PathVariable int projectId,
            @PathVariable int userId) {
        try {
            String result = projectService.addTeamMemberToProject(projectId, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}