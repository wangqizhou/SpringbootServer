package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.entity.HeatContentSearchInfo;
import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.entity.User;
import com.evistek.mediaserver.model.AdminModel;
import com.evistek.mediaserver.model.UserModel;
import com.evistek.mediaserver.service.*;
import com.evistek.mediaserver.service.impl.TotalPlayRecordServiceImpl;
import com.evistek.mediaserver.service.impl.UserServiceImpl;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import com.evistek.mediaserver.utils.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    private final IUserService userService;
    private final ILoggerService loggerService;
    private final IAdminService adminService;
    private final IDatabaseService databaseService;
    private final ITotalPlayRecordService totalPlayRecordService;
    private final UrlBuilder urlBuilder;
    private final Logger logger;
    private final OpLogger opLogger;

    public UserController(OpLogger opLogger, IUserService userService, IAdminService adminService, ILoggerService loggerService
            , IDatabaseService databaseService, ITotalPlayRecordService totalPlayRecordService, UrlBuilder urlBuilder) {
        this.userService = userService;
        this.adminService = adminService;
        this.loggerService = loggerService;
        this.databaseService = databaseService;
        this.totalPlayRecordService = totalPlayRecordService;
        this.urlBuilder = urlBuilder;
        this.logger = LoggerFactory.getLogger(getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getUsers(
	        @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request) {

        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;
        List<User> users = userService.findUsers(pageNumber, pageSize);
        int total = this.userService.findUserNumber();

        List<UserModel> userModels = new ArrayList<>();
        for(User user: users) {
            UserModel userModel = new UserModel();
            userModel.setId(user.getId());
            userModel.setUsername(user.getUsername());
            userModel.setPhone(user.getPhone());
            userModel.setEmail(user.getEmail());
            userModel.setNickname(user.getNickname());
            userModel.setRegisterTime(user.getRegisterTime());
            userModel.setLocation(user.getLocation());
            userModel.setSex(user.getSex());
            userModel.setFigureUrl(user.getFigureUrl());
            userModel.setSource(user.getSource());
            userModel.setPhoneDevice(user.getPhoneDevice());
            userModel.setPhoneSystem(user.getPhoneSystem());
            userModel.setVrDevice(user.getVrDevice());

            userModels.add(userModel);
        }

        this.opLogger.info(request, OpLogger.ACTION_QUERY_USERS, "query users information");

        return TablePageHelper.toJSONString(total, userModels);
	}

    @RequestMapping(value = "/loggers", method = RequestMethod.GET)
    public String getLoggers(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request) {

        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;
        List<com.evistek.mediaserver.entity.Logger> loggers = loggerService.findLoggers(pageNumber, pageSize);
        int total = this.loggerService.findLoggersNumber();
        this.opLogger.info(request, OpLogger.ACTION_QUERY_LOGGERS, "query loggers information");

        return TablePageHelper.toJSONString(total, loggers);
    }

	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public String updateUser(@RequestBody User user, HttpServletRequest request){
	    String msg = "success";
	    if (this.userService.updateUser(user) != 1) {
            logger.error("updateUser failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_USER,
                "update user information of " + user.getUsername());

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public List<AdminModel> getAdmins(HttpServletRequest request){
        List<Admin> admins = this.adminService.findAdmins();
        if (admins == null || admins.isEmpty()) {
            logger.error("getAdmins failed.");
        }

        List<AdminModel> adminModels = new ArrayList<>();
        for (Admin adminEntity: admins) {
            AdminModel adminModel = new AdminModel();
            adminModel.setId(adminEntity.getId());
            adminModel.setUsername(adminEntity.getUsername());
            adminModel.setEmail(adminEntity.getEmail());
            adminModel.setEnabled(adminEntity.isEnabled());
            adminModel.setAuthority(adminEntity.getAuthority());

            adminModels.add(adminModel);
        }

        this.opLogger.info(request, OpLogger.ACTION_QUERY_ADMINS, "get admins information");

        return adminModels;
    }

    @RequestMapping(value = "/admins", method = RequestMethod.PUT)
    public String updateAdmin(@RequestBody Admin admin, HttpServletRequest request){
        String msg = "success";
        if (this.adminService.updateAdmin(admin) != 1) {
            logger.error("updateAdmin failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_ADMIN,
                "update admin information of " + admin.getUsername());

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.GET)
    public String getNewUser(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "range", required = false) String range,
            @RequestParam(value = "date", required = false) String dateString,
            @RequestParam(value = "granularity", required = false) String granularity,
            @RequestParam(value = "type", required = false) String type,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;

        int query_grade;
        switch (granularity) {
            case "日":
                query_grade = UserServiceImpl.GRADE_DAYS;
                break;
            case "周":
                query_grade = UserServiceImpl.GRADE_WEEK;
                break;
            case "月":
                query_grade = UserServiceImpl.GRADE_MONTH;
                break;
            default:
                query_grade = UserServiceImpl.GRADE_DAYS;
                break;
        }

        int query_type;
        if (type.contains("时间")) {
            query_type = UserServiceImpl.QUERY_USUALLY;
        } else {
            query_type = UserServiceImpl.QUERY_LOCATION;
        }

        int num = Integer.parseInt(range.substring(0, range.indexOf("个")));
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<StatisticUser> statisticUsers = this.userService.getStatisticRegisteredUser(
                                                query_grade, query_type, date, num, pageNumber, pageSize);
        int total = this.userService.getStatisticRegisteredUser(query_grade, query_type, date, num).size();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_NEW_USER,
                "query new user information");

        return TablePageHelper.toJSONString(total, statisticUsers);
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.POST)
    public String exportNewUser(@RequestBody HeatContentSearchInfo info){

        int query_type;
        if (info.type.contains("时间")) {
            query_type = UserServiceImpl.QUERY_USUALLY;
        } else {
            query_type = UserServiceImpl.QUERY_LOCATION;
        }

        int query_grade;
        switch (info.granularity) {
            case "日":
                query_grade = UserServiceImpl.GRADE_DAYS;
                break;
            case "周":
                query_grade = UserServiceImpl.GRADE_WEEK;
                break;
            case "月":
                query_grade = UserServiceImpl.GRADE_MONTH;
                break;
            default:
                query_grade = UserServiceImpl.GRADE_DAYS;
                break;
        }

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(info.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int num = Integer.parseInt(info.range.substring(0, info.range.indexOf("个")));
        String statement = this.userService.exportXlsByRegisterUser(query_grade, query_type, date, num);

        String filename = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(new Date()) + ".xls";
        File exportFile = new File(this.databaseService.getExportPath() + filename);
        if (exportFile.exists()) {
            exportFile.delete();
        }
        int result = databaseService.exportXls(statement, filename);
        if (result != -1) {
            return JSON.toJSONString(this.urlBuilder.buildUrl(filename));
        } else {
            return JSON.toJSONString("err");
        }
    }

    @RequestMapping(value = "/users/active", method = RequestMethod.GET)
    public String getActiveUser(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "range", required = false) String range,
            @RequestParam(value = "date", required = false) String dateString,
            @RequestParam(value = "granularity", required = false) String granularity,
            @RequestParam(value = "type", required = false) String type,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;

        int query_type;
        if (type.contains("时间")) {
            query_type = TotalPlayRecordServiceImpl.QUERY_USUALLY;
        } else {
            query_type = TotalPlayRecordServiceImpl.QUERY_LOCATION;
        }

        int query_grade;
        switch (granularity) {
            case "日":
                query_grade = TotalPlayRecordServiceImpl.GRADE_DAYS;
                break;
            case "周":
                query_grade = TotalPlayRecordServiceImpl.GRADE_WEEK;
                break;
            case "月":
                query_grade = TotalPlayRecordServiceImpl.GRADE_MONTH;
                break;
            default:
                query_grade = TotalPlayRecordServiceImpl.GRADE_DAYS;
                break;
        }

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int num = Integer.parseInt(range.substring(0, range.indexOf("个")));

        List<StatisticUser> statisticUsers = this.totalPlayRecordService.getStatisticActiveUser(
                query_grade, query_type, date, num, pageNumber, pageSize);
        int total = this.totalPlayRecordService.getStatisticActiveUser(query_grade, query_type, date, num).size();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_ACTIVE_USER,
                "query active user information");

        return TablePageHelper.toJSONString(total, statisticUsers);
    }

    @RequestMapping(value = "/users/active", method = RequestMethod.POST)
    public String exportActiveUser(@RequestBody HeatContentSearchInfo info){

        int query_type;
        if (info.type.contains("时间")) {
            query_type = TotalPlayRecordServiceImpl.QUERY_USUALLY;
        } else {
            query_type = TotalPlayRecordServiceImpl.QUERY_LOCATION;
        }

        int query_grade;
        switch (info.granularity) {
            case "日":
                query_grade = TotalPlayRecordServiceImpl.GRADE_DAYS;
                break;
            case "周":
                query_grade = TotalPlayRecordServiceImpl.GRADE_WEEK;
                break;
            case "月":
                query_grade = TotalPlayRecordServiceImpl.GRADE_MONTH;
                break;
            default:
                query_grade = TotalPlayRecordServiceImpl.GRADE_DAYS;
                break;
        }

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(info.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int num = Integer.parseInt(info.range.substring(0, info.range.indexOf("个")));
        String statement = totalPlayRecordService.exportXLSByActiveUser(query_grade, query_type, date, num);

        String filename = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(new Date()) + ".xls";
        File exportFile = new File(databaseService.getExportPath() + filename);
        if (exportFile.exists()) {
            exportFile.delete();
        }
        int result = databaseService.exportXls(statement, filename);
        if (result != -1) {
            return JSON.toJSONString(this.urlBuilder.buildUrl(filename));
        } else {
            return JSON.toJSONString("err");
        }
    }
}
