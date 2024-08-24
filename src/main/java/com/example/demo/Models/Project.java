package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_details", columnDefinition = "TEXT")
    private String projectDetails;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @JsonIgnoreProperties("managedProjects")
    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private User projectManager;

    @ManyToMany
    @JoinTable(
            name = "project_team_member",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> teamMembers = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;

    public Project() {}

    public Project(int projectId, String projectName, String projectDetails, Client client, User projectManager, Set<User> teamMembers) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDetails = projectDetails;
        this.client = client;
        this.projectManager = projectManager;
        this.teamMembers = teamMembers;
    }
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    public Set<User> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Set<User> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
