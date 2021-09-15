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
@TableName("group")
public class Group {

    @TableId(value = "gid", type = IdType.AUTO)
    private Long gid;

    @TableField("group_num")
    private String groupNum;

    @TableField("group_name")
    private String groupName;

    @TableField("group_image")
    private String groupImage;

    @TableField("introduce")
    private String introduce;

    @TableField("update_time")
    private Integer updateTime;

    @TableField("enable")
    private Integer enable;
}
