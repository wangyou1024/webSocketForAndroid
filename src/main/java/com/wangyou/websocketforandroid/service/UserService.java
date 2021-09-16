package com.wangyou.websocketforandroid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, IService<User> {

    public ResponseData<User> signUp(String username, String password);
}
