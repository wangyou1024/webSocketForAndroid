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
                .lt(UserRelation::getEnable, UserRelation.DEPENDENCE)
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
                .lt(UserRelation::getEnable, UserRelation.DEPENDENCE)
                .and(i -> i.nested(t -> t.eq(UserRelation::getUidFormer, userRelation.getUidFormer()).eq(UserRelation::getUidLatter, userRelation.getUidLatter()))
                        .or()
                        .nested(t -> t.eq(UserRelation::getUidFormer, userRelation.getUidLatter()).eq(UserRelation::getUidLatter, userRelation.getUidFormer()))
                ));
        int result = 0;
        if (oldRelation == null) {
            // 新申请
            userRelation.setUrid(null);
            userRelation.setUpdateTime((int) (Instant.now().getEpochSecond()));
            userRelation.setReadTime((int) (Instant.now().getEpochSecond()));
            userRelation.setEnable(UserRelation.NO_DEAL);
            result = baseMapper.insert(userRelation);
            oldRelation = userRelation;
        } else {
            // 已有申请
            oldRelation.setUpdateTime((int) (Instant.now().getEpochSecond()));
            oldRelation.setEnable(userRelation.getEnable());
            result = baseMapper.updateById(oldRelation);
        }
        // 查看是否存在从关系
        UserRelation dependenceRelation = getOne(Wrappers.<UserRelation>lambdaQuery().eq(UserRelation::getEnable, UserRelation.DEPENDENCE)
                .eq(UserRelation::getUidFormer, oldRelation.getUidLatter()).eq(UserRelation::getUidLatter, oldRelation.getUidFormer()));
        if (dependenceRelation != null){
            dependenceRelation.setUpdateTime(oldRelation.getUpdateTime());
            baseMapper.updateById(dependenceRelation);
        } else {
          dependenceRelation = UserRelation.builder()
                  .enable(UserRelation.DEPENDENCE)
                  .readTime(oldRelation.getUpdateTime())
                  .updateTime(oldRelation.getUpdateTime())
                  .uidFormer(oldRelation.getUidLatter())
                  .uidLatter(oldRelation.getUidFormer())
                  .build();
          baseMapper.insert(dependenceRelation);
        }
        if (result == 0){
            // 错误
            oldRelation.setEnable(500);
        }
        return oldRelation;
    }
}
