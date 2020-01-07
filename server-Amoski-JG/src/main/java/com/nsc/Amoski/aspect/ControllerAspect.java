package com.nsc.Amoski.aspect;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.nsc.Amoski.exception.JGException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author 李阳
 * @date 2019/12/12 15:19
 */
@Aspect
@Component
@Slf4j
public class ControllerAspect {

    /**
     * 切入点表达式
     */
    @Pointcut(value = "execution(public * com.nsc.Amoski.controller..*.*(..)) && !execution(public * com.nsc.Amoski.controller.HandlerController.*(..))")
    public void pointcut() {

    }

    /**
     * 环绕通知
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        log.info("start");
        //打印调用类名称和Swagger Api注解tags
        log.info("接口={}({})", proceedingJoinPoint.getTarget().getClass().getName(), proceedingJoinPoint.getTarget().getClass().getAnnotation(Api.class).tags()[0]);
        Method[] methods = proceedingJoinPoint.getTarget().getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(proceedingJoinPoint.getSignature().getName())) {
                //打印调用类的方法名称和Swagger ApiOperation注解value
                log.info("方法={}({})", proceedingJoinPoint.getSignature().getName(), method.getAnnotation(ApiOperation.class).value());
                break;
            }
        }
        Object[] args = proceedingJoinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("参数=");
        for (int i = 0; i < args.length; i++) {
            if (i + 1 == args.length) {
                stringBuilder.append("[{}]");
            } else {
                stringBuilder.append("[{}],");
            }
        }
        //打印方法中传入的参数
        log.info(stringBuilder.toString(), args);
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (APIRequestException e) {
            throw new JGException(e.getMessage());
        } catch (APIConnectionException e) {
            throw new JGException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        log.info("结果={}", result);
        log.info("end");
        return result;
    }

}
