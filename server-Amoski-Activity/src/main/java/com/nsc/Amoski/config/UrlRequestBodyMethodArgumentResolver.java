package com.nsc.Amoski.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.lang.UsesJava8;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.security.Principal;
import java.time.ZoneId;
import java.util.*;

@Log
public class UrlRequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {
    Logger logger = LoggerFactory.getLogger(UrlRequestBodyMethodArgumentResolver.class);
    @Autowired
    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    @Autowired
    private ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor;
    public UrlRequestBodyMethodArgumentResolver(RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor,
                                                ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor){
        this.requestResponseBodyMethodProcessor=requestResponseBodyMethodProcessor;
        this.servletModelAttributeMethodProcessor=servletModelAttributeMethodProcessor;
    }
    public UrlRequestBodyMethodArgumentResolver(){}

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        final String parameterJson = webRequest.getParameter(parameter.getParameterName());
        final Type type = parameter.getGenericParameterType();
        //final Object o = requestResponseBodyMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        final Object o = JSON.parseObject(parameterJson, type);

        if(o==null||o.equals("")){
            return servletModelAttributeMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        }else{
            return o;
        }


        /*//this.dataBinderFactory = binderFactory;
        Map<String, String[]> parameterMap = ((ServletWebRequest) webRequest).getRequest().getParameterMap();
        if (parameterMap == null) {
            return null;
        }
        Iterator<String> keyIterator = parameterMap.keySet().iterator();
        String key;
        StringBuffer json = new StringBuffer();
        json.append("{");
        boolean isFirst = true;
        while (keyIterator.hasNext()) {
            key = keyIterator.next();
            String[] strings = parameterMap.get(key);
            if (strings.length > 1) {
                logger.info("There can only be one parameter " + key + "!");
            }
            if (isFirst) {
                isFirst = false;
            } else {
                json.append(",");
            }
            json.append("\"");
            json.append(key);
            json.append("\"");
            json.append(":");
            json.append(strings[0]);
        }
        json.append("}");
        Class<?> parameterType = parameter.getParameterType();
        try {

            final Object o = JsonUtil.json2Object(json.toString(), parameterType);
            return o;

        } catch (Exception e) {
            if(parameterMap!=null && parameterMap.size()>0){
                return parameterMap;
            }else{

            }
        }
*/


    }


    public Object resolveArgument2(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Class<?> paramType = parameter.getParameterType();
        //如果是WebRequest的子类
        if (WebRequest.class.isAssignableFrom(paramType)) {
            //不是NativeWebRequest的实现类，则抛出异常
            if (!paramType.isInstance(webRequest)) {
                throw new IllegalStateException(
                        "Current request is not of type [" + paramType.getName() + "]: " + webRequest);
            }
            //直接返回传进来的NativeWebRequest
            return webRequest;
        }
        //从传进来的webRequest中获取HttpServletRequest对象
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        //如果是ServletRequest的类型或者为MultipartRequest的类型
        if (ServletRequest.class.isAssignableFrom(paramType) || MultipartRequest.class.isAssignableFrom(paramType)) {
            //判断类型是否一致
            Object nativeRequest = webRequest.getNativeRequest(paramType);
            if (nativeRequest == null) {
                throw new IllegalStateException(
                        "Current request is not of type [" + paramType.getName() + "]: " + request);
            }
            return nativeRequest;
        }
        else if (HttpSession.class.isAssignableFrom(paramType)) {
            //从request中得到HttpSession对象
            HttpSession session = request.getSession();
            if (session != null && !paramType.isInstance(session)) {
                throw new IllegalStateException(
                        "Current session is not of type [" + paramType.getName() + "]: " + session);
            }
            return session;
        }
        else if (InputStream.class.isAssignableFrom(paramType)) {
            //从request中得到InputStream对象
            InputStream inputStream = request.getInputStream();
            if (inputStream != null && !paramType.isInstance(inputStream)) {
                throw new IllegalStateException(
                        "Request input stream is not of type [" + paramType.getName() + "]: " + inputStream);
            }
            return inputStream;
        }
        else if (Reader.class.isAssignableFrom(paramType)) {
            //从request中得到Reader对象
            Reader reader = request.getReader();
            if (reader != null && !paramType.isInstance(reader)) {
                throw new IllegalStateException(
                        "Request body reader is not of type [" + paramType.getName() + "]: " + reader);
            }
            return reader;
        }
        else if (Principal.class.isAssignableFrom(paramType)) {
            //从request中得到Reader对象
            Principal userPrincipal = request.getUserPrincipal();
            if (userPrincipal != null && !paramType.isInstance(userPrincipal)) {
                throw new IllegalStateException(
                        "Current user principal is not of type [" + paramType.getName() + "]: " + userPrincipal);
            }
            return userPrincipal;
        }
        else if (HttpMethod.class == paramType) {
            //从request中得到请求类型的值
            return HttpMethod.resolve(request.getMethod());
        }
        else if (Locale.class == paramType) {
            //从Request上下文中获取国际化对象
            return RequestContextUtils.getLocale(request);
        }
        else if (TimeZone.class == paramType) {
            //从Request上下文中获取时区对象
            TimeZone timeZone = RequestContextUtils.getTimeZone(request);
            return (timeZone != null ? timeZone : TimeZone.getDefault());
        }
        else if ("java.time.ZoneId".equals(paramType.getName())) {
            //JDK1.8时区
            return ZoneIdResolver.resolveZoneId(request);
        }
        else {
            // Should never happen...
            throw new UnsupportedOperationException(
                    "Unknown parameter type [" + paramType.getName() + "] in " + parameter.getMethod());
        }
    }





    @UsesJava8
    private static class ZoneIdResolver {

        public static Object resolveZoneId(HttpServletRequest request) {
            TimeZone timeZone = RequestContextUtils.getTimeZone(request);
            return (timeZone != null ? timeZone.toZoneId() : ZoneId.systemDefault());
        }
    }


}