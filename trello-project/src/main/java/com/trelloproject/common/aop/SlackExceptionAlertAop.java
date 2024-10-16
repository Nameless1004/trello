package com.trelloproject.common.aop;

import com.trelloproject.common.annotations.SlackExceptionAlert;
import com.trelloproject.domain.notification.SlackClient;
import com.trelloproject.security.AuthUser;
import com.trelloproject.security.JwtAuthenticationToken;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @AfterThrowing(value = "annotaionPc()", throwing = "e")
    public void slackAlert(JoinPoint joinPoint, RuntimeException e) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SlackExceptionAlert annotation = method.getAnnotation(SlackExceptionAlert.class);
        String webhookUrl = annotation != null && StringUtils.hasText(annotation.value()) ? annotation.value() : defaultSlackWebhookUrl;

        // Auth user 가져오기
        AuthUser auth = null;
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        auth = authentication == null ? null : (AuthUser) authentication.getPrincipal();

        String authInfo = auth == null
            ? "NO AUTH"
            : "{ "+MessageFormat.format("Id: {0} || Email: {1} || ROLE: {2}" ,auth.getUserId(), auth.getEmail(), Arrays.toString(auth.getAuthorities().toArray())) + " }";

        String message = MessageFormat.format(
            "\n"+"""
                ```
                🚨 [Exception Alert]* 🚨
                
                👤 Auth: {0}
                📌 Service: {1}
                ⚠️ Exception: {2}
                💥 Message: {3}
                🕒 Timestamp: {4}
                """ +"\n",
            authInfo,
            joinPoint.getSignature().toShortString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            LocalDateTime.now().toString()
        );
        slackClient.notify(webhookUrl,message);
    }
}
