package com.wangyou.websocketforandroid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangyou.websocketforandroid.entity.Chat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {

    public List<Chat> findLastSendChatList(@Param("type") Integer type, @Param("sender") Long sender, @Param("recipients") Set<Long> recipients);

    public List<Chat> findLastReceiveChatList(@Param("type") Integer type, @Param("recipient") Long recipient, @Param("senders") Set<Long> sender);

    public List<Chat> findLastGroupChatList(@Param("type") Integer type, @Param("groups") Set<Long> groups);
}
