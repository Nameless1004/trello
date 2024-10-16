package com.trelloproject.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlackNotifyEventListener {

    private final SlackClient slackClient;

    /**
     * SlackNotifyEvent가 발행되면 실행되는 메서드
     * @param event
     */
    @EventListener
    public void onSlackNotify(SlackNotifyEvent event) {
        String message = event.getMessage();
        slackClient.notify(message);
    }
}
