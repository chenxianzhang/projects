package edu.coursemgr.aop;

import edu.coursemgr.common.ResponseBase;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenxianzhang
 * @date 2018-08-07
 * @version 0.0.1
 * @description 负责将返回转换成统一消息格式
 * 序列化为的格式如下:
 * {
"status": 1,
"data": data,
"msg": ""
}
其中被注解的方法只需要关心data的内容即可
 */
@Aspect
@Order(0)
@Component
public class ResponseAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MappingJackson2HttpMessageConverter converter;


    /**
     * 拦截所有@ResponseBody
     * @author chenxianzhang
     * @date 2018-08-07
     * @description
     */
//    @Pointcut("execution(* edu.coursemgr.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Pointcut("execution(* edu.coursemgr.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.ResponseBody)")
    public void responseBodyPointCut() {

    }

    /**
     * @author chenxianzhang
     * @date 2018-08-07
     * @description
     * @param pjp
     * @throws Throwable
     */
    @Around(value = "responseBodyPointCut()")
    @ResponseBody
    public void formatResult2JSON(ProceedingJoinPoint pjp) throws Throwable {
        Object ret = pjp.proceed();
        ResponseBase responseBase = new ResponseBase();
        responseBase.setData(ret);

        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes()).getResponse();
        // 使用了cors处理跨域，则不能做如下操作
//        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        converter.write(responseBase, MediaType.APPLICATION_JSON, outputMessage);
        shutdownResponse(response);
    }

    /**
     *
     * @author chenxianzhang
     * @date 2018-08-07
     * @description
     * @param jp
     * @param error
     * @throws Throwable
     */
    @AfterThrowing(pointcut = "responseBodyPointCut()", throwing = "error")
    public void handleForException(JoinPoint jp, Throwable error) throws Throwable{
        ResponseBase responseBase = new ResponseBase();
        responseBase.setStatus(0);
        responseBase.setMsg(error.getMessage());
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        // 使用了cors处理跨域，则不能做如下操作
//        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.error(jp.getSignature().getName() + "-error!", error);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        converter.write(responseBase, MediaType.APPLICATION_JSON, outputMessage);
        shutdownResponse(response);
    }

    private void shutdownResponse(HttpServletResponse response) throws IOException {
        response.getOutputStream().close();
    }
}
