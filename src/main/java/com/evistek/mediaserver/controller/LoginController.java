package com.evistek.mediaserver.controller;

import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.service.IAdminService;
import com.evistek.mediaserver.service.IAuthService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPrivateKey;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */

@RestController
public class LoginController {
    private static final String ERROR_INVALID_USERNAME_PASSWORD = "用户名或密码错误!";
    private static final String ERROR_SESSION_EXCEEDED = "用户已经登陆";

    private final IAdminService adminService;
    private final IAuthService rsaService;
    private final IAuthService desService;
    private  HttpSession session;
    private Admin admin;
    private final Logger logger;
    private final OpLogger opLogger;

    @Autowired
    public LoginController(IAdminService adminService, OpLogger opLogger,
                           @Qualifier("RSAService") IAuthService rsaService,
                           @Qualifier("DESService") IAuthService desService,
                           Admin admin) {

        this.adminService = adminService;
        this.rsaService = rsaService;
        this.desService = desService;
        this.admin = admin;

        this.logger = LoggerFactory.getLogger(getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request) {
        session = request.getSession();
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);

        this.opLogger.info(request, OpLogger.ACTION_LOGIN, "admin login: " + username);

        return modelAndView;
    }

    @RequestMapping(value = "/db/db.html", method = RequestMethod.GET)
    public ModelAndView databasePage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/db/db");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/manager/user.html", method = RequestMethod.GET)
    public ModelAndView userManagerPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/manager/user");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/manager/admin.html", method = RequestMethod.GET)
    public ModelAndView adminManagerPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/manager/admin");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/manager/logger.html", method = RequestMethod.GET)
    public ModelAndView loggerManagerPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/manager/logger");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/upload/image.html", method = RequestMethod.GET)
    public ModelAndView uploadImagePage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/upload/image");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/upload/video.html", method = RequestMethod.GET)
    public ModelAndView uploadVideoPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/upload/video");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/upload/product.html", method = RequestMethod.GET)
    public ModelAndView uploadProductPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/upload/product");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/chart.html", method = RequestMethod.GET)
    public ModelAndView chartsPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/chart");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/download.html", method = RequestMethod.GET)
    public ModelAndView downloadsPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/download");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/device.html", method = RequestMethod.GET)
    public ModelAndView devicesPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/device");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/favorite.html", method = RequestMethod.GET)
    public ModelAndView favoritesPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/favorite");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/playRecord.html", method = RequestMethod.GET)
    public ModelAndView playRecordsPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/playRecord");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/newUser.html", method = RequestMethod.GET)
    public ModelAndView newUsersPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/newUser");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/activeUser.html", method = RequestMethod.GET)
    public ModelAndView activeUsersPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/activeUser");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/stat/heatContent.html", method = RequestMethod.GET)
    public ModelAndView heatContentPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/statistics/heatContent");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/audit/image.html", method = RequestMethod.GET)
    public ModelAndView auditImagePage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/audit/image");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/audit/video.html", method = RequestMethod.GET)
    public ModelAndView auditVideoPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/audit/video");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/audit/product.html", method = RequestMethod.GET)
    public ModelAndView auditProductPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/audit/product");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/modify/image.html", method = RequestMethod.GET)
    public ModelAndView modifyImagePage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/modify/image");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/modify/video.html", method = RequestMethod.GET)
    public ModelAndView modifyVideoPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/modify/video");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/modify/product.html", method = RequestMethod.GET)
    public ModelAndView modifyProductPage(){
        String username = (String) session.getAttribute("username");
        String authority = (String) session.getAttribute("authority");

        ModelAndView modelAndView = new ModelAndView("/modify/product");
        modelAndView.addObject("username", username);
        modelAndView.addObject("authority", authority);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value="error", required = false, defaultValue = "false") boolean err) {
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("admin", admin);
        if (err) {
            modelAndView.addObject("log", ERROR_INVALID_USERNAME_PASSWORD);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/admin/login/session-error", method = RequestMethod.GET)
    public ModelAndView loginSessionError() {
        ModelAndView modelAndView = new ModelAndView("login");

        modelAndView.addObject("admin", admin);
        modelAndView.addObject("log", ERROR_SESSION_EXCEEDED);

        System.out.println("##loginSessionError##");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.GET)
    public ModelAndView registerView(@RequestParam(value="error", required = false, defaultValue = "false") boolean err) {
        ModelAndView modelAndView = new ModelAndView("register");

        modelAndView.addObject("admin", admin);
        if (err) {
            modelAndView.addObject("log", ERROR_INVALID_USERNAME_PASSWORD);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public ModelAndView register(Admin admin, HttpServletRequest request, HttpServletResponse resp) {

        ModelAndView mv = new ModelAndView("success");

        if (this.adminService.findAdminByName(admin.getUsername()) != null) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for username: " + admin.getUsername());
            String err = HttpErrorMessage.build(resp, HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for username: " + admin.getUsername());
            mv.addObject(err);
            return mv;
        }

        if (!Validator.isEmail(admin.getEmail())) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for email: " + admin.getEmail());
            String err = HttpErrorMessage.build(resp, HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for email: " + admin.getEmail());
            mv.addObject(err);
            return mv;
        }

        RSAPrivateKey rsaPrivateKey = this.rsaService.getPrivateKey();
        if (rsaPrivateKey != null) {
            try {
                String clientPasswordString = new String(
                        this.rsaService.decryptFromBase64String(admin.getPassword(), rsaPrivateKey), "UTF-8");
                String serverPasswordString = this.desService.encryptToBase64String(clientPasswordString,
                        this.desService.getKeyForPassword());

                admin.setPassword(serverPasswordString);
                admin.setEnabled(true);
                Admin newAdmin = this.adminService.addAdmin(admin);
                if (newAdmin != null) {
                    mv.addObject(newAdmin);
                    this.opLogger.info(request, OpLogger.ACTION_REGISTER_ADMIN,
                            "register admin with name: " + newAdmin.getUsername());
                    return mv;
                } else {
                    logger.error("status:" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " SC INTERNAL SERVER ERROR " +
                            "message: " + "fail to add admin " + admin.getUsername() + " into db");
                    String err = HttpErrorMessage.build(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "fail to add admin " + admin.getUsername() + " into db");
                    mv.addObject(err);
                    return mv;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error("status:" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " SC INTERNAL SERVER ERROR " +
                        "message: " + "UnsupportedEncodingException for UTF-8");
                String err = HttpErrorMessage.build(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "UnsupportedEncodingException for UTF-8");
                mv.addObject(err);
                return mv;
            }
        } else {
            logger.error("status:" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " SC INTERNAL SERVER ERROR " +
                    "message: " + "RSA private key is null");
            String err = HttpErrorMessage.build(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "RSA private key is null");
            mv.addObject(err);
            return mv;
        }
    }
}
