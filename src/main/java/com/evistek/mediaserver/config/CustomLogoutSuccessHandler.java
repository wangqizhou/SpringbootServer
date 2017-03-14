package com.evistek.mediaserver.config;

import com.evistek.mediaserver.utils.OpLogger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/2/27.
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final OpLogger opLogger;

    public CustomLogoutSuccessHandler(OpLogger opLogger) {
        this.opLogger = opLogger;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        //no username in session of this request
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        this.opLogger.info(userDetails.getUsername(), OpLogger.ACTION_LOGOUT,
                "admin logout: " + userDetails.getUsername());
        response.sendRedirect(request.getContextPath() + "/admin/login");
    }
}
