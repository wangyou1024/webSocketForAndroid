package com.wangyou.websocketforandroid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyou.websocketforandroid.entity.GroupRelation;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.entity.UserRelation;
import com.wangyou.websocketforandroid.mapper.GroupRelationMapper;
import com.wangyou.websocketforandroid.mapper.UserMapper;
import com.wangyou.websocketforandroid.mapper.UserRelationMapper;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRelationMapper userRelationMapper;

    @Autowired
    GroupRelationMapper groupRelationMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getEnable, 1)
                .and(i -> i.eq(User::getUsername, s)
                        .or()
                        .eq(User::getPhone, s)));
        if (user == null) {
            user = new User();
        }
        return user;
    }


    @Override
    public ResponseData<User> signUp(String username, String password) {
        User oldUser = baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username).or().eq(User::getPhone, username));
        if (oldUser != null) {
            return ResponseData.<User>builder()
                    .code("200")
                    .msg("已注册")
                    .data(null)
                    .build();
        }
        User user = User.builder()
                .username(Instant.now().getEpochSecond() + "")
                .password(passwordEncoder.encode(password))
                .phone(username)
                .realName(username)
                .enable(1)
                .locked(0)
                .build();
        boolean isSuccess = save(user);
        if (isSuccess) {
            return ResponseData.<User>builder()
                    .code("200")
                    .msg("注册成功")
                    .data(null)
                    .build();
        }
        return null;
    }

    @Override
    public List<User> findFriend(String username) {
        User user = baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        List<UserRelation> userRelations = userRelationMapper.selectList(Wrappers.<UserRelation>lambdaQuery()
                .eq(UserRelation::getEnable, 2)
                .and(i -> i.eq(UserRelation::getUidFormer, user.getUid()).or().eq(UserRelation::getUidLatter, user.getUid())));
        Set<Long> ids = new HashSet<>();
        ids.add(-1L);
        for (UserRelation userRelation :
                userRelations) {
            if (!Objects.equals(userRelation.getUidFormer(), user.getUid())) {
                ids.add(userRelation.getUidFormer());
            }
            if (!Objects.equals(userRelation.getUidLatter(), user.getUid())) {
                ids.add(userRelation.getUidLatter());
            }
        }
        return list(Wrappers.<User>lambdaQuery()
                .eq(User::getEnable, 1)
                .and(i -> i.in(User::getUid, ids)));
    }

    @Override
    public User findLeader(Long gid) {
        GroupRelation groupRelation = groupRelationMapper.selectOne(Wrappers.<GroupRelation>lambdaQuery()
                .eq(GroupRelation::getGid, gid)
                .eq(GroupRelation::getEnable, GroupRelation.LEADER));
        return baseMapper.selectById(groupRelation.getUid());
    }

    @Override
    public List<User> findMembers(Long gid) {
        List<GroupRelation> groupRelations = groupRelationMapper.selectList(Wrappers.<GroupRelation>lambdaQuery()
                .eq(GroupRelation::getGid, gid)
                .eq(GroupRelation::getEnable, GroupRelation.AGREE));
        Set<Long> ids = new HashSet<>();
        ids.add(-1L);
        for (GroupRelation groupRelation :
                groupRelations) {
            ids.add(groupRelation.getUid());
        }
        return baseMapper.selectList(Wrappers.<User>lambdaQuery().in(User::getUid, ids).eq(User::getEnable, 1));
    }
}
