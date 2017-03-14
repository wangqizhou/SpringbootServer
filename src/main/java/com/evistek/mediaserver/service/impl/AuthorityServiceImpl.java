package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.AuthorityDao;
import com.evistek.mediaserver.entity.Authority;
import com.evistek.mediaserver.service.IAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/22.
 */
@Service
public class AuthorityServiceImpl implements IAuthorityService {

    private final AuthorityDao authorityDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public AuthorityServiceImpl(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    @Override
    public Authority findAuthorityById(int id) {
        return this.authorityDao.selectAuthorityById(id);
    }

    @Override
    public Authority findAuthorityByUsername(String username) {
        return this.authorityDao.selectAuthorityByUsername(username);
    }

    @Override
    public List<Authority> findAuthorities() {
        return this.authorityDao.selectAuthorities();
    }

    @Override
    public Authority addAuthority(Authority authority) {
        int result = this.authorityDao.insertAuthority(authority);
        if (result == 1) {
            return this.authorityDao.selectAuthorityByUsername(authority.getUsername());
        } else {
            logger.error("fail to add authority with username: " + authority.getUsername());
            return null;
        }
    }

    @Override
    public int deleteAuthority(Authority authority) {
        return this.authorityDao.deleteAuthority(authority);
    }

    @Override
    public int deleteAuthorityById(int id) {
        return this.authorityDao.deleteAuthorityById(id);
    }

    @Override
    public int deleteAuthorityByUsername(String username) {
        return this.authorityDao.deleteAuthorityByUsername(username);
    }

    @Override
    public int updateAuthority(Authority authority) {
        return this.authorityDao.updateAuthority(authority);
    }

    @Override
    public int updateAuthorityByUsername(Authority authority) {
        return this.authorityDao.updateAuthorityByUsername(authority);
    }
}
