package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.entity.User;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/20.
 */
@Component
public class UserDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public UserDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public User selectUserByName(String name) {
        return this.sqlSession.selectOne("selectUserByName", name);
    }

    public User selectUserById(int id) {
        return this.sqlSession.selectOne("selectUserById", id);
    }

    public User selectUserByPhone(String phone) {
        return this.sqlSession.selectOne("selectUserByPhone", phone);
    }

    public User selectUserByEmail(String email) {
        return this.sqlSession.selectOne("selectUserByEmail", email);
    }

    public List<User> selectUsers() {
        return this.sqlSession.selectList("selectUsers");
    }

    public List<User> selectUsers(OrderBy orderBy) {
        return this.sqlSession.selectList("selectUsersOrderBy", orderBy);
    }

    public List<User> selectUsers(int pageNum, int pageSize) {
        return this.sqlSession.selectList("selectUsers", null,
                this.pageHelper.getRowBounds(pageNum, pageSize));
    }

    public List<User> selectUsers(int pageNum, int pageSize, OrderBy orderBy) {
        return this.sqlSession.selectList("selectUsersOrderBy", orderBy,
                this.pageHelper.getRowBounds(pageNum, pageSize));
    }

    public List<User> selectUsersByRegisterTime(int year, int month, int day) {
        Map<String, Object> map = new HashMap<>();
        if (year > 0) {
            map.put("year", year);
            if (month > 0 && month <= 12) {
                map.put("month", month);
                if (day > 0 && day <= 31) {
                    map.put("day", day);
                }
            }
        }

        return this.sqlSession.selectList("selectUsersByRegisterTime", map);
    }

    public List<User> selectUsersByRegisterTime(int year, int month, int day, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        if (year > 0) {
            map.put("year", year);
            if (month > 0 && month <= 12) {
                map.put("month", month);
                if (day > 0 && day <= 31) {
                    map.put("day", day);
                }
            }
        }
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectUsersByRegisterTimeOrderBy", map);
    }

    public List<User> selectUsersByRegisterTime(int year, int month, int day, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (year > 0) {
            map.put("year", year);
            if (month > 0 && month <= 12) {
                map.put("month", month);
                if (day > 0 && day <= 31) {
                    map.put("day", day);
                }
            }
        }

        return this.sqlSession.selectList("selectUsersByRegisterTime", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<User> selectUsersByRegisterTime(int year, int month, int day, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        if (year > 0) {
            map.put("year", year);
            if (month > 0 && month <= 12) {
                map.put("month", month);
                if (day > 0 && day <= 31) {
                    map.put("day", day);
                }
            }
        }
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectUsersByRegisterTimeOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<User> selectUsersByLocation(String location) {
        return this.sqlSession.selectList("selectUsersByLocation", location);
    }

    public List<User> selectUsersByLocation(String location, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectUsersByLocationOrderBy", map);
    }

    public List<User> selectUsersByLocation(String location, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectUsersByLocation", location,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<User> selectUsersByLocation(String location, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectUsersByLocationOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public int selectUserCount() {
        return this.sqlSession.selectOne("selectUserCount");
    }

    public int insertUser(User user) {
        return this.sqlSession.insert("insertUser", user);
    }

    public int deleteUser(User user) {
        return this.sqlSession.delete("deleteUser", user);
    }

    public int deleteUserById(int id) {
        return this.sqlSession.delete("deleteUserById", id);
    }

    public int deleteUserByName(String name) {
        return this.sqlSession.delete("deleteUserByName", name);
    }

    public int updateUser(User user) {
        return this.sqlSession.update("updateUser", user);
    }

    public List<StatisticUser> getStatisticUserByMonth(Date date, int num, String query_type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        map.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticUserByMonth", map);
    }

    public List<StatisticUser> getStatisticUserByMonth(Date date, int num, String query_type, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        map.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticUserByMonth", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticUser> getStatisticUserByWeek(Date date, int num, String query_type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        map.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticUserByWeek", map);
    }

    public List<StatisticUser> getStatisticUserByWeek(Date date, int num, String query_type, int paegNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        map.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticUserByWeek", map, this.pageHelper.getRowBounds(paegNumber, pageSize));
    }

    public List<StatisticUser> getStatisticUserByDay(Date date, int num, String query_type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        map.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticUserByDay", map);
    }

    public List<StatisticUser> getStatisticUserByDay(Date date, int num, String query_type, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        map.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticUserByDay", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }
}
