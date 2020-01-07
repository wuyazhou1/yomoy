package com.nsc.Amoski.config;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/*
@Component
class StringToDateConverter implements Converter<String, Date> {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Date convert(String source) {
        if (StringUtils.isNotEmpty(source)) {
            try {
                if(source.matches("^\\d{4}-\\d{2}-\\d{2}[^\\r\\t]*")) {
                    return DateUtil.parseDate(source);
                } else if(source.matches("^\\d+")) {
                    Date d = new Date();
                    d.setTime(Long.valueOf(source));
                    return d;
                } else {
                    logger.warn("日期参数错误:\"{}\"", source);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}*/
