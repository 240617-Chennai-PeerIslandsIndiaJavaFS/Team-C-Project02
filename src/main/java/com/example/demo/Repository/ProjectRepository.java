package com.example.demo.Repository;

import com.example.demo.Models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT p FROM Project p WHERE p.projectManager.username = :username")
    List<Project> findProjectsByUsername(@Param("username") String username);

    Project findByProjectName(String projectName);

    Project findByProjectId(int projectId);

}