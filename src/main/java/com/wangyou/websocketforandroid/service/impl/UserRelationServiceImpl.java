package com.wangyou.websocketforandroid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.entity.UserRelation;
import com.wangyou.websocketforandroid.mapper.UserRelationMapper;
import com.wangyou.websocketforandroid.service.UserRelationService;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements UserRelationService {

    @Autowired
    private UserService userService;

    @Override
    public UserRelation findUserRelation(String username, String uid) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        UserRelation userRelation = getOne(Wrappers.<UserRelation>lambdaQuery()
                .lt(UserRelation::getEnable, 3)
                .and(i -> i.eq(UserRelation::getUidFormer, user.getUid()).or().eq(UserRelation::getUidLatter, user.getUid())));
        return userRelation;
    }
}
