package com.wangyou.websocketforandroid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService, IService<User> {

    public ResponseData<User> signUp(String username, String password);

    public List<User> findFriend(String username);

    public User findLeader(Long gid);

    public List<User> findMembers(Long gid);
}
