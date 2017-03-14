package com.evistek.mediaserver.entity;

import org.springframework.stereotype.Component;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */
@Component
public class Admin {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthority() {
        return authority;
    }

    /**
     * 设置管理员的权限
     *
     * @param authority 由IAdminService接口中的ROLE常量拼接而成的字符串，英文逗号或者分号隔开
     *                  例如，"ROLE_UPLOAD,ROLE_AUDIT"
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
