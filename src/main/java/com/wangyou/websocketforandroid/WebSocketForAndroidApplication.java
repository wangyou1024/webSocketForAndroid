package com.wangyou.websocketforandroid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wangyou.websocketforandroid.mapper")
public class WebSocketForAndroidApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketForAndroidApplication.class, args);
    }

}
