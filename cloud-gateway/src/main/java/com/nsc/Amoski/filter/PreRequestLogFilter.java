package com.nsc.Amoski.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class PreRequestLogFilter extends ZuulFilter {
    private static final Log logger = LogFactory.getLog(PreRequestLogFilter.class);

    private String cacheToken;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        long starTime = System.currentTimeMillis();
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        //response.setHeader("Access-Control-Allow-Origin","*");
        logger.info("URL:"+request.getRequestURL().toString());
        logger.info("HTTP_METHOD:"+request.getMethod());
        long endTime = System.currentTimeMillis();
        logger.info("转发秒"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return false;
    }
}
