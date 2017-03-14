package com.evistek.mediaserver.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by ymzhao on 2017/3/6.
 */
@Component
public class Logger {
    private int id;
    private Date time;
    private String action;
    private String owner;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
