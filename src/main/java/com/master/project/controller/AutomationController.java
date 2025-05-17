package com.master.project.controller;

import com.master.project.model.Automation;
import com.master.project.service.AutomationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/automation")
public class AutomationController {

    @Autowired
    private AutomationService automationService;

    // Create a new automation record
    @PostMapping("/")
    public ResponseEntity<Automation> createAutomation(@RequestBody Automation automation) {
        Automation createdAutomation = automationService.createAutomation(automation);
        return ResponseEntity.ok(createdAutomation);
    }

    // Get automations by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Automation>> getAutomationsByUserId(@PathVariable String userId) {
        List<Automation> automations = automationService.getAutomationsByUserId(userId);
        return ResponseEntity.ok(automations);
    }

    // Get automation by id
    @GetMapping("/{id}")
    public ResponseEntity<Automation> getAutomationById(@PathVariable String id) {
        Optional<Automation> automation = automationService.getAutomationById(id);
        return automation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update automation by id
    @PutMapping("/{id}")
    public ResponseEntity<Automation> updateAutomationById(@PathVariable String id, @RequestBody Automation automationDetails) {
        Optional<Automation> updatedAutomation = automationService.updateAutomationById(id, automationDetails);
        return updatedAutomation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete automation by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutomationById(@PathVariable String id) {
        boolean isDeleted = automationService.deleteAutomationById(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

