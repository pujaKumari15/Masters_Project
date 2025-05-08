package com.master.project.controller;

import com.master.project.model.EnergyRecord;
import com.master.project.service.EnergyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/v1/energy")
public class EnergyRecordController {

    @Autowired
    private EnergyRecordService energyRecordService;

    // Endpoint to get energy records filtered by user_id and date range
    @GetMapping("/records")
    public ResponseEntity<List<EnergyRecord>> getEnergyRecords(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // Parse date strings into Timestamp
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        // Get the filtered energy records
        List<EnergyRecord> energyRecords = energyRecordService.getEnergyRecordsByUserIdAndDateRange(userId, startTimestamp, endTimestamp);

        if (!energyRecords.isEmpty()) {
            return ResponseEntity.ok(energyRecords);  // Return filtered records
        }
        return ResponseEntity.notFound().build();  // Return 404 if no records found
    }
}

