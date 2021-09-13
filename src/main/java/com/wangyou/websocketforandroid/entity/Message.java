package com.wangyou.websocketforandroid.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王游
 * @date 2021/9/3 9:25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String name;
    private String content;
}
