package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.persistence.*;

public class UserDTO {
    private String firebaseUid;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String picture;

    public String getFirebaseUid(){
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid){
        this.firebaseUid = firebaseUid;
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getPicture(){
        return picture;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }
}