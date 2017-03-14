package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Admin;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ymzhao on 2017/1/24.
 */
@Component
public class EmailGroupDao {
    private final SqlSession sqlSession;
    public EmailGroupDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    public List<Admin> selectMembers() {
        return this.sqlSession.selectList("selectMembers");
    }

    public int insert(Admin admin) {
        return this.sqlSession.insert("insertMember", admin);
    }

    public int delete(int id) {
        return this.sqlSession.delete("deleteMember", id);
    }
}
