package com.example.demo.UserMVCTest;

import com.example.demo.Controller.MilestoneController;
import com.example.demo.Models.Milestone;
import com.example.demo.Service.MilestoneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MilestoneController.class)
public class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MilestoneService milestoneService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMilestone() throws Exception {
        Milestone milestone = new Milestone();
        milestone.setMilestoneId(1);
        milestone.setMilestoneName("Milestone 1");

        when(milestoneService.createMilestone(any(Milestone.class))).thenReturn(milestone);

        mockMvc.perform(post("/milestones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(milestone)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(milestone)));
    }

    @Test
    void testGetAllMilestones() throws Exception {
        Milestone milestone1 = new Milestone();
        Milestone milestone2 = new Milestone();
        List<Milestone> milestones = new ArrayList<>();
        milestones.add(milestone1);
        milestones.add(milestone2);

        when(milestoneService.getAllMilestones()).thenReturn(milestones);

        mockMvc.perform(get("/milestones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(milestones)));
    }
}
