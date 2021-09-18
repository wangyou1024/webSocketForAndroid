package com.wangyou.websocketforandroid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyou.websocketforandroid.entity.UserRelation;

public interface UserRelationService extends IService<UserRelation>  {

    UserRelation findUserRelation(String username, Long uid);

    UserRelation handleUserRelation(UserRelation userRelation);
}
