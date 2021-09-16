package com.wangyou.websocketforandroid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * 连接监听器
 */
@Slf4j
@Component
public class ConnectEventListener implements ApplicationListener<SessionConnectEvent> {
    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        log.info("连接：" + sessionConnectEvent.getUser().getName() + "->" + sessionConnectEvent.getMessage());
    }
}
