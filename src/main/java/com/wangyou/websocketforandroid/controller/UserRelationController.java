package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.entity.UserRelation;
import com.wangyou.websocketforandroid.service.UserRelationService;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/userRelation")
public class UserRelationController {

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserService userService;

    @RequestMapping("/findUserRelation")
    public ResponseData<UserRelation> findUserRelation(Principal principal, @RequestParam("uid") Long uid) {
        return ResponseData.<UserRelation>builder()
                .code("200")
                .msg("获取成功")
                .data(userRelationService.findUserRelation(principal.getName(), uid))
                .build();
    }

    @RequestMapping("findUserRelationList")
    public ResponseData<List<UserRelation>> findUserRelationList(Principal principal){
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, principal.getName()));
        return ResponseData.<List<UserRelation>>builder()
                .code("200")
                .msg("获取成功")
                .data(userRelationService.list(Wrappers.<UserRelation>lambdaQuery()
                        .lt(UserRelation::getEnable, UserRelation.DEPENDENCE)
                        .and(i -> i.eq(UserRelation::getUidFormer, user.getUid()).or().eq(UserRelation::getUidLatter, user.getUid()))))
                .build();
    }
}
