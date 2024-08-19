package com.example.demo.Mockito;

import com.example.demo.DAO.ProjectRepository;
import com.example.demo.Models.Project;
import com.example.demo.Services.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAllProjects() {
        // Given
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findAll()).thenReturn(projects);

        // When
        List<Project> result = projectService.getAllProjects();

        // Then
        assertEquals(projects, result);
        verify(projectRepository).findAll();
    }

    @Test
    void getProjectById() {
        // Given
        Long projectId = 1L;
        Project project = new Project();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // When
        Project result = projectService.getProjectById(projectId);

        // Then
        assertEquals(project, result);
        verify(projectRepository).findById(projectId);
    }
}
