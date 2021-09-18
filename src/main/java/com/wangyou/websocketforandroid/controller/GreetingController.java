package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.GroupRelation;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.entity.UserRelation;
import com.wangyou.websocketforandroid.service.GroupRelationService;
import com.wangyou.websocketforandroid.service.UserRelationService;
import com.wangyou.websocketforandroid.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 王游
 * @date 2021/9/3 9:28
 */

@Slf4j
@Controller
public class GreetingController {

    @Autowired
    UserService userService;

    @Autowired
    UserRelationService userRelationService;

    @Autowired
    GroupRelationService groupRelationService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    public String greeting(String message) {
        simpMessagingTemplate.convertAndSend("/topic/greetings", message);
        return message;
    }

    @MessageMapping("/chat")
    public void chat(Principal principal, String message) {
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/chat", new String[]{message});
    }

    @MessageMapping("/friendApplication")
    public void friendApplication(Principal principal, @RequestBody UserRelation userRelation) {
        log.info(principal.getName() + " -> " + userRelation.toString());
        User former = userService.getById(userRelation.getUidFormer());
        User latter = userService.getById(userRelation.getUidLatter());
        UserRelation newRelation = userRelationService.handleUserRelation(userRelation);
        simpMessagingTemplate.convertAndSendToUser(former.getUsername(), "/queue/friendApplication", newRelation);
        simpMessagingTemplate.convertAndSendToUser(latter.getUsername(), "/queue/friendApplication", newRelation);
    }

    @MessageMapping("/groupApplication")
    public void groupApplication(Principal principal, @RequestBody GroupRelation groupRelation) {
        log.info(principal.getName() + " -> " + groupRelation.toString());
        Set<String> noticedUserName = new HashSet<>();
        groupRelation = groupRelationService.handleGroupRelation(principal.getName(), groupRelation);
        noticedUserName.add(userService.findLeader(groupRelation.getGid()).getUsername());
        if (groupRelation.getEnable() != GroupRelation.DISMISS) {
            // 申请、拒绝、同意
            noticedUserName.add(userService.getById(groupRelation.getUid()).getUsername());
        } else {
            groupRelationService.handleDismiss(groupRelation.getGid());
            List<User> members = userService.findMembers(groupRelation.getGid());
            for (User user :
                    members) {
                noticedUserName.add(user.getUsername());
            }
        }
        // 向当前的订阅者发信息
        for (String username :
                noticedUserName) {
            simpMessagingTemplate.convertAndSendToUser(username, "/queue/groupApplication", groupRelation);
        }
    }

    @RequestMapping("/test")
    public @ResponseBody
    String test() {
        return "test";
    }
}
