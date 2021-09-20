package com.wangyou.websocketforandroid.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("group_relation")
public class GroupRelation {

    public final static int NO_DEAL = 0;
    public final static int REFUSE = 1;
    public final static int AGREE = 2;
    public final static int DELETE = 3;
    public final static int DISMISS = 4;
    public final static int LEADER = 5;

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

    /**
     * 是否可用，0代表未操作，1代表拒绝，2代表同意，3代表群聊已解散，4代表群主
     */
    @TableField("enable")
    private Integer enable;
}
