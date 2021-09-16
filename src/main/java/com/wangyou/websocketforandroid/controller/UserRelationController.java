package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.entity.UserRelation;
import com.wangyou.websocketforandroid.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/userRelation")
public class UserRelationController {

    @Autowired
    private UserRelationService userRelationService;

    public ResponseData<UserRelation> findUserRelation(Principal principal, @RequestParam("uid") String uid){
        User user = (User) principal;
        return ResponseData.<UserRelation>builder()
                .code("200")
                .msg("获取成功")
                .data(userRelationService.getOne(Wrappers.<UserRelation>lambdaQuery()
                        .lt(UserRelation::getEnable, 3)
                        .and(i -> i.eq(UserRelation::getUidFormer, user.getUid())
                                .or()
                                .eq(UserRelation::getUidLatter, user.getUid()))))
                .build();
    }
}
