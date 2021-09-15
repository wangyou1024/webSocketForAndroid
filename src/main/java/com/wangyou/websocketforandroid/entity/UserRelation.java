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
@TableName("user_relation")
public class UserRelation {

    @TableId(value = "urid", type = IdType.AUTO)
    private Long urid;

    @TableField("uid_former")
    private Long uidFormer;

    @TableField("uid_latter")
    private Long uidLatter;

    @TableField("update_time")
    private Integer updateTime;

    @TableField("read_time")
    private Integer readTime;

    @TableField("enable")
    private Integer enable;
}
