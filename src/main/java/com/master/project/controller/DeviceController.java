package com.master.project.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.project.dto.DeviceStatusDto;
import com.master.project.enums.MqttCaller;
import com.master.project.model.Device;
import com.master.project.model.DeviceStatus;
import com.master.project.service.DeviceService;
import com.master.project.service.DeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceStatusService deviceStatusService;

    // Create a new device
    @PostMapping("/")
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Device createdDevice = deviceService.createDevice(device);
        return ResponseEntity.ok(createdDevice);
    }

    // Get a device by ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id) {
        Optional<Device> device = deviceService.getDeviceById(id);
        // Device not found
        return device.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a device by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        boolean isDeleted = deviceService.deleteDevice(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();  // Successfully deleted
        }
        return ResponseEntity.notFound().build();  // Device not found
    }

    // Get all devices by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Device>> getDevicesByUserId(@PathVariable String userId) {
        List<Device> devices = deviceService.getDevicesByUserId(userId);
        return ResponseEntity.ok(devices);
    }

    // Update a device
    @PutMapping("/{deviceId}")
    public ResponseEntity<Device> updateDevice(@PathVariable String deviceId, @RequestBody Device deviceDetails) {
        Device updatedDevice = deviceService.updateDevice(deviceId, deviceDetails, MqttCaller.USER);
        if (updatedDevice != null) {
            return ResponseEntity.ok(updatedDevice);
        }
        return ResponseEntity.notFound().build();  // Device not found
    }

    // Get device status by device ID
    @GetMapping("/{deviceId}/status")
    public ResponseEntity<List<DeviceStatus>> getDeviceStatusByDeviceId(@PathVariable String deviceId) {
        List<DeviceStatus> deviceStatuses = deviceStatusService.getDeviceStatusByDeviceId(deviceId);
        if (deviceStatuses.isEmpty()) {
            return ResponseEntity.notFound().build();  // Return 404 if no status found for device
        }
        return ResponseEntity.ok(deviceStatuses);  // Return the list of statuses
    }

    // Update device status
    @PutMapping("/{deviceId}/status")
    public ResponseEntity<Device> updateDeviceStatus(@PathVariable String deviceId, @RequestBody DeviceStatusDto status) {
        Device existingDevice = deviceService.getDeviceById(deviceId).orElse(null);
        if (existingDevice == null) {
            return ResponseEntity.notFound().build();  // Device not found
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String jsonString;
        try {
            jsonString = mapper.writeValueAsString(status);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).build();
        }

        log.info("updateDeviceStatus JSON String: {}", jsonString);
        existingDevice.setCurrentStatus(jsonString);

        Device updatedDevice = deviceService.updateDevice(deviceId, existingDevice, MqttCaller.USER);

        if (updatedDevice != null) {
            return ResponseEntity.ok(updatedDevice);
        }
        return ResponseEntity.status(500).build();
    }
}
