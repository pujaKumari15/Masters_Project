package com.master.project.service;


import com.master.project.dao.DeviceStatusDao;
import com.master.project.model.DeviceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceStatusService {

    @Autowired
    private DeviceStatusDao deviceStatusDao;

    // Get device status by device ID
    public List<DeviceStatus> getDeviceStatusByDeviceId(String deviceId) {
        return deviceStatusDao.findByDeviceId(deviceId);
    }

    // Method to add a history record when a device is created or updated
    public void addDeviceHistory(String deviceId, String changeType, String status) {
        DeviceStatus history = new DeviceStatus();
        history.setId(UUID.randomUUID().toString());
        history.setDeviceId(deviceId);
        history.setChangeType(changeType);  // Either "CREATE" or "UPDATE"
        history.setChangeTimestamp(new Timestamp(System.currentTimeMillis()));  // Set current timestamp
        history.setStatus(status);  // Store the details of the change in JSON format

        deviceStatusDao.save(history);  // Save the history record
    }

    public void deleteDeviceHistory(String deviceId) {
        deviceStatusDao.deleteByDeviceId(deviceId);
    }
}
