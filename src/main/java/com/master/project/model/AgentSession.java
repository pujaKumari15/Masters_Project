package com.master.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="agent_session")
public class AgentSession {

    @Id
    private String id;
    private String userId;
    private Timestamp updatedDate;
    private Timestamp createdDate;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}

