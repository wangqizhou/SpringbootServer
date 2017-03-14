package com.evistek.mediaserver.config;

import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/16.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private IAdminService adminService;

    @Autowired
    public CustomUserDetailsService(IAdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Admin admin = adminService.findAdminByName(s);
        if (admin != null) {
            CustomUserDetails userDetails = new CustomUserDetails();

            userDetails.setId(admin.getId());
            userDetails.setUsername(admin.getUsername());
            userDetails.setPassword(admin.getPassword());
            userDetails.setEmail(admin.getEmail());
            userDetails.setEnabled(admin.isEnabled());
            userDetails.setAuthority(admin.getAuthority());
            return userDetails;
        }

        return null;
    }

    private class CustomUserDetails implements UserDetails {
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

        public void setUsername(String username) {
            this.username = username;
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

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean getEnabled() {
            return this.enabled;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }

        // UserDetails methods
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            //多个权限以英文的逗号或者分号进行分隔
            String[] authorities = authority.split("[,;]");

            ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            for (String str: authorities) {
                authorityList.add(new SimpleGrantedAuthority(str.trim()));
            }

            return authorityList;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }
}
