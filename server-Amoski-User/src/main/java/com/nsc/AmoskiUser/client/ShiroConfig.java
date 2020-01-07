package com.nsc.AmoskiUser.client;


/**
 * @Auther: cpb
 * @Date: 2018/8/10 11:32
 * @Description:
 */
//@Configuration
public class ShiroConfig {
    /*@Qualifier("cacheManager")
    //@Order(2)
    private RedisCacheManager cacheManager;*/

/*    //设置shiro拦截器
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断
        //会员校验方法
        filterChainDefinitionMap.put("/MemberWeiXinManage/WeiXinRegister", "anon");//会员微信注册接口
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
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/AMOSKI/loginNameUser");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/AMOSKI/XiTongMenu");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    *//**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     * @return
     *//*
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        //hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    public UserRealm ShiroConfig(){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }


    @Bean
    //@Order(3)
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        // 自定义session管理使用redis
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(ShiroConfig());
        //配置记住我
        securityManager.setRememberMeManager(rememberMeManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(ehCacheManager());

        //配置自定义session管理，使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager em = new EhCacheManager();
        //将ehcacheManager转换成shiro包装后的ehcacheManager对象
        em.setCacheManager(CacheManager.create());
        //em.setCacheManagerConfigFile("classpath:ehcache.xml");
        return em;
    }

    *//**
     * 配置session监听
     * @return
     *//*
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }

    *//**
     * 配置会话管理器，设定会话超时及保存
     * @return
     *//*
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(ehCacheManager());

        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(1800000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        sessionManager.setSessionValidationInterval(3600000);
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;

    }

    *//**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     *//*
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //使用ehCacheManager
        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
        //设置session缓存的名字 默认为 shiro-activeSessionCache
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        //sessionId生成器
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }




    *//**
     * 配置会话ID生成器
     * @return
     *//*
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    *//**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     *//*
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
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

    *//**
     * cookie管理对象;记住我功能,rememberMe管理器
     * @return
     *//*
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey("AmoskiUser".getBytes());
        return cookieRememberMeManager;
    }
    *//**
     * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
     * @return
     *//*
    @Bean
    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }


    *//*启动shiro在ioc容器中的注解，只有在使用
* 开启 Shiro 的注解功能 (如 @RequiresRoles,@RequiresPermissions),需借助 SpringAOP 扫描使 用 Shiro 注 解 的 类 , 并 在 必 要 时 进 行 安 全 逻 辑 验 证 , 需 要 配 置 两 个 bean(DefaultAdvisorAutoProxyCreator(可选 ) 和 AuthorizationAttributeSourceAdvisor) 实现此功 能。
Spring Boot系列 安全框架Apache Shiro基本功能*//*
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    *//**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     *//*
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //非必要
    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException","403");
        r.setExceptionMappings(mappings);  // None by default
//        r.setDefaultErrorView("error");    // No default
//        r.setExceptionAttribute("ex");     // Default is "exception"
        //r.setWarnLogCategory("example.MvcLogger");     // No default
        return r;
    }


    //thymeleaf的shiro标签库
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }*/

































/*    public static List list = new ArrayList(){{
        this.add("getRestTemplate");
        this.add("LoginUserCheck");
        this.add("WeiXinRegister");
        this.add("MemberLogin");
        this.add("checkedLoginRepeat");
        this.add("TelRegister");
        this.add("UpdateMemberView");
        this.add("findMemberView");
    }};


    //设置shiro拦截器
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断
        //会员校验方法
        filterChainDefinitionMap.put("/MemberWeiXinManage/WeiXinRegister", "anon");//会员微信注册接口
        filterChainDefinitionMap.put("/MemberWeiXinManage/MemberLogin", "anon");//会员登入接口
        filterChainDefinitionMap.put("/MemberWeiXinManage/checkedLoginRepeat", "anon");//验证会员手机号码或账号是否重复
        filterChainDefinitionMap.put("/MemberWeiXinManage/TelRegister", "anon");//会员手机号注册
        filterChainDefinitionMap.put("/MemberWeiXinManage/UpdateMemberView", "anon");//修改会员信息
        filterChainDefinitionMap.put("/MemberWeiXinManage/findMemberView", "anon");//获取会员信息
        filterChainDefinitionMap.put("/css/**", "anon");//前端css样式
        filterChainDefinitionMap.put("/images/**", "anon");//前端所有图片
        filterChainDefinitionMap.put("/js/**", "anon");//前端所有js

        filterChainDefinitionMap.put("/AMOSKI/LoginUserCheck.html", "anon");//用户登入校验
        //filterChainDefinitionMap.put("/AMOSKI/loginNameUser", "anon");//前端用户登入页面
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/AMOSKI/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");


        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/AMOSKI/loginNameUser");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/AMOSKI/XiTongMenu");
        //未授权界面;
       // shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //解决登录成功后无法跳转到successUrl的问题
        Map<String, Filter> map = new LinkedHashMap<String, Filter>();
        map.put("authc", new MyFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(map);
        return shiroFilterFactoryBean;
    }
    *//**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     * @return
     *//*
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        //hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }


    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    *//*启动shiro在ioc容器中的注解，只有在使用
* 开启 Shiro 的注解功能 (如 @RequiresRoles,@RequiresPermissions),需借助 SpringAOP 扫描使 用 Shiro 注 解 的 类 , 并 在 必 要 时 进 行 安 全 逻 辑 验 证 , 需 要 配 置 两 个 bean(DefaultAdvisorAutoProxyCreator(可选 ) 和 AuthorizationAttributeSourceAdvisor) 实现此功 能。
Spring Boot系列 安全框架Apache Shiro基本功能*//*
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    *//**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     *//*
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

*//*    //非必要
    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver
    createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException","403");
        r.setExceptionMappings(mappings);  // None by default
//        r.setDefaultErrorView("error");    // No default
//        r.setExceptionAttribute("ex");     // Default is "exception"
        //r.setWarnLogCategory("example.MvcLogger");     // No default
        return r;
    }*//*


    //thymeleaf的shiro标签库
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }*/

}
