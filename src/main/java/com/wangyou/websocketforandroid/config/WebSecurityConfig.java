package com.wangyou.websocketforandroid.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 王游
 * @date 2021/9/3 16:35
 */

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/test","/user/signUp");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .rememberMe()
                .tokenValiditySeconds(3600)
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        User principal = (User) authentication.getPrincipal();
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        httpServletResponse.setStatus(200);
                        ResponseData<User> responseData = ResponseData.<User>builder().code("200").msg("登录成功").data(principal).build();
                        ObjectMapper om = new ObjectMapper();
                        out.write(om.writeValueAsString(responseData));
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        httpServletResponse.setStatus(401);
                        ResponseData.ResponseDataBuilder<User> data = ResponseData.<User>builder().code("401").data(null);
                        if (e instanceof LockedException){
                            data.msg("账户被锁定，登录失败");
                        } else if (e instanceof BadCredentialsException) {
                            data.msg("账户名或者密码输入错误，登录失败");
                        } else if (e instanceof DisabledException) {
                            data.msg("账户被禁用，登录失败");
                        } else if (e instanceof AccountExpiredException) {
                            data.msg("账户已过期，登录失败");
                        } else if (e instanceof CredentialsExpiredException){
                            data.msg("密码已过期，登录失败");
                        }else {
                            data.msg("登录失败");
                        }
                        ObjectMapper om = new ObjectMapper();
                        out.write(om.writeValueAsString(data.build()));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
                        User user = (User) authentication.getPrincipal();
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        try {
                            PrintWriter out = httpServletResponse.getWriter();
                            httpServletResponse.setStatus(200);
                            ResponseData<User> responseData = ResponseData.<User>builder().code("200").msg("退出成功").data(user).build();
                            ObjectMapper om = new ObjectMapper();
                            out.write(om.writeValueAsString(responseData));
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .and()
                .csrf()
                .disable();
    }

}
