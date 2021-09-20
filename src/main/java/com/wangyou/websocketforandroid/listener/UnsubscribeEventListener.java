package com.wangyou.websocketforandroid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

/**
 * 订阅监听器
 */
@Slf4j
@Component
public class UnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        log.info("订阅取消：" + sessionUnsubscribeEvent.getUser().getName() + "->" + sessionUnsubscribeEvent.getMessage().getHeaders());
    }
}
