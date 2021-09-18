package com.wangyou.websocketforandroid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyou.websocketforandroid.entity.Group;
import com.wangyou.websocketforandroid.entity.GroupRelation;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.mapper.GroupMapper;
import com.wangyou.websocketforandroid.mapper.GroupRelationMapper;
import com.wangyou.websocketforandroid.mapper.UserMapper;
import com.wangyou.websocketforandroid.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    GroupRelationMapper groupRelationMapper;

    @Override
    public ResponseData<Group> create(String username, Group group) {
        // 名称是否重复
        Group oldGroup = baseMapper.selectOne(Wrappers.<Group>lambdaQuery().eq(Group::getGroupName, group.getGroupName()));
        if (oldGroup != null){
            return ResponseData.<Group>builder()
                    .code("500")
                    .msg("群聊名称重复")
                    .data(new Group())
                    .build();
        }
        // 登录者作为群主
        User leader = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        group.setGroupNum(Instant.now().getEpochSecond() + "");
        group.setUpdateTime((int) Instant.now().getEpochSecond());
        group.setEnable(1);
        baseMapper.insert(group);
        // 保存关系
        GroupRelation groupRelation = GroupRelation.builder()
                .enable(GroupRelation.LEADER)
                .gid(group.getGid())
                .uid(leader.getUid())
                .updateTime((int) Instant.now().getEpochSecond())
                .readTime((int) Instant.now().getEpochSecond())
                .build();
        groupRelationMapper.insert(groupRelation);
        return ResponseData.<Group>builder()
                .code("200")
                .msg("添加成功")
                .data(group)
                .build();
    }

    @Override
    public List<Group> findGroupList(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        // 查询已经同意或者自建的群聊
        List<GroupRelation> groupRelations = groupRelationMapper.selectList(Wrappers.<GroupRelation>lambdaQuery()
                .eq(GroupRelation::getUid, user.getUid())
                .and(i -> i.eq(GroupRelation::getEnable, GroupRelation.AGREE)
                        .or()
                        .eq(GroupRelation::getEnable, GroupRelation.LEADER)));
        Set<Long> gids = new HashSet<>();
        // 避免空值导致的sql异常
        gids.add(-1L);
        for (GroupRelation groupRelation :
                groupRelations) {
            gids.add(groupRelation.getGid());
        }
        return baseMapper.selectList(Wrappers.<Group>lambdaQuery().eq(Group::getEnable, 1).in(Group::getGroupName, gids));
    }
}
