package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.Chat;
import com.wangyou.websocketforandroid.entity.GroupRelation;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.entity.UserRelation;
import com.wangyou.websocketforandroid.service.ChatService;
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
    ChatService chatService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    public String greeting(String message) {
        simpMessagingTemplate.convertAndSend("/topic/greetings", message);
        return message;
    }

    @MessageMapping("/chat")
    public void chat(Principal principal, @RequestBody  Chat chat) {
        chat = chatService.handleChat(principal.getName(), chat);
        Set<String> noticedUserName = new HashSet<>();
        noticedUserName.add(principal.getName());
        if (chat.getEnable() == Chat.PRIVATE_CHAT) {
            // 私聊
            User user = userService.getById(chat.getRecipient());
            noticedUserName.add(user.getUsername());
        } else {
            // 群聊
            User leader = userService.findLeader(chat.getGid());
            noticedUserName.add(leader.getUsername());
            List<User> members = userService.findMembers(chat.getGid());
            for (User member: members) {
                noticedUserName.add(member.getUsername());
            }
        }
        // 向当前的订阅者发信息
        for (String username :
                noticedUserName) {
            simpMessagingTemplate.convertAndSendToUser(username, "/queue/chat", chat);
        }
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
        // 订阅名单
        Set<String> noticedUserName = new HashSet<>();
        // 所有的申请总是要发给群主
        noticedUserName.add(userService.findLeader(groupRelation.getGid()).getUsername());
        if (groupRelation.getEnable() == GroupRelation.DISMISS){
            List<User> members = userService.findMembers(groupRelation.getGid());
            for (User user :
                    members) {
                noticedUserName.add(user.getUsername());
            }
            groupRelation = groupRelationService.handleDismiss(groupRelation.getGid());
            groupRelation.setEnable(GroupRelation.DISMISS);
        } else {
            // 申请、拒绝、同意
            groupRelation = groupRelationService.handleGroupRelation(principal.getName(), groupRelation);
            noticedUserName.add(userService.getById(groupRelation.getUid()).getUsername());
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
