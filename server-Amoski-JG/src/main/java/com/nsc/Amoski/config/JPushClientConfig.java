package com.nsc.Amoski.config;

import cn.jpush.api.JPushClient;
import com.nsc.Amoski.utlis.JGUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 李阳
 * @date 2019/12/12 10:38
 * 极光Push客户端配置
 */
@Configuration
public class JPushClientConfig {

    @Bean(destroyMethod = "close")
    public JPushClient jPushClient() {
        JPushClient jPushClient = new JPushClient(JGUtil.MASTER_SECRET, JGUtil.APP_KEY);
        return jPushClient;
    }

}
