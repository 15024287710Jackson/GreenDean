package com.example.greendean;

import com.alibaba.fastjson.JSON;


public class Msg {
    private boolean send;//是否是发送的消息
    private String content;//发送的内容
    private boolean system;//是否是系统消息
    private boolean isPrivate;
    private String targetUserID;
    private boolean isConfi;

    public Msg(boolean send, String content) {
        this.send = send;
        this.content = content;
        this.system = false;
        this.isPrivate = false;
        this.targetUserID = "false";
        this.isConfi = false;
    }

    public Msg() {
    }

    public Msg(boolean send, String content, boolean system) {
        this.send = send;
        this.content = content;
        this.system = system;
    }

    public boolean isConfi() { return  isConfi; }

    public void setConfi(boolean isConfi) {this.isConfi = isConfi;}

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public boolean isPrivate() {return isPrivate;}

    public void setPrivate(boolean isPrivate ) {this.isPrivate = isPrivate;}

    public String getTargetUserID() { return targetUserID; }

    public void setTargetUserID(String TargetUserID) { this.targetUserID = TargetUserID; }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
