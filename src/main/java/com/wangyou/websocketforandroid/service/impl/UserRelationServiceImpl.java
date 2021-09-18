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

import java.time.Instant;

@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements UserRelationService {

    @Autowired
    private UserService userService;

    @Override
    public UserRelation findUserRelation(String username, Long uid) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        // enable为4代表从关系
        UserRelation userRelation = getOne(Wrappers.<UserRelation>lambdaQuery()
                .lt(UserRelation::getEnable, 4)
                .and(i -> i.nested(t -> t.eq(UserRelation::getUidFormer, user.getUid()).eq(UserRelation::getUidLatter, uid))
                        .or()
                        .nested(t -> t.eq(UserRelation::getUidLatter, user.getUid()).eq(UserRelation::getUidFormer, uid))
                ));
        if (userRelation == null) {
            userRelation = new UserRelation();
        }
        return userRelation;
    }

    @Override
    public UserRelation handleUserRelation(UserRelation userRelation) {
        UserRelation oldRelation = getOne(Wrappers.<UserRelation>lambdaQuery()
                .lt(UserRelation::getEnable, 4)
                .and(i -> i.nested(t -> t.eq(UserRelation::getUidFormer, userRelation.getUidFormer()).eq(UserRelation::getUidLatter, userRelation.getUidLatter()))
                        .or()
                        .nested(t -> t.eq(UserRelation::getUidFormer, userRelation.getUidLatter()).eq(UserRelation::getUidLatter, userRelation.getUidFormer()))
                ));
        int result = 0;
        if (oldRelation == null) {
            // 新申请
            userRelation.setUrid(null);
            userRelation.setUpdateTime((int) (Instant.now().getEpochSecond() / 1000));
            userRelation.setReadTime((int) (Instant.now().getEpochSecond() / 1000));
            userRelation.setEnable(0);
            result = baseMapper.insert(userRelation);
            oldRelation = userRelation;
        } else {
            // 已有申请
            oldRelation.setUpdateTime((int) (Instant.now().getEpochSecond() / 1000));
            oldRelation.setEnable(userRelation.getEnable());
            result = baseMapper.updateById(oldRelation);
        }
        if (result == 0){
            // 错误
            oldRelation.setEnable(500);
        }
        return oldRelation;
    }
}
