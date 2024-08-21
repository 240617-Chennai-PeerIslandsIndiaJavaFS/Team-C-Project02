package com.example.demo.Service;

import com.example.demo.Models.Project;
import com.example.demo.Models.User;
import com.example.demo.Repository.ProjectRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
        return projectRepository.findProjectsByUsername(username);
    }

    public String addTeamMemberToProject(int projectId, int userId) throws Exception {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        project.getTeamMembers().add(user);
        projectRepository.save(project);

        return "Team member added successfully!";
    }

    public Set<User> getTeamMembersByProjectName(String projectName) {
        Project project = projectRepository.findByProjectName(projectName);
        if (project != null) {
            return project.getTeamMembers();
        } else {
            return Set.of();
        }
    }

    public Set<User> getTeamMembersByProjectId(int projectId) {
        Project project = projectRepository.findByProjectId(projectId);
        if (project != null) {
            return project.getTeamMembers();
        }
        return null;
    }
}
