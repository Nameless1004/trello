package com.trelloproject.domain.notification;

import org.springframework.context.ApplicationEvent;

public class SlackNotifyEvent extends ApplicationEvent {

    public SlackNotifyEvent(String message) {
        super(message);
    }

    public String getMessage() {
        return (String) super.getSource();
    }
}
