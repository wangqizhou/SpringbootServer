package com.evistek.mediaserver.entity;

import org.springframework.stereotype.Component;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/22.
 */
@Component
public class Authority {
    private int id;
    private String username;
    private String authority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
