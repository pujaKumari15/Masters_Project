package com.master.project.dao;

import com.master.project.model.Device;
import com.master.project.model.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DeviceStatusDao extends JpaRepository<DeviceStatus, String> {
    List<DeviceStatus> findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}