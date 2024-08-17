package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id") // Custom column name
    private int clientId;

    @Column(name = "client_name", nullable = false) // Custom column name
    private String clientName;

    @Column(name = "client_email", nullable = false, unique = true) // Custom column name
    private String clientEmail;

    @Column(name = "client_description", columnDefinition = "TEXT") // Custom column name
    private String clientDescription;

    @JsonIgnoreProperties("client")
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "projects") // Custom column name for the set of projects
    private Set<Project> projects;

    // Default constructor
    public Client() {}

    // Parameterized constructor
    public Client(int clientId, String clientName, String clientEmail, String clientDescription) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientDescription = clientDescription;
    }

    // Getters and Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientDescription() {
        return clientDescription;
    }

    public void setClientDescription(String clientDescription) {
        this.clientDescription = clientDescription;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
