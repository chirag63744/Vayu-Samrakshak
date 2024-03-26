package com.example.vayusamrakshak;

import org.json.JSONArray;

public class UserInfo {

    private String userName;
    private String userEmail;
    private String userNumber;
    public UserInfo() {

    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }
    public String getUserEmail() {
        return userEmail;
    }


    public void setUserName(String name) {
        this.userName = name;
    }
    public String getUserName() {
        return userName;
    }


    public void setUserNumber(String number) {
        this.userNumber = number;
    }

    public String getUserNumber() {
        return userNumber;
    }

}
