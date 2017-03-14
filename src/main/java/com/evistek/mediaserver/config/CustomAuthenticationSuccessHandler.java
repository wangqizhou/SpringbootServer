package com.evistek.mediaserver.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/13.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession httpSession = request.getSession();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String authorityString = "";
        for (GrantedAuthority authority: authorities) {
            authorityString += (authority.getAuthority() + " ");
        }

        httpSession.setAttribute("username", userDetails.getUsername());
        httpSession.setAttribute("authority", authorityString.trim());

        response.sendRedirect(request.getContextPath() + "/");
    }
}
