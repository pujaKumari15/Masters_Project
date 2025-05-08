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
@Table(name="energy_record")
@Entity
public class EnergyRecord {

    @Id
    private String id;
		private String userId;
		private Timestamp timestamp;
		private Double power;

		public String getId() {
				return id;
		}

		public String getUserId() {
				return userId;
		}

		public Timestamp getTimestamp() {
				return timestamp;
		}

		public Double getPower() {
				return power;
		}

		public void setId(String id) {
				this.id = id;
		}

		public void setUserId(String userId) {
				this.userId = userId;
		}

		public void setTimestamp(Timestamp timestamp) {
				this.timestamp = timestamp;
		}

		public void setPower(Double power) {
				this.power = power;
		}

}