package com.wangyou.websocketforandroid.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {
    private String code;
    private String msg;
    private T data;
}
