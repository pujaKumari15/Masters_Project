package com.master.project.service;

import com.master.project.dao.DeviceDao;
import com.master.project.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private DeviceStatusService deviceStatusService;

    // Create a new device
    public Device createDevice(Device device) {

        device.setId(UUID.randomUUID().toString());
        device.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        device.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        Device createdDevice = deviceDao.save(device);

        deviceStatusService.addDeviceHistory(createdDevice.getId(), "CREATE", createdDevice.getCurrentStatus());

        return createdDevice;
    }

    // Get a device by its ID
    public Optional<Device> getDeviceById(String id) {
        return deviceDao.findById(id);
    }

    // Update a device
    public Device updateDevice(String id, Device deviceDetails) {
        Optional<Device> deviceOptional = deviceDao.findById(id);

        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            if(deviceDetails.getName() != null)
                device.setName(deviceDetails.getName());
            if(deviceDetails.getDeviceType() != null)
                device.setDeviceType(deviceDetails.getDeviceType());
            if(deviceDetails.getOwnerId() != null)
                device.setOwnerId(deviceDetails.getOwnerId());
            if(deviceDetails.getCurrentStatus() != null)
                device.setCurrentStatus(deviceDetails.getCurrentStatus());
            if(deviceDetails.getConfig() != null)
                device.setConfig(deviceDetails.getConfig());
            device.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            Device updatedDevice = deviceDao.save(device);

            deviceStatusService.addDeviceHistory(updatedDevice.getId(), "UPDATE", updatedDevice.getCurrentStatus());

            return updatedDevice;
        }
        return null;
    }

    // Delete a device by its ID
    @Transactional
    public boolean deleteDevice(String id) {
        Optional<Device> device = deviceDao.findById(id);
        if (device.isPresent()) {
            deviceStatusService.deleteDeviceHistory(id);
            deviceDao.delete(device.get());
            return true;
        }
        return false;
    }

    public List<Device> getDevicesByUserId(String userId) {
        return deviceDao.findByOwnerId(userId); // This fetches the devices by ownerId
    }
}

