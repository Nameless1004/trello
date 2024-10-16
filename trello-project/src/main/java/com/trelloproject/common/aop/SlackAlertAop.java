package com.trelloproject.common.aop;

import com.trelloproject.common.annotations.SlackAlert;
import com.trelloproject.domain.notification.SlackClient;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class SlackAlertAop {
    @Value("${DEFAULT_SLACK_WEBHOOK_URL}")
    private String defaultSlackWebhookUrl;  // 여기에서 설정 값 주입

    private final SlackClient slackClient;

    @Pointcut("@annotation(com.trelloproject.common.annotations.SlackAlert)")
    public void annotaionPc(){}

    @Around("annotaionPc()")
    public Object slackAlert(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SlackAlert annotation = method.getAnnotation(SlackAlert.class);
        String webhookUrl = annotation != null && StringUtils.hasText(annotation.hookUrl()) ? annotation.hookUrl() : defaultSlackWebhookUrl;
        String msg = annotation != null && StringUtils.hasText(annotation.message()) ? annotation.message() : "Method Completed!!";

        try{
            Object proceed = joinPoint.proceed();

            String payload = MessageFormat.format(
                "\n"+"""
                ```
                🔔 *[Slack Alert]* 🔔
                *────────────────────────────────────────────────────────*
                📌 *Method*: {0}
                ✉️ *Message*: "{1}"
                🕒 *Timestamp*: {2}
                *────────────────────────────────────────────────────────*```
                """ +"\n",
                joinPoint.getSignature().toShortString(),
                msg,
                LocalDateTime.now().toString()
            );

            slackClient.notify(webhookUrl, payload);
            return proceed;
        } catch (Exception e) {
            throw e;
        }
    }
}
