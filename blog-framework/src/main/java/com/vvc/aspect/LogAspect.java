package com.vvc.aspect;

import com.vvc.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {
    //切点
    @Pointcut("@annotation(com.vvc.annotation.SystemLog)")
    public void pt() {
    }

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret = null;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }

        return ret;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        StringBuffer url = request.getRequestURL();
        //获取被增强用户上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SystemLog annotation = method.getAnnotation(SystemLog.class);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",url);
        // 打印描述信息
        log.info("BusinessName   : {}",annotation.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}",request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),method.getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost() );
        // 打印请求入参
        log.info("Request Args   : {}",joinPoint.getArgs());
    }

    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}",ret);
    }
}
