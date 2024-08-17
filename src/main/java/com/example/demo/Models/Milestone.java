package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "milestone")
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milestone_id") // Custom column name
    private int milestoneId;

    @Column(name = "milestone_name", nullable = false) // Custom column name
    private String milestoneName;

    @Column(name = "milestone_description", columnDefinition = "TEXT") // Custom column name
    private String milestoneDescription;

    @JsonIgnore // Prevents JSON looping
    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;

    // Default constructor
    public Milestone() {}

    // Parameterized constructor
    public Milestone(int milestoneId, String milestoneName, String milestoneDescription) {
        this.milestoneId = milestoneId;
        this.milestoneName = milestoneName;
        this.milestoneDescription = milestoneDescription;
    }

    // Getters and Setters
    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getMilestoneDescription() {
        return milestoneDescription;
    }

    public void setMilestoneDescription(String milestoneDescription) {
        this.milestoneDescription = milestoneDescription;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
