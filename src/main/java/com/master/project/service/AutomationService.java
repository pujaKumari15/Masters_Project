package com.master.project.service;

import com.master.project.model.Automation;
import com.master.project.model.Device;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.project.dao.AutomationDao;
import com.master.project.dto.AutomationTriggerDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutomationService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutomationService.class);

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

    // GetAutomation by deviceId
    public List<Automation> getAutomationsByDeviceId(String deviceId) {
        return automationDao.findByDeviceIdInTriggers(deviceId);
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

    // check automation condition
    public boolean checkAutomationCondition(Automation automation, Device device) {
        AutomationTriggerDto trigger = automation.getTriggerObject();
        if (trigger.getDeviceId().equals(device.getId())) {
            String operator = trigger.getOperator(); // operator "=", "<", ">", "<=", ">="
            String attribute = trigger.getAttribute();
            Object value = trigger.getValue();
            Object currentValue = null;
            try {
                // Assuming currentStatus is a JSON string, parse it to a Map
                ObjectMapper objectMapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> statusMap = objectMapper.readValue(device.getCurrentStatus(), java.util.Map.class);
                currentValue = statusMap.get(attribute);
            } catch (Exception ex) {
                log.error("Automation Failed to parse currentStatus JSON: {}", ex.getMessage());
            }

            // Check if the condition is met
            boolean conditionMet = false;
            if (operator.equals("=")) {
                conditionMet = currentValue != null && currentValue.equals(value);
            } else if (operator.equals("<")) {
                if (currentValue instanceof Comparable && value != null) {
                    @SuppressWarnings("unchecked")
                    Comparable<Object> comparableValue = (Comparable<Object>) currentValue;
                    conditionMet = comparableValue.compareTo(value) < 0;
                } else {
                    log.warn("Automation Current value is not comparable or value is null for automation {}", automation.getId());
                }
            } else if (operator.equals(">")) {
                if (currentValue instanceof Comparable && value != null) {
                    @SuppressWarnings("unchecked")
                    Comparable<Object> comparableValue = (Comparable<Object>) currentValue;
                    conditionMet = comparableValue.compareTo(value) > 0;
                } else {
                    log.warn("Automation Current value is not comparable or value is null for automation {}", automation.getId());
                }
            } else if (operator.equals("<=")) {
                if (currentValue instanceof Comparable && value != null) {
                    @SuppressWarnings("unchecked")
                    Comparable<Object> comparableValue = (Comparable<Object>) currentValue;
                    conditionMet = comparableValue.compareTo(value) <= 0;
                } else {
                    log.warn("Automation Current value is not comparable or value is null for automation {}", automation.getId());
                }
            } else if (operator.equals(">=")) {
                if (currentValue instanceof Comparable && value != null) {
                    @SuppressWarnings("unchecked")
                    Comparable<Object> comparableValue = (Comparable<Object>) currentValue;
                    conditionMet = comparableValue.compareTo(value) >= 0;
                } else {
                    log.warn("Automation Current value is not comparable or value is null for automation {}", automation.getId());
                }
            } else {
                log.warn("Automation Unknown operator '{}' for automation {}", operator, automation.getId());
            }

            if (conditionMet) {
                // Execute the action
                log.info("Automation Condition met for automation {}. Executing action.", automation.getId());
                // Assuming the action is to update the device status
                // Device actionDevice = new ObjectMapper().readValue(automation.getAction().getMessage(), Device.class);
                // deviceService.updateDeviceStatus(actionDevice.getId(), actionDevice.getCurrentStatus());
                return true;  // Return true if the action was executed
            } else {
                log.info("Automation Condition not met for automation {}. No action taken.", automation.getId());
                return false;  // Return false if the condition is not met
            }
        }
        return false;  // Return false if the condition is not met
    }

}

