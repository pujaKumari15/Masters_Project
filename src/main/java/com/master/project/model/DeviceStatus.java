package com.master.project.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name="device_status")
@Entity
public class DeviceStatus {

    @Id
    private String id;
    private String deviceId;
    private String changeType;
    private Timestamp changeTimestamp;
    private String status;

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getChangeType() {
        return changeType;
    }

    public Timestamp getChangeTimestamp() {
        return changeTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public void setChangeTimestamp(Timestamp changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

