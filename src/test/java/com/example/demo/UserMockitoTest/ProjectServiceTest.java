package com.example.demo.UserMockitoTest;

import com.example.demo.Models.Project;
import com.example.demo.Models.User;
import com.example.demo.Repository.ProjectRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project();
        project.setProjectId(1);
        project.setProjectName("Test Project");

        user = new User();
        user.setUserid(1);
        user.setUsername("john");

        Set<User> teamMembers = new HashSet<>();
        teamMembers.add(user);
        project.setTeamMembers(teamMembers);
    }

    @Test
    public void testCreateProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project createdProject = projectService.createProject(project);

        assertNotNull(createdProject);
        assertEquals(project.getProjectName(), createdProject.getProjectName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void testGetProjectsByUsername() {
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        when(projectRepository.findProjectsByUsername("john")).thenReturn(projects);
        List<Project> result = projectService.getProjectsByUsername("john");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getProjectName());
        verify(projectRepository, times(1)).findProjectsByUsername("john");
    }


    @Test
    public void testGetTeamMembersByProjectName() {
        project.getTeamMembers().add(user);
        when(projectRepository.findByProjectName(anyString())).thenReturn(project);

        Set<User> teamMembers = projectService.getTeamMembersByProjectName("Test Project");

        assertNotNull(teamMembers);
        assertEquals(1, teamMembers.size());
        verify(projectRepository, times(1)).findByProjectName("Test Project");
    }

    @Test
    public void testAddTeamMemberToProject() throws Exception {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        String result = projectService.addTeamMemberToProject(1, 1);
        assertEquals("Team member added successfully!", result);
        assertNotNull(project.getTeamMembers());
        assertTrue(project.getTeamMembers().contains(user));
        verify(projectRepository, times(1)).save(project);
    }
    @Test
    public void testAddTeamMemberToProject_ProjectNotFound() {

        when(projectRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            projectService.addTeamMemberToProject(1, 1);
        });
        assertEquals("Project not found", exception.getMessage());
    }
    @Test
    public void testAddTeamMemberToProject_UserNotFound() throws Exception {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            projectService.addTeamMemberToProject(1, 1);
        });
        assertEquals("User not found", exception.getMessage());
    }
    @Test
    public void testGetTeamMembersByProjectId() {
        when(projectRepository.findByProjectId(1)).thenReturn(project);
        Set<User> result = projectService.getTeamMembersByProjectId(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(user));
        verify(projectRepository, times(1)).findByProjectId(1);
    }
    @Test
    public void testGetTeamMembersByProjectId_ProjectNotFound() {
        when(projectRepository.findByProjectId(1)).thenReturn(null);
        Set<User> result = projectService.getTeamMembersByProjectId(1);
        assertNull(result);
        verify(projectRepository, times(1)).findByProjectId(1);
    }
}