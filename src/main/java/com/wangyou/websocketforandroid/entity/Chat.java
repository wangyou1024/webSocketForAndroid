package com.wangyou.websocketforandroid.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("chat")
public class Chat {

    public final static int PRIVATE_CHAT = 1;
    public final static int GROUP_CHAT = 2;
    public final static int DISABLE = 0;

    @TableId(value = "cid", type = IdType.AUTO)
    private Long cid;

    @TableField("sender")
    private Long sender;

    @TableField("recipient")
    private Long recipient;

    @TableField("gid")
    private Long gid;

    @TableField("content")
    private String content;

    @TableField("update_time")
    private Integer updateTime;

    @TableField("enable")
    private Integer enable;
}
