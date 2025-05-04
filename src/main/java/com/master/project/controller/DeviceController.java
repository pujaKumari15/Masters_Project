package com.master.project.controller;

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
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/device")
public class DeviceController {
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

    // Update a device
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable String id, @RequestBody Device deviceDetails) {
        Device updatedDevice = deviceService.updateDevice(id, deviceDetails);
        if (updatedDevice != null) {
            return ResponseEntity.ok(updatedDevice);
        }
        return ResponseEntity.notFound().build();  // Device not found
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

    // Get device status by device ID
    @GetMapping("/{deviceId}/status")
    public ResponseEntity<List<DeviceStatus>> getDeviceStatusByDeviceId(@PathVariable String deviceId) {
        List<DeviceStatus> deviceStatuses = deviceStatusService.getDeviceStatusByDeviceId(deviceId);

        if (deviceStatuses.isEmpty()) {
            return ResponseEntity.notFound().build();  // Return 404 if no status found for device
        }

        return ResponseEntity.ok(deviceStatuses);  // Return the list of statuses
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Device>> getDevicesByUserId(@PathVariable String userId) {
        List<Device> devices = deviceService.getDevicesByUserId(userId);
        if (!devices.isEmpty()) {
            return ResponseEntity.ok(devices);  // Return the list of devices with status 200 OK
        }
        return ResponseEntity.notFound().build();  // If no devices are found for the user, return 404
    }
}
