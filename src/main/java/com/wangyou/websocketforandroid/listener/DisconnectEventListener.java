package com.wangyou.websocketforandroid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * 断开连接监听器
 */
@Slf4j
@Component
public class DisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        log.info("断开连接：" + sessionDisconnectEvent.getUser().getName() + " -> " + sessionDisconnectEvent.getMessage().getHeaders());
    }
}
