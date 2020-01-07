package com.nsc.Amoski.config;

import cn.jmessage.api.JMessageClient;
import com.nsc.Amoski.utlis.JGUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 李阳
 * @date 2019-12-10 11:02
 * 极光IM客户端配置
 */
@Configuration
public class JMessageClientConfig {

    @Bean
    public JMessageClient jMessageClient() {
        JMessageClient jMessageClient = new JMessageClient(JGUtil.APP_KEY, JGUtil.MASTER_SECRET);
        return jMessageClient;
    }

}
