package com.nsc.AmoskiUser.client;

/*
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.nsc.AmoskiUser.entity.ShiroUser;
import com.nsc.AmoskiUser.service.LoginUserService;
import com.nsc.AmoskiUser.util.AmoskiException;
import com.nsc.AmoskiUser.util.BaseController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Configuration
public class logginCheckedAspectJ extends BaseController {

    @Autowired
    @Lazy
    private LoginUserService loginUserService;
    public static List list = new ArrayList(){{
        this.add("getRestTemplate");
        this.add("LoginUserCheck");
        this.add("WeiXinRegister");
        this.add("MemberLogin");
        this.add("checkedLoginRepeat");
        this.add("TelRegister");
        this.add("UpdateMemberView");
        this.add("findMemberView");
        this.add("getZtreeDatail");
        this.add("GetDictZtree");
    }};


    */
/*
     *定义一个方法,用于声明切点表达式,该方法一般没有方法体
     *@Pointcut用来声明切点表达式
     *通知直接使用定义的方法名即可引入当前的切点表达式D:\yanfa3\server-AmoskiUser-User\src\main\java\com\nsc\AmoskiUser\controller
     *//*

    @Pointcut("execution(* com.nsc.AmoskiUser.controller.*.*(..))")
    public void PointcutDeclaration() {}

    //前置通知,方法执行之前执行
    @Before("PointcutDeclaration()")
    public void BeforeMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println("方法前进入切面 "+ methodName +"   parameter is  "+ Arrays.asList(args));
        if(!list.contains(methodName)){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            Map<String,Object> parentMap = loginUserService.findRequestShiro(getIpAddr(request), null);
            ShiroUser requestShiro = (ShiroUser) parentMap.get("ShiroUser");
            if(requestShiro==null){
                try {
                    String path=request.getHeader("x-forwarded-host");
                    response.sendRedirect("http://"+path+"/AmoskiWebUser/AMOSKI/loginNameUser");
                    throw new AmoskiException("切面异常");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //后置通知,方法执行之后执行(不管是否发生异常)
    @After("PointcutDeclaration()")
    public void AfterMethod(JoinPoint jp) {
        */
/*String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println("方法后进入切面    "+ methodName +"   parameter is  "+Arrays.asList(args));*//*

    }

    //返回通知,方法正常执行完毕之后执行
    @AfterReturning(value="PointcutDeclaration()",returning="result")
    public void AfterReturningMethod(JoinPoint jp,Object result) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println("方法执行完成进入切面   "+ methodName +"   parameter is  "+Arrays.asList(args)+" "+result);
        if(!list.contains(methodName)){
            System.out.println("返回结果输出"+ result);
        }
    }

    //异常通知,在方法抛出异常之后执行
    @AfterThrowing(value="PointcutDeclaration()",throwing="e")
    public void AfterThrowingMethod(JoinPoint jp,Exception e) {
        */
/*String methodName = jp.getSignature().getName();
        System.out.println("方法执行异常进入切面   "+ methodName +"exception :"+e);*//*

    }
}
*/
