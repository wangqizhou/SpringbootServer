package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Authority;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/22.
 */
public interface IAuthorityService {
    Authority findAuthorityById(int id);
    Authority findAuthorityByUsername(String username);
    List<Authority> findAuthorities();
    Authority addAuthority(Authority authority);
    int deleteAuthority(Authority authority);
    int deleteAuthorityById(int id);
    int deleteAuthorityByUsername(String username);
    int updateAuthority(Authority authority);
    int updateAuthorityByUsername(Authority authority);
}
