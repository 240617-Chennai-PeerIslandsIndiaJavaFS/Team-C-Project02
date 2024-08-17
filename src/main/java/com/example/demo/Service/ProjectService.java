package com.example.demo.Service;

import com.example.demo.Models.Project;
import com.example.demo.Models.User;
import com.example.demo.Repository.ProjectRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByUsername(String username) {
        return projectRepository.findProjectsByUsername(username); // Fetch projects by username
    }

    public String addTeamMemberToProject(int projectId, int userId) throws Exception {
        // Retrieve the project and user
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // Add the user to the project
        project.getTeamMembers().add(user);

        // Save the updated project
        projectRepository.save(project);

        return "Team member added successfully!";
    }
}
