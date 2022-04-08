package com.xxx.crm.model;

public class UserModel {
    private String userId;
    private String userName;
    private String trueName;

    public UserModel() {
    }

    public UserModel(String userId, String userName, String trueName) {
        this.userId = userId;
        this.userName = userName;
        this.trueName = trueName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", trueName='" + trueName + '\'' +
                '}';
    }
}
