package com.example.demo.UserMVCTest;

import com.example.demo.Controller.ProjectController;
import com.example.demo.Models.Project;
import com.example.demo.Models.User;
import com.example.demo.Service.ProjectService;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;
    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateProject() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectName("Test Project");

        when(projectService.createProject(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(1))
                .andExpect(jsonPath("$.projectName").value("Test Project"));
    }

    @Test
    void testGetAllProjects() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectName("Test Project");

        List<Project> projects = Arrays.asList(project);

        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectId").value(1))
                .andExpect(jsonPath("$[0].projectName").value("Test Project"));
    }

    @Test
    void testGetProjectsByUsername() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectName("Test Project");

        List<Project> projects = Arrays.asList(project);

        when(projectService.getProjectsByUsername(anyString())).thenReturn(projects);

        mockMvc.perform(get("/projects/by-username")
                        .param("username", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectId").value(1))
                .andExpect(jsonPath("$[0].projectName").value("Test Project"));
    }

    @Test
    void testAddTeamMemberToProject() throws Exception {
        when(projectService.addTeamMemberToProject(anyInt(), anyInt())).thenReturn("Team member added successfully!");

        mockMvc.perform(put("/projects/1/add-team-member/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Team member added successfully!"));
    }

    @Test
    void testGetTeamMembersByProjectName() throws Exception {
        User user = new User();
        user.setUserid(1);
        user.setUsername("john");

        Set<User> teamMembers = new HashSet<>(Arrays.asList(user));

        when(projectService.getTeamMembersByProjectName(anyString())).thenReturn(teamMembers);

        mockMvc.perform(get("/projects/teamMembers")
                        .param("projectName", "Test Project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userid").value(1))
                .andExpect(jsonPath("$[0].username").value("john"));
    }

    @Test
    void testGetTeamMembersByProjectId() throws Exception {
        User user = new User();
        user.setUserid(1);
        user.setUsername("john");

        Set<User> teamMembers = new HashSet<>(Arrays.asList(user));

        when(projectService.getTeamMembersByProjectId(anyInt())).thenReturn(teamMembers);

        mockMvc.perform(get("/projects/getTeamMembersByProjectId")
                        .param("projectId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userid").value(1))
                .andExpect(jsonPath("$[0].username").value("john"));
    }
}
