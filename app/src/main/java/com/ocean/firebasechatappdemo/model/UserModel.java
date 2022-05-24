package com.ocean.firebasechatappdemo.model;


import java.io.Serializable;

import lombok.Data;


public class UserModel implements Serializable {

    private String userName,userPhoneNum,userEmail,userJobStatus,userImage,userID,contactList;
    private Long lastLogin;

    public UserModel() {
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.userEmail = userEmail;
        this.userJobStatus = userJobStatus;
        this.userImage = userImage;
        this.userID = userID;
        this.contactList = contactList;
        this.lastLogin = lastLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserJobStatus() {
        return userJobStatus;
    }

    public void setUserJobStatus(String userJobStatus) {
        this.userJobStatus = userJobStatus;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContactList() {
        return contactList;
    }

    public void setContactList(String contactList) {
        this.contactList = contactList;
    }

    public Long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
    }
}
