package com.amarogamedev.plantai.tools;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ToolLoggingAspect {

    @Around("@annotation(org.springframework.ai.tool.annotation.Tool)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        String toolName = joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();

        log.info("→ Tool: {} called with arguments: {}", toolName, Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            if (result instanceof ToolResponse<?> response) {
                if (response.success()) {
                    log.info("✓ Tool: {} completed after {} ms - {}", toolName, duration, response.data());
                } else {
                    log.warn("⚠ Tool: {} completed with business error [{}] after {} ms", toolName, response.errorCode(), duration);
                }

            } else {
                log.info("✓ Tool: {} completed after {} ms", toolName, duration);
            }
            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("✗ Tool: {} failed after {} ms", toolName, duration, e);
            throw e;
        }
    }
}