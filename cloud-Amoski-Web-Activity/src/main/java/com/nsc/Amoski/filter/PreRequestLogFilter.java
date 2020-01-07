package com.nsc.Amoski.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

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
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        logger.info("URL:"+request.getRequestURL().toString());
        logger.info("HTTP_METHOD:"+request.getMethod());

        return null;
    }
}
