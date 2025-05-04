package com.master.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="device")
@Entity
public class Device {

    @Id
    private String id;
    private String name;
    private String deviceType;
    private String ownerId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private String currentStatus;
    private String config;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getConfig() {
        return config;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}

