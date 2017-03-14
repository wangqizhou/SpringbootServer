package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.dao.UserDao;
import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.entity.User;
import com.evistek.mediaserver.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.evistek.mediaserver.dao.OrderBy.ASC;
import static com.evistek.mediaserver.dao.OrderBy.DESC;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/20.
 */
@Service
public class UserServiceImpl implements IUserService {
    public static final int GRADE_MONTH = 0;
    public static final int GRADE_WEEK = 1;
    public static final int GRADE_DAYS = 2;
    public static final int QUERY_LOCATION = 1;
    public static final int QUERY_USUALLY = 2;
    private final UserDao userDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User findUserByName(String name) {
        return this.userDao.selectUserByName(name);
    }

    @Override
    public User findUserById(int id) {
        return this.userDao.selectUserById(id);
    }

    @Override
    public User findUserByPhone(String phone) {
        return this.userDao.selectUserByPhone(phone);
    }

    @Override
    public User findUserByEmail(String email) {
        return this.userDao.selectUserByEmail(email);
    }

    @Override
    public List<User> findUsers() {
        return this.userDao.selectUsers();
    }

    @Override
    public List<User> findUsers(OrderBy orderBy) {
        return this.userDao.selectUsers(validateOrderBy(orderBy));
    }

    @Override
    public List<User> findUsers(int pageNum, int pageSize) {
        return this.userDao.selectUsers(pageNum, pageSize);
    }

    @Override
    public List<User> findUsers(int pageNumber, int pageSize, OrderBy orderBy) {
        return this.userDao.selectUsers(pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<User> findUsersByRegisterTime(int year, int month, int day) {
        return this.userDao.selectUsersByRegisterTime(year, month, day);
    }

    @Override
    public List<User> findUsersByRegisterTime(int year, int month, int day, OrderBy orderBy) {
        return this.userDao.selectUsersByRegisterTime(year, month, day, validateOrderBy(orderBy));
    }

    @Override
    public List<User> findUsersByRegisterTime(int year, int month, int day, int pageNumber, int pageSize) {
        return this.userDao.selectUsersByRegisterTime(year, month, day, pageNumber, pageSize);
    }

    @Override
    public List<User> findUsersByRegisterTime(int year, int month, int day, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.userDao.selectUsersByRegisterTime(year, month, day, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<User> findUsersByLocation(String location) {
        return this.userDao.selectUsersByLocation(location);
    }

    @Override
    public List<User> findUsersByLocation(String location, OrderBy orderBy) {
        return this.userDao.selectUsersByLocation(location, orderBy);
    }

    @Override
    public List<User> findUsersByLocation(String location, int pageNumber, int pageSize) {
        return this.userDao.selectUsersByLocation(location, pageNumber, pageSize);
    }

    @Override
    public List<User> findUsersByLocation(String location, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.userDao.selectUsersByLocation(location, pageNumber, pageSize, orderBy);
    }

    @Override
    public int findUserNumber() {
        return this.userDao.selectUserCount();
    }

    @Override
    public User addUser(User user) {
        int result = this.userDao.insertUser(user);
        if (result == 1) {
            return this.userDao.selectUserByName(user.getUsername());
        } else {
            logger.error("fail to add user with name: " + user.getUsername());
            return null;
        }
    }

    @Override
    public int deleteUser(User user) {
        return this.userDao.deleteUser(user);
    }

    @Override
    public int deleteUserById(int id) {
        return this.userDao.deleteUserById(id);
    }

    @Override
    public int deleteUserByName(String name) {
        return this.userDao.deleteUserByName(name);
    }

    @Override
    public int updateUser(User user) {
        return this.userDao.updateUser(user);
    }

    @Override
    public List<StatisticUser> getStatisticRegisteredUser(int query_grade, int query_type, Date date, int num) {
        List<StatisticUser> userList = new ArrayList<StatisticUser>();
        switch (query_grade) {
            case GRADE_MONTH:
                if (query_type == QUERY_LOCATION) {
                    userList = this.userDao.getStatisticUserByMonth(date, num, "months, location");
                } else {
                    userList = this.userDao.getStatisticUserByMonth(date, num, "months");
                }
                break;
            case GRADE_WEEK:
                if (query_type == QUERY_LOCATION) {
                    userList = this.userDao.getStatisticUserByWeek(date, num, "weeks, location");
                } else {
                    userList = this.userDao.getStatisticUserByWeek(date, num, "weeks");
                }
                break;
            case GRADE_DAYS:
                if (query_type == QUERY_LOCATION) {
                    userList = this.userDao.getStatisticUserByDay(date, num, "days, location");
                } else {
                    userList = this.userDao.getStatisticUserByDay(date, num, "days");
                }
                break;
        }
        if (!userList.isEmpty()) {
            return userList;
        }
        return null;
    }

    @Override
    public List<StatisticUser> getStatisticRegisteredUser(int query_grade, int query_type, Date date, int num, int pageNumber, int pageSize) {
        List<StatisticUser> userList = new ArrayList<StatisticUser>();
        switch (query_grade) {
            case GRADE_MONTH:
                if (query_type == QUERY_LOCATION) {
                    userList = this.userDao.getStatisticUserByMonth(date, num, "months, location", pageNumber, pageSize);
                } else {
                    userList = this.userDao.getStatisticUserByMonth(date, num, "months", pageNumber, pageSize);
                }
                break;
            case GRADE_WEEK:
                if (query_type == QUERY_LOCATION) {
                    userList = this.userDao.getStatisticUserByWeek(date, num, "weeks, location", pageNumber, pageSize);
                } else {
                    userList = this.userDao.getStatisticUserByWeek(date, num, "weeks", pageNumber, pageSize);
                }
                break;
            case GRADE_DAYS:
                if (query_type == QUERY_LOCATION) {
                    userList = this.userDao.getStatisticUserByDay(date, num, "days, location", pageNumber, pageSize);
                } else {
                    userList = this.userDao.getStatisticUserByDay(date, num, "days", pageNumber, pageSize);
                }
                break;
        }
        if (!userList.isEmpty()) {
            return userList;
        }
        return null;
    }

    @Override
    public String exportXlsByRegisterUser(int query_grade, int query_type, Date date, int num) {
        String dateFormat = "%Y%m";
        String queryKey = "";
        String location = "";
        if (query_type == QUERY_LOCATION) {
            location = ", location";
        }
        String weeksFields = "";
        switch (query_grade) {
            case GRADE_MONTH:
                dateFormat = "%Y%m";
                queryKey = "months";
                break;
            case GRADE_WEEK:
                dateFormat = "%Y%u";
                queryKey = "weeks";
                weeksFields = ", date_sub(date_format(register_time,'%Y%m%d'),INTERVAL WEEKDAY(register_time) DAY) as starttime,"
                        + "date_sub(date_format(register_time,'%Y%m%d'),INTERVAL WEEKDAY(register_time) - 6 DAY) as endtime";
                break;
            case GRADE_DAYS:
                dateFormat = "%Y%m%d";
                queryKey = "days";
                break;
        }
        String queryDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("queryDate" + queryDate);
        String statement = "\"select  DATE_FORMAT(register_time,'" + dateFormat + "') " + queryKey + weeksFields + location + ", count(*) as"
                + " count from users where PERIOD_DIFF( date_format('" + queryDate + "' , '%Y%m' ) , date_format( register_time, '%Y%m' )"
                + " )> -1 && PERIOD_DIFF( date_format('" + queryDate + "' , '%Y%m' ) , date_format( register_time, '%Y%m' ) ) < "
                + num + "  group by "+ queryKey + location +";\"";
        return statement;
    }

    private OrderBy validateOrderBy(OrderBy orderBy) {
        List<String> keyList = Arrays.asList(
                ORDER_ID,
                ORDER_REGISTER_TIME,
                ORDER_USERNAME,
                ORDER_LOCATION
        );

        List<String> orderList = Arrays.asList(
                ASC,
                DESC
        );

        if (!keyList.contains(orderBy.getKey()) || !orderList.contains(orderBy.getOrder())) {
            logger.error("invalid parameters for ORDER BY: " + orderBy.getKey() + " " + orderBy.getOrder() + "."
                    + " Use ORDER BY id ASC as default."
            );
            return  new OrderBy(ORDER_ID, ASC);
        }

        return orderBy;
    }
}
