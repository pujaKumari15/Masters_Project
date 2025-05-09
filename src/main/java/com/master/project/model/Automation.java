package com.master.project.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "automation")
public class Automation {

    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    private String name;

    private Boolean enable;

    private String type;

    private String triggers;

    private String conditions;

    private String action;

    private String config;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "created_date")
    private Timestamp createdDate;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrigger() {
        return triggers;
    }

    public void setTrigger(String triggers) {
        this.triggers = triggers;
    }

    public String getCondition() {
        return conditions;
    }

    public void setCondition(String conditions) {
        this.conditions = conditions;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
