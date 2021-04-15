package com.example.greendean;

import com.alibaba.fastjson.JSON;

public class User {
    private String userId;
    private String userName;
    private int userImg;
    private Msg userMsg;

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userImg) {
        this.userImg = userImg;
    }

    public User(String userId, String userName, int userImg) {
        this.userId = userId;
        this.userName = userName;
        this.userImg = userImg;
    }

    public User() {
    }

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public User(String userId, String userName, Msg userMsg) {
        this.userId = userId;
        this.userName = userName;
        this.userMsg = userMsg;
    }

    public User(String userId, String userName, int userImg, Msg userMsg) {
        this.userId = userId;
        this.userName = userName;
        this.userImg = userImg;
        this.userMsg = userMsg;
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

    public Msg getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(Msg userMsg) {
        this.userMsg = userMsg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
