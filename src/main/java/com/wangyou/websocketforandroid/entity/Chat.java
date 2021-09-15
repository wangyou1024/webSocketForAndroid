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
