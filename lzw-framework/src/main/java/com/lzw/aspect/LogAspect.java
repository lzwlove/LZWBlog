package com.lzw.aspect;

import com.alibaba.fastjson.JSON;
import com.lzw.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 切面类
 */

@Component//注入容器
@Aspect//告知spring这是一个切面类
@Slf4j
public class LogAspect {

    //1.确定切点
    @Pointcut("@annotation(com.lzw.annotation.SystemLog)")
    public void pt() {

    }

    //2.通知方法
    @Around("pt()")//参数：指定切点是哪一个
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //参数：joinPoint是被增强的方法封装出来的对象
        //joinPoint专业术语：连接点

        Object ret;
        try {
            //方法执行之前
            handleBefore(joinPoint);
            //目标方法的调用
            ret = joinPoint.proceed();
            //方法执行之后
            handleAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }

        //目标方法增强（执行）后的返回值
        return ret;
    }

    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(ret));
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}", request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringType(),((MethodSignature) joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = signature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;
    }

}
