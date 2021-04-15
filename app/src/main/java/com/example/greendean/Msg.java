package com.example.greendean;

import com.alibaba.fastjson.JSON;

public class Msg {
    private boolean send;//是否是发送的消息
    private String content;//发送的内容
    private boolean system;//是否是系统消息

    public Msg(boolean send, String content) {
        this.send = send;
        this.content = content;
        this.system = false;
    }

    public Msg() {
    }

    public Msg(boolean send, String content, boolean system) {
        this.send = send;
        this.content = content;
        this.system = system;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }



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
