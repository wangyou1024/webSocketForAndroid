package com.wangyou.websocketforandroid.controller;

import com.wangyou.websocketforandroid.entity.Chat;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @GetMapping("/findSessionList")
    public ResponseData<List<Chat>> findSessionList(Principal principal) {
        return ResponseData.<List<Chat>>builder()
                .code("200")
                .msg("获取成功")
                .data(chatService.findSessionList(principal.getName()))
                .build();
    }

    @GetMapping("/findChatList")
    public ResponseData<List<Chat>> findChatList(Principal principal, @RequestParam("type") Integer type, @RequestParam("id") Long id){
        return ResponseData.<List<Chat>>builder()
                .code("200")
                .msg("获取成功")
                .data(chatService.findChatList(principal.getName(), type, id))
                .build();
    }
}
