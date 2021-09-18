package com.wangyou.websocketforandroid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyou.websocketforandroid.entity.Group;
import com.wangyou.websocketforandroid.entity.ResponseData;

import java.util.List;

public interface GroupService extends IService<Group> {

    public ResponseData<Group> create(String username, Group group);

    public List<Group> findGroupList(String username);
}
