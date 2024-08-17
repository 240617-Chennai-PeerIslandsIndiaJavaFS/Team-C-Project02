package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id") // Custom column name
    private int taskId;

    @Column(name = "task_name", nullable = false) // Custom column name
    private String taskName;

    @Column(name = "task_details", columnDefinition = "TEXT") // Custom column name
    private String taskDetails;

    @Column(name = "start_date") // Custom column name
    private Date startDate;

    @Column(name = "due_date") // Custom column name
    private Date dueDate;

    @JsonIgnoreProperties({"tasks", "client", "projectManager"}) // Prevents JSON looping
    @ManyToOne
    @JoinColumn(name = "project_id") // Foreign key column
    private Project project;

    @JsonIgnoreProperties({"managedProjects", "tasks"}) // Prevents JSON looping
    @ManyToOne
    @JoinColumn(name = "assigned_to") // Foreign key column
    private User assignedTo;

    @JsonIgnoreProperties("tasks") // Prevents JSON looping
    @ManyToOne
    @JoinColumn(name = "milestone_id") // Foreign key column
    private Milestone milestone;

    // Default constructor
    public Task() {}

    // Parameterized constructor
    public Task(int taskId, String taskName, String taskDetails, Date startDate, Date dueDate, Project project, User assignedTo, Milestone milestone) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDetails = taskDetails;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.project = project;
        this.assignedTo = assignedTo;
        this.milestone = milestone;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }
}
