package com.wangyou.websocketforandroid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangyou.websocketforandroid.entity.Chat;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
}
