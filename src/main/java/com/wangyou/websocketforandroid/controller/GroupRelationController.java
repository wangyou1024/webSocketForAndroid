package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.GroupRelation;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.service.GroupRelationService;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/groupRelation")
public class GroupRelationController {

    @Autowired
    GroupRelationService groupRelationService;

    @Autowired
    UserService userService;

    @GetMapping("/findRelation")
    public ResponseData<GroupRelation> findRelation(Principal principal, Long gid){
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, principal.getName()));
        return ResponseData.<GroupRelation>builder()
                .code("200")
                .msg("获取成功")
                .data(groupRelationService.getOne(Wrappers.<GroupRelation>lambdaQuery()
                        .eq(GroupRelation::getUid, user.getUid())
                        .eq(GroupRelation::getGid, gid)))
                .build();
    }

    @GetMapping("/findRelationList")
    public ResponseData<List<GroupRelation>> findRelationList(Principal principal){
        return ResponseData.<List<GroupRelation>>builder()
                .code("200")
                .msg("获取成功")
                .data(groupRelationService.findGroupRelationList(principal.getName()))
                .build();
    }
}
