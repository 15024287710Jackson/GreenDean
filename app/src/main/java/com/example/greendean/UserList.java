package com.example.greendean;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> userlist;
    private boolean isUserList;
    private boolean isEmpty;

    public UserList() {
        System.out.println("创建USERLIST！");
        this.userlist = new ArrayList<User>();
        this.isUserList = false;
        this.isEmpty = true;
    }

    public boolean isEmpty()
    {
        return this.isEmpty;
    }


    public void setEmpty(boolean i)
    {
        this.isEmpty = i;
    }

    public void setIsUserList(boolean j)
    {
        this.isUserList = j;
    }

    public boolean isUserList() {
        if(isUserList)
        {
            return true;
        }
        else return false;
    }
    public void addUser(User user)
    {
        this.userlist.add(user);
    }

    public void cleanuser()
    {
        this.userlist.clear();
    }

    public ArrayList<User> getuserlist(){
        return userlist;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}