package com.tdt.ip.aspect;

import com.alibaba.fastjson.JSON;
import com.tdt.ip.commons.JsonResult;
import com.tdt.ip.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mr.superbeyone
 * @project cs-sys
 * @className LogAspect
 * @description 日志切面
 * @date 2020-04-16 14:58
 **/
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 表示包及子包 该方法代表controller层的所有方法
     */
    @Pointcut("execution(public * com.tdt.ip.service..*.*(..))")
    public void requestMethod() {

    }

    @Before("requestMethod()")
    public void logRequestInfo(JoinPoint joinPoint) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        // 处理请求参数
        String[] paramNames = ((MethodSignature) signature).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paramNames.length; i++) {
            if (StringUtils.equalsIgnoreCase(paramNames[i], "request")) {
                continue;
            }
            builder.append(paramNames[i]).append("=").append(paramValues[i]);
            if (i + 1 < paramNames.length) {
                builder.append(",");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.info("======================切点 start============================");
            logger.info("IP:[{}],URL:[{}],Method:[{}],Class:[{}.{}],Args:[{{}}]",
                    IpUtil.getIp(request),
                    request.getRequestURI(),
                    request.getMethod(),
                    signature.getDeclaringTypeName(),
                    signature.getName(), builder.toString());
            logger.info("======================切点 end============================");
        }

    }


    @AfterReturning(returning = "result", pointcut = "requestMethod()")
    public void logResult(JsonResult<Object> result) throws Exception {
        logger.info("======================响应结果 start============================");
        logger.info("result:[{}]", JSON.toJSONString(result));
        logger.info("======================响应结果 end============================");
    }

    /**
     * 异常通知
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "requestMethod()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        // 处理请求参数
        String[] paramNames = ((MethodSignature) signature).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paramNames.length; i++) {
            if (StringUtils.equalsIgnoreCase(paramNames[i], "request")) {
                continue;
            }
            builder.append(paramNames[i]).append("=").append(paramValues[i]);
            if (i + 1 < paramNames.length) {
                builder.append(",");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.error("====================== 异常 start============================");
            logger.error("IP:[{}],URL:[{}],Method:[{}],Class:[{}.{}],Args:[{{}}],Exception:[{}],Reason:[{}]",
                    IpUtil.getIp(request),
                    request.getRequestURI(),
                    request.getMethod(),
                    signature.getDeclaringTypeName(),
                    signature.getName(), builder.toString(), e.getMessage(), e.getCause());
            logger.error("======================异常 end============================");
        }
    }


}
