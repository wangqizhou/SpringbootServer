package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.AdminDao;
import com.evistek.mediaserver.dao.AuthorityDao;
import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.entity.Authority;
import com.evistek.mediaserver.service.IAdminService;
import com.evistek.mediaserver.service.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */
@Service
public class AdminServiceImpl implements IAdminService {
    private final IAuthService rsaService;
    private final IAuthService desService;
    private final AdminDao adminDao;
    private final AuthorityDao authorityDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public AdminServiceImpl(@Qualifier("RSAService") IAuthService rsaService,
                            @Qualifier("DESService") IAuthService desService,
                            AdminDao adminDao, AuthorityDao authorityDao) {
        this.rsaService = rsaService;
        this.desService = desService;
        this.adminDao = adminDao;
        this.authorityDao = authorityDao;
    }

    @Override
    public List<Admin> findAdmins() {
        return this.adminDao.selectAdmins();
    }

    @Override
    public List<Admin> findAdmins(int pageNum, int pageSize) {
        return this.adminDao.selectAdmins(pageNum, pageSize);
    }

    @Override
    public Admin findAdminByName(String username) {
        return this.adminDao.selectAdminByName(username);
    }

    @Override
    public Admin findAdminById(int id) {
        return this.adminDao.selectAdminById(id);
    }

    @Override
    public int findAdminNumber() {
        return this.adminDao.selectAdminCount();
    }

    @Override
    public List<String> findAdminRoles() {
        List<String> list = new ArrayList<>();

        list.add(ROLE_UPLOAD);
        list.add(ROLE_AUDIT);
        list.add(ROLE_MODIFY);
        list.add(ROLE_STAT);
        list.add(ROLE_DBA);
        list.add(ROLE_USER_MANAGER);
        list.add(ROLE_ADMIN);

        return list;
    }

    @Override
    @Transactional
    public Admin addAdmin(Admin admin) {
        int result = this.adminDao.insertAdmin(admin);
        if (result == 1) {
            Authority authority = new Authority();
            authority.setUsername(admin.getUsername());
            authority.setAuthority(admin.getAuthority());

            result = this.authorityDao.insertAuthority(authority);
            if (result != 1) {
                logger.error("fail to add admin with name: " + admin.getUsername());
                throw new RuntimeException("add admin failed caused by authority insert failure");
            }
            return this.adminDao.selectAdminByName(admin.getUsername());
        } else {
            logger.error("fail to add admin with name: " + admin.getUsername());
            return null;
        }
    }

    @Override
    public int deleteAdmin(Admin admin) {
        return this.adminDao.deleteAdmin(admin);
    }

    @Override
    public int deleteAdminById(int id) {
        return this.adminDao.deleteAdminById(id);
    }

    @Override
    public int deleteAdminByName(String name) {
        return this.adminDao.deleteAdminByName(name);
    }

    @Override
    @Transactional
    public int updateAdmin(Admin admin) {
        if (admin.getAuthority() == null) {
            return this.adminDao.updateAdmin(admin);
        }

        // 密码加密后再存入数据库
        if (admin.getPassword() != null) {
            RSAPrivateKey rsaPrivateKey = this.rsaService.getPrivateKey();
            if (rsaPrivateKey != null) {
                try {
                    String clientPasswordString = new String(
                            this.rsaService.decryptFromBase64String(admin.getPassword(), rsaPrivateKey), "UTF-8");
                    String serverPasswordString = this.desService.encryptToBase64String(clientPasswordString,
                            this.desService.getKeyForPassword());
                    admin.setPassword(serverPasswordString);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    logger.error("UnsupportedEncodingException for UTF-8");
                }
            } else {
                logger.error("RSA private key is null");
            }
        }

        int result = this.adminDao.updateAdmin(admin);
        if (result == 1) {
            Authority authority = new Authority();
            authority.setUsername(admin.getUsername());
            authority.setAuthority(admin.getAuthority());
            if (this.authorityDao.updateAuthorityByUsername(authority) != 1) {
                throw new RuntimeException("updateAdmin failed caused by authority update failure.");
            } else {
                return 1;
            }
        }

        return 0;
    }
}
