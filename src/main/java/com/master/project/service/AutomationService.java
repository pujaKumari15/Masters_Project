package com.master.project.service;

import com.master.project.model.Automation;
import com.master.project.dao.AutomationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutomationService {

    @Autowired
    private AutomationDao automationDao;

    // Create a new automation record
    public Automation createAutomation(Automation automation) {
        automation.setId(UUID.randomUUID().toString());
        automation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        automation.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return automationDao.save(automation);
    }

    // Get automations by userId
    public List<Automation> getAutomationsByUserId(String userId) {
        return automationDao.findByUserId(userId);
    }

    // Get automation by id
    public Optional<Automation> getAutomationById(String id) {
        return automationDao.findById(id);
    }

    // Update automation by id
    public Optional<Automation> updateAutomationById(String id, Automation automationDetails) {
        if (automationDao.existsById(id)) {
            automationDetails.setId(id);
            return Optional.of(automationDao.save(automationDetails));
        }
        return Optional.empty();  // Return empty if automation not found
    }

    // Delete automation by id
    public boolean deleteAutomationById(String id) {
        if (automationDao.existsById(id)) {
            automationDao.deleteById(id);
            return true;
        }
        return false;  // Return false if automation not found
    }
}

