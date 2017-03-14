package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Admin;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/20.
 */
public interface IAdminService {
    String ROLE_UPLOAD = "ROLE_UPLOAD";
    String ROLE_AUDIT = "ROLE_AUDIT";
    String ROLE_MODIFY = "ROLE_MODIFY";
    String ROLE_STAT = "ROLE_STAT";
    String ROLE_DBA = "ROLE_DBA";
    String ROLE_USER_MANAGER = "ROLE_USER_MANAGER";
    String ROLE_ADMIN = "ROLE_ADMIN";

    List<Admin> findAdmins();
    List<Admin> findAdmins(int pageNum, int pageSize);
    Admin findAdminByName(String username);
    Admin findAdminById(int id);
    int findAdminNumber();
    List<String> findAdminRoles();
    Admin addAdmin(Admin admin);
    int deleteAdmin(Admin admin);
    int deleteAdminById(int id);
    int deleteAdminByName(String name);
    int updateAdmin(Admin admin);
}
