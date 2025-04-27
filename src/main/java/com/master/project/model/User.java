package com.master.project.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="user")
@Entity
public class User {

    @Id
    private String userID;
    private String password;
    private char userType;

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public char getUserType() {
        return userType;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(char userType) {
        this.userType = userType;
    }
}
