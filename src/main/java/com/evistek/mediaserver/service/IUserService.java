package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.entity.User;

import java.util.Date;
import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/20.
 */
public interface IUserService {
    String ORDER_ID = "id";
    String ORDER_REGISTER_TIME = "register_time";
    String ORDER_USERNAME = "username";
    String ORDER_LOCATION = "location";

    User findUserByName(String name);
    User findUserById(int id);
    User findUserByPhone(String phone);
    User findUserByEmail(String email);
    List<User> findUsers();
    List<User> findUsers(OrderBy orderBy);
    List<User> findUsers(int pageNumber, int pageSize);
    List<User> findUsers(int pageNumber, int pageSize, OrderBy orderBy);
    List<User> findUsersByRegisterTime(int year, int month, int day);
    List<User> findUsersByRegisterTime(int year, int month, int day, OrderBy orderBy);
    List<User> findUsersByRegisterTime(int year, int month, int day, int pageNumber, int pageSize);
    List<User> findUsersByRegisterTime(int year, int month, int day, int pageNumber, int pageSize, OrderBy orderBy);
    List<User> findUsersByLocation(String location);
    List<User> findUsersByLocation(String location, OrderBy orderBy);
    List<User> findUsersByLocation(String location, int pageNumber, int pageSize);
    List<User> findUsersByLocation(String location, int pageNumber, int pageSize, OrderBy orderBy);
    int findUserNumber();
    User addUser(User user);
    int deleteUser(User user);
    int deleteUserById(int id);
    int deleteUserByName(String name);
    int updateUser(User user);

    /**
     * 根据该时间点，查询该月/周/天的新增注册用户数且以地区进行分组
     * */

    List<StatisticUser> getStatisticRegisteredUser(int query_grade, int query_type, Date date, int num);

    List<StatisticUser> getStatisticRegisteredUser(int query_grade, int query_type, Date date, int num, int pageNumber, int pageSize);

    String exportXlsByRegisterUser(int query_grade, int query_type, Date date, int num);
}
