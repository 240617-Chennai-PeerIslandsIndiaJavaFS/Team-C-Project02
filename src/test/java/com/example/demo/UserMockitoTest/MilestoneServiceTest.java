package com.example.demo.UserMockitoTest;

import com.example.demo.Models.Milestone;
import com.example.demo.Repository.MilestoneRepository;
import com.example.demo.Service.MilestoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MilestoneServiceTest {

    @Mock
    private MilestoneRepository milestoneRepository;

    @InjectMocks
    private MilestoneService milestoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMilestone() {
        Milestone milestone = new Milestone();
        milestone.setMilestoneId(1);
        milestone.setMilestoneName("Milestone 1");

        when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);

        Milestone createdMilestone = milestoneService.createMilestone(milestone);

        assertEquals(milestone, createdMilestone);
        verify(milestoneRepository, times(1)).save(milestone);
    }

    @Test
    void testGetAllMilestones() {
        Milestone milestone1 = new Milestone();
        Milestone milestone2 = new Milestone();
        List<Milestone> milestones = new ArrayList<>();
        milestones.add(milestone1);
        milestones.add(milestone2);

        when(milestoneRepository.findAll()).thenReturn(milestones);

        List<Milestone> retrievedMilestones = milestoneService.getAllMilestones();

        assertEquals(milestones, retrievedMilestones);
        verify(milestoneRepository, times(1)).findAll();
    }

    @Test
    void testGetMilestoneById() {
        Milestone milestone = new Milestone();
        milestone.setMilestoneId(1);

        when(milestoneRepository.findByMilestoneId(1)).thenReturn(milestone);

        Milestone retrievedMilestone = milestoneService.getMilestoneById(1);

        assertEquals(milestone, retrievedMilestone);
        verify(milestoneRepository, times(1)).findByMilestoneId(1);
    }
}
