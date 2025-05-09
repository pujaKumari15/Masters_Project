package com.master.project.dao;

import com.master.project.model.EnergyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface EnergyRecordDao extends JpaRepository<EnergyRecord, String> {

    // Custom query to get energy records filtered by user_id and date range
    @Query("SELECT e FROM EnergyRecord e WHERE e.userId = :userId AND e.timestamp BETWEEN :startDate AND :endDate ORDER BY e.timestamp ASC")
    List<EnergyRecord> findByUserIdAndTimestampBetweenOrderByTimestampAsc(String userId, Timestamp startDate, Timestamp endDate);
}

