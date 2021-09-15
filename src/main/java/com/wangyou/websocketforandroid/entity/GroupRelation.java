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
@TableName("group_relation")
public class GroupRelation {

    @TableId(value = "grid",  type = IdType.AUTO)
    private Long grid;

    @TableField("gid")
    private Long gid;

    @TableField("uid")
    private Long uid;

    @TableField("update_time")
    private Integer updateTime;

    @TableField("read_time")
    private Integer readTime;

    @TableField("enable")
    private Integer enable;
}
