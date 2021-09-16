package com.wangyou.websocketforandroid.controller;

import com.wangyou.websocketforandroid.entity.UserRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * @author 王游
 * @date 2021/9/3 9:28
 */

@Controller
public class GreetingController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    public String greeting(String message){
        simpMessagingTemplate.convertAndSend("/topic/greetings", message);
        return message;
    }

    @MessageMapping("/chat")
    public void chat(Principal principal, String message){
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/chat", new String[]{message});
    }


    @RequestMapping("/test")
    public @ResponseBody String test(){
        return "test";
    }
}
