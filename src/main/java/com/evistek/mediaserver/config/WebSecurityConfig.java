package com.evistek.mediaserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomAuthenticationProvider customAuthenticationProvider;
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    public WebSecurityConfig(CustomAuthenticationProvider customAuthenticationProvider,
                             CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                             CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter off
        http
            .csrf().disable();

        http
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                //.antMatchers("/**").authenticated()
//                .antMatchers("/users", "/admins").hasRole("USER_MANAGER")
//                .antMatchers("/upload").hasRole("UPLOAD")
//                .antMatchers("/audit").hasRole("AUDIT")
//                .antMatchers("/modify").hasRole("MODIFY")
//                .antMatchers("/statistics").hasRole("STAT")
//                .antMatchers("/db").hasRole("DBA")
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .httpBasic()
                .and()
            .formLogin()
                .loginPage("/admin/login")
                .permitAll()
                //.defaultSuccessUrl("/")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/admin/login?error=true")
                .and()
            .rememberMe()
                .key("springSecurityRememberMeCookies")
                //.rememberMeCookieName("springSecurityRememberMeCookies")
                .and()
            .logout()
                .logoutUrl("/admin/logout")
                //.logoutSuccessUrl("/admin/login")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("springSecurityRememberMeCookies")
                .permitAll();

        http
            .sessionManagement()
                //.sessionAuthenticationErrorUrl("/admin/login/session-error")
                .maximumSessions(1)
                .expiredUrl("/admin/login/session-error");
//
//        http
//            .exceptionHandling()
//                .accessDeniedPage("/admin/accessDenied");
        // @formatter on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // @formatter off
        auth
            .authenticationProvider(customAuthenticationProvider);
            //.userDetailsService(userDetailsService);

        // @formatter on
    }
}
