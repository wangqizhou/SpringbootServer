package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */
@Component
public class AdminDao {

    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public AdminDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public Admin selectAdminById(int id) {
        return this.sqlSession.selectOne("selectAdminById", id);
    }

    public Admin selectAdminByName(String name) {
        return this.sqlSession.selectOne("selectAdminByName", name);
    }

    public List<Admin> selectAdmins() {
        return this.sqlSession.selectList("selectAdmins");
    }

    public List<Admin> selectAdmins(int pageNum, int pageSize) {
        return this.sqlSession.selectList("selectAdmins", null,
                this.pageHelper.getRowBounds(pageNum, pageSize));
    }

    public int selectAdminCount() {
        return this.sqlSession.selectOne("selectAdminCount");
    }

    public int insertAdmin(Admin admin) {
        return this.sqlSession.insert("insertAdmin", admin);
    }

    public int deleteAdmin(Admin admin) {
        return this.sqlSession.delete("deleteAdmin", admin);
    }

    public int deleteAdminById(int id) {
        return this.sqlSession.delete("deleteAdminById", id);
    }

    public int deleteAdminByName(String name) {
        return this.sqlSession.delete("deleteAdminByName", name);
    }

    public int updateAdmin(Admin admin) {
        return this.sqlSession.update("updateAdmin", admin);
    }
}
