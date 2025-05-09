package com.master.project.service;

import com.master.project.dao.EnergyRecordDao;
import com.master.project.model.EnergyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
public class EnergyRecordService {

    @Autowired
    private EnergyRecordDao energyRecordRepository;

    // Method to get energy records filtered by user_id and date range
    public List<EnergyRecord> getEnergyRecordsByUserIdAndDateRange(String userId, Timestamp startDate, Timestamp endDate) {
        return energyRecordRepository.findByUserIdAndTimestampBetweenOrderByTimestampAsc(userId, startDate, endDate);
    }
}

