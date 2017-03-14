package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Authority;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/22.
 */
@Component
public class AuthorityDao {
    private final SqlSession sqlSession;

    public AuthorityDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Authority selectAuthorityById(int id) {
        return this.sqlSession.selectOne("selectAuthorityById", id);
    }

    public Authority selectAuthorityByUsername(String username) {
        return this.sqlSession.selectOne("selectAuthorityByUsername", username);
    }

    public List<Authority> selectAuthorities() {
        return this.sqlSession.selectList("selectAuthorities");
    }

    public int insertAuthority(Authority authority) {
        return this.sqlSession.insert("insertAuthority", authority);
    }

    public int deleteAuthority(Authority authority) {
        return this.sqlSession.delete("deleteAuthority", authority);
    }

    public int deleteAuthorityById(int id) {
        return this.sqlSession.delete("deleteAuthorityById", id);
    }

    public int deleteAuthorityByUsername(String username) {
        return this.sqlSession.delete("deleteAuthorityByUsername", username);
    }

    public int updateAuthority(Authority authority) {
        return this.sqlSession.update("updateAuthority", authority);
    }

    public int updateAuthorityByUsername(Authority authority) {
        return this.sqlSession.update("updateAuthorityByUsername", authority);
    }
}
