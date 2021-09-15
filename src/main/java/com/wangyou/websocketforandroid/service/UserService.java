package com.wangyou.websocketforandroid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", s).or().eq("phone", s));
        if (user == null){
            user = new User();
        }
        return user;
    }


}
