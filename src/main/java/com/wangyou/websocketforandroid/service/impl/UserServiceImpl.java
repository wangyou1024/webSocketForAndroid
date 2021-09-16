package com.wangyou.websocketforandroid.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.mapper.UserMapper;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, s).or().eq(User::getPhone, s));
        if (user == null) {
            user = new User();
        }
        return user;
    }


    @Override
    public ResponseData<User> signUp(String username, String password) {
        User oldUser = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username).or().eq(User::getPhone, username));
        if (oldUser != null){
            return ResponseData.<User>builder()
                    .code("200")
                    .msg("已注册")
                    .data(null)
                    .build();
        }
        User user = User.builder()
                .username(Instant.now().getEpochSecond() + "")
                .password(passwordEncoder.encode(password))
                .phone(username)
                .enable(1)
                .locked(0)
                .build();
        boolean isSuccess = save(user);
        if (isSuccess){
            return ResponseData.<User>builder()
                    .code("200")
                    .msg("注册成功")
                    .data(null)
                    .build();
        }
        return null;
    }
}
