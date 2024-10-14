package com.trelloproject.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trelloproject.common.annotations.SlackExceptionAlert;
import com.trelloproject.domain.notification.SlackClient;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
public class SlackExceptionAlertAop {

    @Value("${DEFAULT_SLACK_WEBHOOK_URL}")
    private String defaultSlackWebhookUrl;  // 여기에서 설정 값 주입

    private final SlackClient slackClient;

    @Pointcut("@annotation(com.trelloproject.common.annotations.SlackExceptionAlert)")
    public void annotaionPc(){}

    @Around("annotaionPc()")
    public Object slackAlert(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SlackExceptionAlert annotation = method.getAnnotation(SlackExceptionAlert.class);
        String webhookUrl = annotation != null && StringUtils.hasText(annotation.value()) ? annotation.value() : defaultSlackWebhookUrl;

        try{
            return joinPoint.proceed();
        } catch (Exception e) {
            if(!StringUtils.hasText(webhookUrl)){
                throw e;
            }

            String message = MessageFormat.format(
                "\n"+"""
                ```
                🚨 *[Exception Alert]* 🚨
                *────────────────────────────────────────────────────────*
                📌 *Service*: {0}
                ⚠️ *Exception*: `{1}`
                💥 *Message*: _"{2}"_
                🕒 *Timestamp*: {3}
                *────────────────────────────────────────────────────────*```
                """ +"\n",
                joinPoint.getSignature().toShortString(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalDateTime.now().toString()
            );
            slackClient.notify(webhookUrl,message);
            throw e;
        }
    }
}
