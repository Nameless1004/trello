package com.trelloproject.domain.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackClient {

    @Value("${NOTIFICATION_SLACK_WEBHOOK_URL}")
    private String slackAlertWebhookUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    public void notify(String payload)  {
        try {
            Slack slack = Slack.getInstance();

            Map<String, String> map = new HashMap<>();
            map.put("text", payload);

            WebhookResponse response = slack.send(slackAlertWebhookUrl,
                mapper.writeValueAsString(map));
            SlackResponse slackResponse = new SlackResponse(response.getCode(),
                response.getMessage(),
                response.getBody());
            String json = mapper.writeValueAsString(slackResponse);
            log.error("[ slack notify result ] {}", json);
        } catch (Exception e) {
            log.error("Slack Message Send Failed!!");
        }
    }

    private record SlackResponse(int code, String message, String body){ }
}
