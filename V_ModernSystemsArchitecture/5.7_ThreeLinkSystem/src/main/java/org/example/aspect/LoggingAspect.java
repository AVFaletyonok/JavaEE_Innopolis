package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(LoggingBefore)")
    public void logBefore() {}

    @Pointcut("@annotation(LoggingAfter)")
    public void logAfter() {}

    @Pointcut("@annotation(LoggingAround)")
    public void logAround() {}


    @Before("logBefore()")
    public void logB(JoinPoint joinPoint) {
        log.info("Calling before the method: {}", joinPoint.toString());
    }

    @After("logAfter()")
    public void logA(JoinPoint joinPoint) {
        log.info("Calling after the method: {}", joinPoint.toString());
    }

    // it doesn't work if there are annotations in the package 'annotation'
    // I won't change it while it's working))
    @Around("logAround()")
    public Object logAr(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            log.info("Measuring of execution of the method '{}': {} milliseconds",
                    proceedingJoinPoint.toString(), System.currentTimeMillis() - startTime);
        }
    }
}
