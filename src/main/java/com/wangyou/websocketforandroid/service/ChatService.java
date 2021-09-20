package com.wangyou.websocketforandroid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyou.websocketforandroid.entity.Chat;

import java.util.List;

public interface ChatService extends IService<Chat> {

    public List<Chat> findSessionList(String username);

    public List<Chat> findChatList(String username, Integer type, Long id);

    public Chat handleChat(String username, Chat chat);
}
