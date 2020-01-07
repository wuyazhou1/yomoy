package com.nsc.AmoskiUser.chlientTest;


import com.google.common.collect.Maps;
import com.nsc.AmoskiUser.client.MyFormAuthenticationFilter;
import com.nsc.AmoskiUser.client.ShrioRedisCacheManager;
import com.nsc.AmoskiUser.client.URLPermissionsFilter;
import com.nsc.AmoskiUser.client.UserRealm;
import com.nsc.Amoski.config.RedisCacheConfig;
import com.nsc.Amoski.util.StringUtils;
import net.sf.ehcache.CacheManager;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.*;


@Configuration
public class ShiroConfigTestA {
    private Logger logger = LoggerFactory.getLogger(ShiroConfigTestA.class);
    /**
     * FilterRegistrationBean
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        logger.info("进入ShiroConfigTestA==》filterRegistrationBean");
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    /**
     * @see ShiroFilterFactoryBean
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(){
        logger.info("进入ShiroConfigTestA==》shiroFilter");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //<!-- 注入shiro的安全管理员 -->
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //为登入跳转页面
        shiroFilterFactoryBean.setLoginUrl("/AmoskiWebUser/AMOSKI/loginNameUser");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/AmoskiWebUser/AMOSKI/loginNameUser");
        //中
        Map<String, Filter>filters = Maps.newHashMap();
        filters.put("otpLoginFilter", new OtpLoginFilter());
        filters.put("otpLogoutFilter", new OtpLogoutFilter());
        filters.put("otpAccessFilter",new OtpAccessFilter() );


        filters.put("authc", new MyFormAuthenticationFilter());
        Map<String,String> filterChainDefinitionMap = Maps.newHashMap();
        filterChainDefinitionMap.put("/AMOSKI/LoginUserCheck.html","otpLoginFilter");
        filterChainDefinitionMap.put("/MemberManage/GetDictZtreeData", "otpLoginFilter");//字典公共接口
        filterChainDefinitionMap.put("/logout","otpLogoutFilter");
        filterChainDefinitionMap.put("/AMOSKI/LoginUserCheck.html","anon");
        filterChainDefinitionMap.put("/MemberWeiXinManage/*", "anon");

        //filterChainDefinitionMap.put("/AMOSKI/XiTongMenu","anon");
        filterChainDefinitionMap.put("/css/**", "anon");//前端css样式
        filterChainDefinitionMap.put("/images/**", "anon");//前端所有图片
        filterChainDefinitionMap.put("/js/**", "anon");//前端所有js
        filterChainDefinitionMap.put("/AMOSKI/loginNameUser", "anon");//前端用户登入页面
        filterChainDefinitionMap.put("/**","otpAccessFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setFilters(filters);
        //property名为[filterChainDefinitions]中的[value]对应的过滤值
//        shiroFilterFactoryBean.setFilterChainDefinitions("1");
        return shiroFilterFactoryBean;
    }
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")//可选
    public UserRealm ShiroConfig(){
        logger.info("进入ShiroConfigTestA==》ShiroConfig");
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        //userRealm.setCacheManager(redisCacheManager());
        userRealm.setCachingEnabled(true);
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCachingEnabled(true);
        return userRealm;
    }
    /*@Bean
    public UserRealm ShiroDBConfig(){
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }*/
    /**
     * @return
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager securityManager() {
        logger.info("进入ShiroConfigTestA==》securityManager");
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        manager.setAuthenticator(authenticator());
        manager.setAuthorizer(authorizer());
        //manager.setRealms(list);
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");

        manager.setCacheManager(redisCacheManager());//
        manager.setSessionManager(defaultWebSessionManager());
        return manager;
    }

    @Bean
    public ModularRealmAuthorizer authorizer(){
        logger.info("进入ShiroConfigTestA==》authorizer");
        ModularRealmAuthorizer ModularRealmAuthorizer = new ModularRealmAuthorizer();
        List list = new ArrayList();
        list.add(ShiroConfig());
        //list.add(ShiroDBConfig());
        ModularRealmAuthorizer.setRealms(list);
        return ModularRealmAuthorizer;
    }

    @Bean
    public ModularRealmAuthenticator authenticator(){
        logger.info("进入ShiroConfigTestA==》authenticator");
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        //authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        List list = new ArrayList();
        list.add(ShiroConfig());
        //list.add(ShiroDBConfig());
        authenticator.setRealms(list);
        return authenticator;
    }

    @Bean(name="doAuthorizationInfo")
    public DoAuthorizationInfo doAuthorizationInfo(){
        logger.info("进入ShiroConfigTestA==》doAuthorizationInfo");
        return new DoAuthorizationInfo();
    }

    /**
     * @see DefaultWebSessionManager
     * @return
     */
    @Bean(name="sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager() {
        logger.info("进入ShiroConfigTestA==》defaultWebSessionManager");
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(redisCacheManager());
        sessionManager.setGlobalSessionTimeout(3600000);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        /*sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);*/
        return sessionManager;
    }
    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        logger.info("进入ShiroConfigTestA==》sessionIdCookie");
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        /*logger.info("进入ShiroConfigTestA==》sessionDAO");
        //拿到所有节点配置
        Set<HostAndPort> haps = new HashSet<>();
        haps.add(new HostAndPort("192.168.5.199", 7000));
        haps.add(new HostAndPort("192.168.5.199", 7001));
        haps.add(new HostAndPort("192.168.5.199", 7002));
        haps.add(new HostAndPort("192.168.5.199", 7003));
        GenericObjectPoolConfig GenericObjectPoolConfig = new GenericObjectPoolConfig();
        GenericObjectPoolConfig.setMaxWaitMillis(-1);
        GenericObjectPoolConfig.setMaxTotal(1000);
        GenericObjectPoolConfig.setMaxIdle(100);
        GenericObjectPoolConfig.setMinIdle(8);
        JedisCluster jedisCluster = new JedisCluster(haps,300000,4, GenericObjectPoolConfig);
        RedisManager redisManager = new RedisManager();
        redisManager.setJedisCluster(jedisCluster);
        RedisOperator redisOperator = new RedisOperator();
        redisOperator.setJedisCluster(jedisCluster);
        redisManager.setRedisOperator(redisOperator);
        RedisSessionDAO RedisSessionDAO = new RedisSessionDAO();
        RedisSessionDAO.setRedisManager(redisManager);
        RedisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        RedisSessionDAO.setKeyPrefix("shiro-activeSessionCache");
        return RedisSessionDAO;*/




        logger.info("进入ShiroConfigTestA==》sessionDAO");

        String[] clusterNodes = StringUtils.redisClusterNodes.split(",");
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        for(String node:clusterNodes){
            String[] ipPort = node.split(":");
            logger.info("redis初始化"+ipPort[0]+":"+ipPort[1]);
            jedisClusterNodes.add(new HostAndPort(ipPort[0], Integer.parseInt(ipPort[1])));
        }
        GenericObjectPoolConfig GenericObjectPoolConfig = new GenericObjectPoolConfig();
        GenericObjectPoolConfig.setMaxWaitMillis(-1);
        GenericObjectPoolConfig.setMaxTotal(Integer.parseInt(StringUtils.redisPoolMaxTotal));
        GenericObjectPoolConfig.setMaxIdle(Integer.parseInt(StringUtils.redisPoolMaxIdle));
        GenericObjectPoolConfig.setMinIdle(Integer.parseInt(StringUtils.redisPoolMinIdle));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes,Integer.parseInt(StringUtils.redisTimeOut),Integer.parseInt(StringUtils.redisPoolMaxMaxActive), GenericObjectPoolConfig);
        RedisManager redisManager = new RedisManager();
        redisManager.setJedisCluster(jedisCluster);
        RedisOperator redisOperator = new RedisOperator();
        redisOperator.setJedisCluster(jedisCluster);
        redisManager.setRedisOperator(redisOperator);
        RedisSessionDAO RedisSessionDAO = new RedisSessionDAO();
        RedisSessionDAO.setRedisManager(redisManager);
        RedisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        RedisSessionDAO.setKeyPrefix("shiro-activeSessionCache");

        return RedisSessionDAO;
    }

    /**
     * 配置会话ID生成器
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }
    @Bean
    public EhCacheManager ehCacheManager() {
        logger.info("进入ShiroConfigTestA==》sessionIdGenerator");
        EhCacheManager em = new EhCacheManager();
        //将ehcacheManager转换成shiro包装后的ehcacheManager对象
        em.setCacheManager(CacheManager.create());
        //em.setCacheManagerConfigFile("classpath:ehcache.xml");
        return em;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        logger.info("进入ShiroConfigTestA==》hashedCredentialsMatcher");
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        //hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        logger.info("进入ShiroConfigTestA==》authorizationAttributeSourceAdvisor");
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean
    @DependsOn(value="lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        logger.info("进入ShiroConfigTestA==》defaultAdvisorAutoProxyCreator");
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        logger.info("进入ShiroConfigTestA==》lifecycleBeanPostProcessor");
        return new LifecycleBeanPostProcessor();
    }


    /**
     * @see UserRealm--->AuthorizingRealm
     * @return
     */
    @Bean
    @DependsOn(value={"lifecycleBeanPostProcessor", "shrioRedisCacheManager"})
    public UserRealm userRealm() {
        logger.info("进入ShiroConfigTestA==》userRealm");
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        userRealm.setCacheManager(redisCacheManager());
        userRealm.setCachingEnabled(true);
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCachingEnabled(true);
        return userRealm;
    }



    @Bean
    public URLPermissionsFilter urlPermissionsFilter() {
        logger.info("进入ShiroConfigTestA==》urlPermissionsFilter");
        return new URLPermissionsFilter();
    }

    @Bean(name="shrioRedisCacheManager")
    @DependsOn(value="redisTemplate")
    public ShrioRedisCacheManager redisCacheManager() {
        logger.info("进入ShiroConfigTestA==》redisCacheManager");
        ShrioRedisCacheManager cacheManager = new ShrioRedisCacheManager(redisTemplate());
        return cacheManager;
    }

    @Bean(name="redisTemplate")
    @DependsOn("redisConnectionFactory")
    public RedisTemplate<byte[], Object> redisTemplate() {
        logger.info("进入ShiroConfigTestA==》redisTemplate");
        RedisTemplate<byte[], Object> template = new RedisTemplate<>();
        logger.info("进入ShiroConfigTestA==》redisTemplate==RedisCacheConfig.jedisConnectionFactory:"+RedisCacheConfig.jedisConnectionFactory);
        template.setConnectionFactory(RedisCacheConfig.jedisConnectionFactory);
        return template;
    }

    /*@Bean
    public JedisConnectionFactory connectionFactoryS(){
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        Set<RedisNode> jedisClusterNodes = new HashSet<RedisNode>();
        jedisClusterNodes.add(new RedisNode("192.168.5.199", 7000));
        jedisClusterNodes.add(new RedisNode("192.168.5.199", 7001));
        jedisClusterNodes.add(new RedisNode("192.168.5.199", 7002));
        jedisClusterNodes.add(new RedisNode("192.168.5.199", 7003));
        redisClusterConfiguration.setClusterNodes(jedisClusterNodes);
        System.out.println("192.168.5.199:7000");
        System.out.println("192.168.5.199:7001");
        System.out.println("192.168.5.199:7002");
        System.out.println("192.168.5.199:7003");

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(150);
        jedisPoolConfig.setMaxIdle(30);
        jedisPoolConfig.setMinIdle(10);

        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration);
        redisConnectionFactory.setPoolConfig(jedisPoolConfig);

        return redisConnectionFactory;
    }*/

}