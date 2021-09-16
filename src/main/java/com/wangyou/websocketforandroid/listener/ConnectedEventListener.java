package com.wangyou.websocketforandroid.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Slf4j
@Component
public class ConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        log.info("连通：" + sessionConnectedEvent.getUser().getName() + "->" + sessionConnectedEvent.getMessage());
    }
}
