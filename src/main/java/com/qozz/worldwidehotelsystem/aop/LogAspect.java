package com.qozz.worldwidehotelsystem.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut(value = "execution(* com.qozz.worldwidehotelsystem.controller..*(..)) ||" +
            "execution(* com.qozz.worldwidehotelsystem.service..*(..))")
    public void logMethodPointcut() { }

    @Around("logMethodPointcut()")
    public Object logMethod(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        String pathClassMethodArgs = signature.getDeclaringTypeName()
                .replaceAll("com.qozz.worldwidehotelsystem", "**")
                + "." + signature.getName()
                + "(" + Arrays.toString(pjp.getArgs()).replaceAll("[\\[\\]]", "") + ")";

        log.info(">>>>>>>>>> Start method: " + pathClassMethodArgs);

        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();

        log.info("<<<<<<<<<< End method: " + pathClassMethodArgs + ", execution time [" + (endTime - startTime) + "ms]");

        return result;
    }

}
