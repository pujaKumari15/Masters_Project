package com.master.project.dao;

import com.master.project.model.Automation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutomationDao extends JpaRepository<Automation, String> {
    // Custom query to find automation records by userId
    List<Automation> findByUserId(String userId);

    // query to find automation records by deviceId
    @Query(value = "SELECT * FROM automation WHERE JSON_CONTAINS(triggers, JSON_QUOTE(:deviceId), '$.deviceId')", nativeQuery = true)
    List<Automation> findByDeviceIdInTriggers(@Param("deviceId") String deviceId);

}

