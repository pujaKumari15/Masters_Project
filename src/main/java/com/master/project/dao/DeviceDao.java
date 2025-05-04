package com.master.project.dao;

import com.master.project.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DeviceDao extends JpaRepository<Device, String> {
    List<Device> findByOwnerId(String ownerId);
}
