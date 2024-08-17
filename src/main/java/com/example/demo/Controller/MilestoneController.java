package com.example.demo.Controller;

import com.example.demo.Models.Milestone;
import com.example.demo.Service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @PostMapping
    public ResponseEntity<Milestone> createMilestone(@RequestBody Milestone milestone) {
        Milestone createdMilestone = milestoneService.createMilestone(milestone);
        return new ResponseEntity<>(createdMilestone, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Milestone>> getAllMilestones() {
        List<Milestone> milestones = milestoneService.getAllMilestones();
        return new ResponseEntity<>(milestones, HttpStatus.OK);
    }
}
