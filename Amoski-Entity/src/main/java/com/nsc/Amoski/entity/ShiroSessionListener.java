package com.nsc.Amoski.entity;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShiroSessionListener extends SessionListenerAdapter {
    Logger logger=LoggerFactory.getLogger(ShiroSessionListener.class);
    @Override
    public void onStart(Session session) {//会话创建时触发
        logger.debug("会话创建：" + session.getId());

    }
    @Override
    public void onExpiration(Session session) {//会话过期时触发
        logger.debug("会话过期：" + session.getId());

    }
    @Override
    public void onStop(Session session) {//退出时触发
        logger.info("会话停止：" + session.getId());
    }
}
