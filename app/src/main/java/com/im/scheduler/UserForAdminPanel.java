package com.im.scheduler;

public class UserForAdminPanel {
    String status,userID,userType,teacherEmailPath;

    public UserForAdminPanel() {
    }

    public UserForAdminPanel(String status, String userID, String userType, String teacherEmailPath) {
        this.status = status;
        this.userID = userID;
        this.userType = userType;
        this.teacherEmailPath = teacherEmailPath;
    }
}
