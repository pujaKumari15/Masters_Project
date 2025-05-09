package com.master.project.dao;

import com.master.project.model.Automation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AutomationDao extends JpaRepository<Automation, String> {
    // Custom query to find automation records by userId
    List<Automation> findByUserId(String userId);
}

