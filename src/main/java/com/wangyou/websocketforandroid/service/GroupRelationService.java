package com.wangyou.websocketforandroid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyou.websocketforandroid.entity.GroupRelation;

import java.util.List;

public interface GroupRelationService extends IService<GroupRelation> {

    public List<GroupRelation> findGroupRelationList(String username);

    public GroupRelation handleGroupRelation(String username, GroupRelation groupRelation);

    public GroupRelation handleDismiss(Long gid);
}
