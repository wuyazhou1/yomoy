package com.nsc.AmoskiUser.client;


/*

@Configuration("ShiroConfig")
public class ShiroConfigTestA {
    */
/**
     * FilterRegistrationBean
     * @return
     *//*

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    */
/**
     * @see ShiroFilterFactoryBean
     * @return
     *//*

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(){
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());





        //拦截器.
        Map<String,String> filterChainDefinitionMap = Maps.newHashMap();
        // 配置不会被拦截的链接 顺序判断
        //会员校验方法
        */
/*filterChainDefinitionMap.put("/MemberWeiXinManage/WeiXinRegister", "anon");//会员微信注册接口
        filterChainDefinitionMap.put("/MemberWeiXinManage/MemberLogin", "anon");//会员登入接口
        filterChainDefinitionMap.put("/MemberWeiXinManage/checkedLoginRepeat", "anon");//验证会员手机号码或账号是否重复
        filterChainDefinitionMap.put("/MemberWeiXinManage/TelRegister", "anon");//会员手机号注册
        filterChainDefinitionMap.put("/MemberWeiXinManage/UpdateMemberView", "anon");//修改会员信息
        filterChainDefinitionMap.put("/MemberWeiXinManage/findMemberView", "anon");//获取会员信息
        filterChainDefinitionMap.put("/css/**", "anon");//前端css样式
        filterChainDefinitionMap.put("/images/**", "anon");//前端所有图片
        filterChainDefinitionMap.put("/js/**", "anon");//前端所有js

        filterChainDefinitionMap.put("/AMOSKI/LoginUserCheck.html", "anon");//用户登入校验
        filterChainDefinitionMap.put("/AMOSKI/XiTongMenu", "anon");//系统菜单页面
        //filterChainDefinitionMap.put("/AMOSKI/loginNameUser", "anon");//前端用户登入页面
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/AMOSKI/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->*//*

        filterChainDefinitionMap.put("/**", "authc");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/logout");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/AMOSKI/XiTongMenu");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        //添加白名单连接
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter>filters = Maps.newHashMap();
        filters.put("perms", urlPermissionsFilter());
        filters.put("anon", new AnonymousFilter());
        filters.put("authc",new MyFormAuthenticationFilter() );
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    */
/**
     * @see org.apache.shiro.mgt.SecurityManager
     * @return
     *//*

    @Bean(name="securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm());
        manager.setCacheManager(redisCacheManager());
        manager.setSessionManager(defaultWebSessionManager());
        return manager;
    }

    */
/**
     * @see DefaultWebSessionManager
     * @return
     *//*

    @Bean(name="sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(redisCacheManager());
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        //hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    */
/**
     * @see UserRealm--->AuthorizingRealm
     * @return
     *//*

    @Bean
    @DependsOn(value={"lifecycleBeanPostProcessor", "shrioRedisCacheManager"})
    public UserRealm userRealm() {
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
        return new URLPermissionsFilter();
    }

    @Bean(name="shrioRedisCacheManager")
    @DependsOn(value="redisTemplate")
    public ShrioRedisCacheManager redisCacheManager() {
        ShrioRedisCacheManager cacheManager = new ShrioRedisCacheManager(redisTemplate());
        return cacheManager;
    }

    @Bean(name="redisTemplate")
    @DependsOn("redisConnectionFactory")
    public RedisTemplate<byte[], Object> redisTemplate() {
        RedisTemplate<byte[], Object> template = new RedisTemplate<>();
        //template.setConnectionFactory(connectionFactoryS());
        template.setConnectionFactory(RedisCacheConfig.jedisConnectionFactory);
        return template;
    }

    */
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
    }*//*


    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}*/
