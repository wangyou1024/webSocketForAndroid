package com.wangyou.websocketforandroid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 订阅监听器
 */
@Slf4j
@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        log.info("订阅：" + sessionSubscribeEvent.getUser().getName() + "->" + sessionSubscribeEvent.getMessage().getHeaders());
    }
}
