package com.wangyou.websocketforandroid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyou.websocketforandroid.entity.Group;
import com.wangyou.websocketforandroid.entity.GroupRelation;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.mapper.GroupMapper;
import com.wangyou.websocketforandroid.mapper.GroupRelationMapper;
import com.wangyou.websocketforandroid.mapper.UserMapper;
import com.wangyou.websocketforandroid.service.GroupRelationService;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupRelationServiceImpl extends ServiceImpl<GroupRelationMapper, GroupRelation> implements GroupRelationService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    GroupMapper groupMapper;

    @Override
    public List<GroupRelation> findGroupRelationList(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        // 查询加入的所有关系
        List<GroupRelation> groupRelations = baseMapper.selectList(Wrappers.<GroupRelation>lambdaQuery()
                .eq(GroupRelation::getUid, user.getUid())
                .eq(GroupRelation::getEnable, GroupRelation.AGREE));
        // 查询自建的群聊
        List<GroupRelation> leaderGroupRelations = baseMapper.selectList(Wrappers.<GroupRelation>lambdaQuery()
                .eq(GroupRelation::getUid, user.getUid())
                .eq(GroupRelation::getEnable, GroupRelation.LEADER));
        Set<Long> gids = new HashSet<Long>();
        for (GroupRelation groupRelation :
                leaderGroupRelations) {
            gids.add(groupRelation.getGid());
        }
        // 查询自己接收到的申请
        List<GroupRelation> groupRelationsApplication = baseMapper.selectList(Wrappers.<GroupRelation>lambdaQuery()
                .in(GroupRelation::getGid, gids)
                .ne(GroupRelation::getEnable, GroupRelation.LEADER)
                .ne(GroupRelation::getEnable, GroupRelation.DISMISS));
        groupRelations.addAll(groupRelationsApplication);
        return groupRelations;
    }

    @Override
    public GroupRelation handleGroupRelation(String username, GroupRelation groupRelation) {
        if (groupRelation.getUid() == null) {
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
            groupRelation.setUid(user.getUid());
        }
        GroupRelation oldRelation = baseMapper.selectOne(Wrappers.<GroupRelation>lambdaQuery()
                .eq(GroupRelation::getUid, groupRelation.getUid())
                .eq(GroupRelation::getGid, groupRelation.getGid()));
        if (oldRelation == null) {
            oldRelation = groupRelation;
            oldRelation.setReadTime((int) Instant.now().getEpochSecond());
            oldRelation.setUpdateTime((int) Instant.now().getEpochSecond());
            baseMapper.insert(oldRelation);
        } else {
            oldRelation.setEnable(groupRelation.getEnable());
            oldRelation.setUpdateTime((int) Instant.now().getEpochSecond());
            baseMapper.updateById(oldRelation);
        }
        return groupRelation;
    }

    @Override
    public void handleDismiss(Long gid) {
        GroupRelation groupRelation = GroupRelation.builder().enable(GroupRelation.DISMISS).build();
        baseMapper.update(groupRelation, Wrappers.<GroupRelation>lambdaQuery().eq(GroupRelation::getGid, gid));
        Group group = Group.builder().enable(0).gid(gid).build();
        groupMapper.updateById(group);
    }


}
