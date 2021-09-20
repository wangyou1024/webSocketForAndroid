package com.wangyou.websocketforandroid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyou.websocketforandroid.entity.Chat;
import com.wangyou.websocketforandroid.entity.Group;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.entity.UserRelation;
import com.wangyou.websocketforandroid.mapper.ChatMapper;
import com.wangyou.websocketforandroid.mapper.UserMapper;
import com.wangyou.websocketforandroid.mapper.UserRelationMapper;
import com.wangyou.websocketforandroid.service.ChatService;
import com.wangyou.websocketforandroid.service.GroupService;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Override
    public List<Chat> findSessionList(String username) {
        // 查询私聊记录
        List<User> friends = userService.findFriend(username);
        Set<Long> friendsId = new HashSet<>();
        // 避免sql异常
        friendsId.add(-2L);
        for (User friend : friends) {
            friendsId.add(friend.getUid());
        }
        Map<Long, Chat> personalSession = new HashMap<>();
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        // 查看登录者最近发送的消息
        List<Chat> chats = baseMapper.findLastSendChatList(Chat.PRIVATE_CHAT, user.getUid(), friendsId);
        for (Chat chat : chats) {
            personalSession.put(chat.getRecipient(), chat);
        }
        // 查看好友最近发给登录者的消息
        chats = baseMapper.findLastReceiveChatList(Chat.PRIVATE_CHAT, user.getUid(), friendsId);
        // 保留最新记录
        for (Chat chat : chats) {
            if (!personalSession.containsKey(chat.getSender())
                    || personalSession.get(chat.getSender()).getUpdateTime() < chat.getUpdateTime()) {
                personalSession.put(chat.getSender(), chat);
            }
        }
        // 查询群聊记录
        List<Group> groupList = groupService.findGroupList(username);
        Set<Long> groupsId = new HashSet<>();
        // 避免sql异常
        groupsId.add(-2L);
        for (Group group : groupList) {
            groupsId.add(group.getGid());
        }
        List<Chat> groupSession = baseMapper.findLastGroupChatList(Chat.GROUP_CHAT, groupsId);
        // 整合所有聊天记录
        groupSession.addAll(personalSession.values());
        groupSession.sort((o1, o2) -> -1 * o1.getUpdateTime().compareTo(o2.getUpdateTime()));
        return groupSession;
    }

    @Override
    public List<Chat> findChatList(String username, Integer type, Long id) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        List<Chat> chatList = new ArrayList<>();
        if (type == Chat.PRIVATE_CHAT) {
            chatList = list(Wrappers.<Chat>lambdaQuery()
                    .eq(Chat::getEnable, Chat.PRIVATE_CHAT)
                    .and(i -> i.nested(j -> j.eq(Chat::getSender, user.getUid())
                                    .eq(Chat::getRecipient, id))
                            .or()
                            .nested(j -> j.eq(Chat::getSender, id)
                                    .eq(Chat::getRecipient, user.getUid())))
                    .orderBy(true, true, Chat::getUpdateTime));
        } else {
            chatList = list(Wrappers.<Chat>lambdaQuery()
                    .eq(Chat::getEnable, Chat.GROUP_CHAT)
                    .eq(Chat::getGid, id)
                    .orderBy(true, true, Chat::getUpdateTime));
        }
        return chatList;
    }

    @Override
    public Chat handleChat(String username, Chat chat) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        chat.setSender(user.getUid());
        baseMapper.insert(chat);
        return chat;
    }
}
