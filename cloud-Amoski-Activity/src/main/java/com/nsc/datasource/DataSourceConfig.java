package com.nsc.datasource;




import com.nsc.Amoski.controller.ActivityServerBaseController;
import com.nsc.Amoski.util.RegUtil;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.nsc.Amoski.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DataSourceConfig {
    //oracle.jdbc.OracleDriver

    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
    @Value("${spring.profiles.active}")
    private String profiles;
    //链接字符串
    @Value("${spring.dsuser.url}")
    public String url;
    //登入用户名
    @Value("${spring.dsuser.username}")
    public String username;
    //登入密码
    @Value("${spring.dsuser.password}")
    public String password;
    //驱动标识符
    @Value("${spring.dsuser.driver-class-name}")
    public String driverClassName;
    //以毫秒表示的空闲对象驱逐进程由运行状态进入休眠状态的数值。值为非正数时表示不运行任何空闲对象驱逐进程。
    @Value("${spring.dsuser.max-active}")
    public Integer maxActive;
    //池中最小空闲连接数，当连接数少于此值时，池会创建连接来补充到该值的数量
    @Value("${spring.dsuser.max-idle}")
    public Integer maxIdle;
    //以毫秒表示的当连接池中没有可用连接时等待可用连接返回的时间，超时则抛出异常，值为-1时无限期等待。
    @Value("${spring.dsuser.min-idle}")
    public Integer minIdle;
    //池中 工作连接的最大个数，此值为非正数是表述不限制
    @Value("${spring.dsuser.initial-size}")
    public Integer initialSize;
    //（字符串）在将池中连接返回给调用者之前，用于验证这些连接的 SQL 查询。如果指定该值，则该查询不必返回任何数据，只是不抛出SQLException 异常。默认为 null。实例值为：SELECT 1（MySQL） select 1 from dual
    @Value("${spring.dsuser.validation-query}")
    public String validationQuery;
    //（布尔值）默认值为 false。从池中借出对象之前，是否对其进行验证。如果对象验证失败，将其从池中清除，再接着去借下一个。注意：为了让 true 值生效
    @Value("${spring.dsuser.test-on-borrow}")
    public Boolean testOnBorrow;
    //（布尔值）默认值为 false。将对象返回池之前，是否对齐进行验证。注意：为了让 true 值生效，validationQuery参数必须为非空字符串。
    @Value("${spring.dsuser.test-on-return}")
    public Boolean testOnReturn;
    @Value("${spring.dsuser.test-while-idle}")
    public Boolean testWhileIdle;
    @Value("${upload.filePath}")
    public String filePath;
    @Value("${upload.webWechatLoginPageUrl}")
    public String webWechatLoginPageUrl;
    @Value("${upload.attentionWechatUrl}")
    public String attentionWechatUrl;
    @Bean
    @Primary
    public DataSource dsuser(){
        StringUtils.setFilePath(filePath);
        logger.info(">>>>>>>>>>>>source filePath:"+filePath+">>>>> dataSource baseUrl:"+StringUtils.getFilePath());
        /*if(RegUtil.getSingleton().isNull(webWechatLoginPageUrl)){//默认
            ActivityServerBaseController.webWechatLoginPageUrl="";
        }else{
            ActivityServerBaseController.webWechatLoginPageUrl=webWechatLoginPageUrl;
        }*/
        ActivityServerBaseController.webWechatLoginPageUrl=webWechatLoginPageUrl;
        logger.info(">>>>>>>>webWechatLoginPageUrl:"+webWechatLoginPageUrl+">>>>>>>>>>>>>>>controll webWechatLoginPageUrl:"+ActivityServerBaseController.webWechatLoginPageUrl);
        ActivityServerBaseController.attentionWechatUrl=attentionWechatUrl;
        logger.info(">>>>>>>>attentionWechatUrl:"+attentionWechatUrl+">>>>>>>>>>>>>>>controll attentionWechatUrl:"+ActivityServerBaseController.attentionWechatUrl);
        String osName = System.getProperty("os.name");
        DataSource ds = new DataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        logger.info("配置文件中password"+password);
        ds.setPassword(StringUtils.decryptBASE64(password));
        logger.info("解密后的密码"+ StringUtils.decryptBASE64(password));
        ds.setDriverClassName(driverClassName);
        isConnectSuccess(ds);
        ds.setMaxActive(maxActive);
        ds.setMaxIdle(maxIdle);
        ds.setMinIdle(minIdle);
        ds.setInitialSize(initialSize);
        ds.setValidationQuery(validationQuery);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setTestOnReturn(testOnReturn);
        ds.setTestWhileIdle(testWhileIdle);
        return ds;
    }

    public Boolean isConnectSuccess(DataSource ds){
        logger.info("---------------------进入数据库连接方法--------------------------");
        //加载驱动（导入数据库的驱动包）
        Connection con=null;
        try {
            logger.info("加载oracle数据加载类:"+ds.getPoolProperties().getDriverClassName());
            Class.forName(ds.getPoolProperties().getDriverClassName());
            logger.info("连接oracle数据库："+ds.getPoolProperties().getUrl()+","+ds.getPoolProperties().getUsername()+","+ds.getPoolProperties().getPassword());
            con= DriverManager.getConnection(ds.getPoolProperties().getUrl(),ds.getPoolProperties().getUsername(),ds.getPoolProperties().getPassword());
        } catch (Exception e) {
            logger.info("oracle数据库连接失败：ERROR");
            return false;
        }
        logger.info("oracle数据库连接成功：Success");
        return true;
    }
    @Bean
    public NamedParameterJdbcTemplate NamedParameterJdbcTemplate(@Qualifier("dsuser")DataSource Datasource) {
        return new NamedParameterJdbcTemplate(Datasource);
    }

    @Bean
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("dsuser") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

/**
     * 注册一个：filterRegistrationBean
     *
     * @return*//*
    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }*/




}
