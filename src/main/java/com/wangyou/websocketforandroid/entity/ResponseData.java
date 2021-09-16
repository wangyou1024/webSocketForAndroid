package com.wangyou.websocketforandroid.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> implements Serializable {
    private String code;
    private String msg;
    private T data;
}
