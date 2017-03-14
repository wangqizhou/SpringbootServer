package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.entity.User;
import com.evistek.mediaserver.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-users-data.sql"})
public class UserServiceTests {
    @Autowired
    private IUserService userService;

    @Before
    public void setup() {

    }

    @Test
    public void findUser() {
        int result = this.userService.findUserNumber();
        assertThat(result).isEqualTo(16);

        List<User> userList = this.userService.findUsers();
        assertThat(userList.size()).isEqualTo(16);

        List<User> userList1 = this.userService.findUsers(1, 10);
        assertThat(userList1).isNotNull();
        assertThat(userList1.size()).isEqualTo(10);

        List<User> userList2 = this.userService.findUsers(2, 10);
        assertThat(userList2).isNotNull();
        assertThat(userList2.size()).isEqualTo(6);
        assertThat(userList2.get(0).getUsername()).isEqualTo("C457E6CEC5E7A13863EE7199D9C17715");

        List<User> userList3 = this.userService.findUsersByRegisterTime(2016, 0, 0);
        assertThat(userList3.size()).isEqualTo(16);
        List<User> userList4 = this.userService.findUsersByRegisterTime(2016, 3, 0);
        assertThat(userList4.size()).isEqualTo(3);
        List<User> userList5 = this.userService.findUsersByRegisterTime(2016, 7, 14);
        assertThat(userList5.size()).isEqualTo(4);
        List<User> userList6 = this.userService.findUsersByRegisterTime(2018, 13, -1);
        assertThat(userList6.size()).isEqualTo(0);

        List<User> userList7 = this.userService.findUsersByRegisterTime(2016, 8, 0, 2, 5);
        assertThat(userList7.size()).isEqualTo(1);

        List<User> userList8 = this.userService.findUsersByLocation("上海");
        assertThat(userList8.size()).isEqualTo(6);

        List<User> userList9 = this.userService.findUsersByLocation("上海", 2, 5);
        assertThat(userList9.size()).isEqualTo(1);

        User user = this.userService.findUserById(2);
        assertThat(user.getUsername()).isEqualTo("wxzhang@evistek.com");

        user = this.userService.findUserByName("2995E8713E30641845CEAB320E8D28D3");
        assertThat(user.getId()).isEqualTo(5);

        user = this.userService.findUserByEmail("ymzhao@evistek.com");
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(3);
        assertThat(user.getUsername()).isEqualTo("zym");

        user = this.userService.findUserByPhone("18616369689");
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getUsername()).isEqualTo("wxzhang@evistek.com");
    }

    @Test
    public void findUserOrderBy() {
        List<User> list = this.userService.findUsers(new OrderBy(IUserService.ORDER_REGISTER_TIME, OrderBy.DESC));
        assertThat(list.size()).isEqualTo(16);
        assertThat(list.get(0).getId()).isEqualTo(16);

        List<User> list1 = this.userService.findUsers(2, 10,
                new OrderBy(IUserService.ORDER_REGISTER_TIME, OrderBy.DESC));
        assertThat(list1.size()).isEqualTo(6);
        assertThat(list1.get(0).getId()).isEqualTo(6);

        List<User> list2 = this.userService.findUsersByRegisterTime(2016, 8, 0,
                new OrderBy(IUserService.ORDER_REGISTER_TIME, OrderBy.DESC));
        assertThat(list2.size()).isEqualTo(6);
        assertThat(list2.get(0).getId()).isEqualTo(14);

        List<User> list3 = this.userService.findUsersByRegisterTime(2016, 8, 0, 2, 3,
                new OrderBy(IUserService.ORDER_REGISTER_TIME, OrderBy.DESC));
        assertThat(list3.size()).isEqualTo(3);
        assertThat(list3.get(0).getId()).isEqualTo(11);

        List<User> list4 = this.userService.findUsersByLocation("上海",
                new OrderBy(IUserService.ORDER_REGISTER_TIME, OrderBy.DESC));
        assertThat(list4.size()).isEqualTo(6);
        assertThat(list4.get(0).getId()).isEqualTo(16);

        List<User> list5 = this.userService.findUsersByLocation("上海", 2, 3,
                new OrderBy(IUserService.ORDER_REGISTER_TIME, OrderBy.DESC));
        assertThat(list5.size()).isEqualTo(3);
        assertThat(list5.get(0).getId()).isEqualTo(8);
    }

    @Test
    public void findStatisticRegisteredUser() {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2016-08-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //月
        List<StatisticUser> userList = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_MONTH, UserServiceImpl.QUERY_LOCATION, date, 3);
        assertThat(userList.size()).isEqualTo(8);
        assertThat(userList.get(1).getCount()).isEqualTo(2);
        assertThat(userList.get(1).getLocation()).isEqualTo("上海 浦东新区");
        assertThat(userList.get(4).getCount()).isEqualTo(2);
        userList = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_MONTH, UserServiceImpl.QUERY_USUALLY, date, 3);
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(0).getCount()).isEqualTo(4);
        assertThat(userList.get(1).getCount()).isEqualTo(6);
        assertThat(userList.get(0).getGrade()).isEqualTo("201607");
        assertThat(userList.get(1).getGrade()).isEqualTo("201608");

        //分页
        userList = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_MONTH, UserServiceImpl.QUERY_LOCATION, date, 3, 3, 3);
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(1).getCount()).isEqualTo(1);
        assertThat(userList.get(1).getLocation()).isEqualTo("河北 保定");
        userList = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_MONTH, UserServiceImpl.QUERY_USUALLY, date, 3, 2,1);
        assertThat(userList.size()).isEqualTo(1);
        assertThat(userList.get(0).getCount()).isEqualTo(6);
        assertThat(userList.get(0).getGrade()).isEqualTo("201608");

        //周
        List<StatisticUser> userList1 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_WEEK, UserServiceImpl.QUERY_LOCATION, date, 3);
        assertThat(userList1.size()).isEqualTo(8);
        assertThat(userList1.get(4).getLocation()).isEqualTo("河北 保定");
        assertThat(userList1.get(6).getLocation()).isEqualTo("北京 东城");
        assertThat(userList1.get(3).getGrade()).isEqualTo("201632");
        userList1 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_WEEK, UserServiceImpl.QUERY_USUALLY, date, 3);
        assertThat(userList1.size()).isEqualTo(3);
        assertThat(userList1.get(0).getCount()).isEqualTo(4);
        assertThat(userList1.get(2).getCount()).isEqualTo(3);
        assertThat(userList1.get(1).getGrade()).isEqualTo("201632");

        //分页
        userList1 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_WEEK, UserServiceImpl.QUERY_LOCATION, date, 3, 2, 5);
        assertThat(userList1.size()).isEqualTo(3);
        assertThat(userList1.get(1).getLocation()).isEqualTo("北京 东城");
        assertThat(userList1.get(1).getGrade()).isEqualTo("201634");
        assertThat(userList1.get(2).getLocation()).isEqualTo("北京 朝阳");
        userList1 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_WEEK, UserServiceImpl.QUERY_USUALLY, date, 3, 1, 2);
        assertThat(userList1.size()).isEqualTo(2);
        assertThat(userList1.get(0).getCount()).isEqualTo(4);
        assertThat(userList1.get(1).getCount()).isEqualTo(3);
        assertThat(userList1.get(1).getGrade()).isEqualTo("201632");

        //日
        List<StatisticUser> userList2 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_DAYS, UserServiceImpl.QUERY_LOCATION, date, 3);
        assertThat(userList2.size()).isEqualTo(9);
        assertThat(userList2.get(0).getLocation()).isEqualTo(" 巴拉那");
        assertThat(userList2.get(1).getCount()).isEqualTo(2);
        assertThat(userList2.get(8).getGrade()).isEqualTo("20160826");
        assertThat(userList2.get(8).getLocation()).isEqualTo("北京 朝阳");
        userList2 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_DAYS, UserServiceImpl.QUERY_USUALLY, date, 3);
        assertThat(userList2.size()).isEqualTo(6);
        assertThat(userList2.get(0).getCount()).isEqualTo(4);
        assertThat(userList2.get(3).getGrade()).isEqualTo("20160811");

        //分页
        userList2 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_DAYS, UserServiceImpl.QUERY_LOCATION, date, 3, 3, 4);
        assertThat(userList2.size()).isEqualTo(1);
        assertThat(userList2.get(0).getLocation()).isEqualTo("北京 朝阳");
        assertThat(userList2.get(0).getCount()).isEqualTo(1);
        assertThat(userList2.get(0).getGrade()).isEqualTo("20160826");
        userList2 = this.userService.getStatisticRegisteredUser(UserServiceImpl.GRADE_DAYS, UserServiceImpl.QUERY_USUALLY, date, 3, 2, 4);
        assertThat(userList2.size()).isEqualTo(2);
        assertThat(userList2.get(1).getCount()).isEqualTo(2);
        assertThat(userList2.get(1).getGrade()).isEqualTo("20160826");
        assertThat(userList2.get(1).getLocation()).isEqualTo("北京 东城");
    }

    @Test
    public void exportStatisticRegisteredUser() {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2016-08-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //months
        String statement = userService.exportXlsByRegisterUser(UserServiceImpl.GRADE_MONTH, UserServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(register_time,'%Y%m') months, location, count(*) as count from users where PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) ) < 4  group by months, location;\"");
        statement = userService.exportXlsByRegisterUser(UserServiceImpl.GRADE_MONTH, UserServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(register_time,'%Y%m') months, count(*) as count from users where PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) ) < 4  group by months;\"");

        //weeks
        statement = userService.exportXlsByRegisterUser(UserServiceImpl.GRADE_WEEK, UserServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(register_time,'%Y%u') weeks, date_sub(date_format(register_time,'%Y%m%d'),INTERVAL WEEKDAY(register_time) DAY) as starttime,date_sub(date_format(register_time,'%Y%m%d'),INTERVAL WEEKDAY(register_time) - 6 DAY) as endtime, location, count(*) as count from users where PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) ) < 4  group by weeks, location;\"");
        statement = userService.exportXlsByRegisterUser(UserServiceImpl.GRADE_WEEK, UserServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(register_time,'%Y%u') weeks, date_sub(date_format(register_time,'%Y%m%d'),INTERVAL WEEKDAY(register_time) DAY) as starttime,date_sub(date_format(register_time,'%Y%m%d'),INTERVAL WEEKDAY(register_time) - 6 DAY) as endtime, count(*) as count from users where PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) ) < 4  group by weeks;\"");

        //days
        statement = userService.exportXlsByRegisterUser(UserServiceImpl.GRADE_DAYS, UserServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(register_time,'%Y%m%d') days, location, count(*) as count from users where PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) ) < 4  group by days, location;\"");
        statement = userService.exportXlsByRegisterUser(UserServiceImpl.GRADE_DAYS, UserServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(register_time,'%Y%m%d') days, count(*) as count from users where PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2016-08-01' , '%Y%m' ) , date_format( register_time, '%Y%m' ) ) < 4  group by days;\"");
    }

    @Test
    public void addUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setRegisterTime(new Date());

        User newUser = this.userService.addUser(user);
        assertThat(newUser).isNotNull();
        assertThat(newUser.getUsername()).isEqualTo("test");
        assertThat(newUser.getPassword()).isEqualTo("password");
    }

    @Test
    public void deleteUser() {
        User user1 = this.userService.findUserByName("zwq");
        assertThat(user1.getPassword()).isEqualTo("/0Xm28BgqhY=");

        int result1 = this.userService.deleteUserByName("zwq");
        assertThat(result1).isEqualTo(1);

        User user2 = this.userService.findUserById(1);
        assertThat(user2.getPassword()).isEqualTo("7oSe14rdAZPgVfE3PQktvA==");

        int result2 = this.userService.deleteUserById(1);
        assertThat(result2).isEqualTo(1);

        User user3 = this.userService.findUserById(3);
        assertThat(user3).isNotNull();

        int result3 = this.userService.deleteUser(user3);
        assertThat(result3).isEqualTo(1);
    }

    @Test
    public void updateUser() {
        User user = this.userService.findUserByName("wxzhang@evistek.com");
        assertThat(user).isNotNull();


        try {
            user.setRegisterTime(DateFormat.getDateInstance().parse("2011-11-11 11:11:11"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPassword("iampassword");
        user.setNickname("iamnickname");
        user.setEmail("wxzhang@evistek.com");
        user.setPhone("18616369689");
        user.setLocation("shanghai");
        user.setFigureUrl("http://www.evistek.com/figure.jpg");
        user.setSex("male");
        user.setSource("evistek");
        user.setPhoneDevice("K550");
        user.setPhoneSystem("Android 5.1");
        user.setVrDevice("HTC VIVE");

        int result = this.userService.updateUser(user);
        assertThat(result).isEqualTo(1);

        User newUser = this.userService.findUserByName("wxzhang@evistek.com");
        assertThat(newUser).isNotNull();
        assertThat(newUser.getPassword()).isEqualTo("iampassword");
        assertThat(newUser.getNickname()).isEqualTo("iamnickname");
        assertThat(newUser.getEmail()).isEqualTo("wxzhang@evistek.com");
        assertThat(newUser.getPhone()).isEqualTo("18616369689");
        assertThat(newUser.getLocation()).isEqualTo("shanghai");
        assertThat(newUser.getFigureUrl()).isEqualTo("http://www.evistek.com/figure.jpg");
        assertThat(newUser.getSex()).isEqualTo("male");
        assertThat(newUser.getSource()).isEqualTo("evistek");
        assertThat(newUser.getPhoneDevice()).isEqualTo("K550");
        assertThat(newUser.getPhoneSystem()).isEqualTo("Android 5.1");
        assertThat(newUser.getVrDevice()).isEqualTo("HTC VIVE");
        assertThat(newUser.getRegisterTime()).isEqualTo("2011-11-11 11:11:11");
    }
}
