package com.nsc.datasource;




import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

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
    @Primary
    @Bean//(name="dsuser")
    //@Qualifier("dsuser")
    public DataSource dsuser(){
        String osName = System.getProperty("os.name");
        DataSource ds = new DataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        logger.info("连接路径："+url);
        logger.info("用户名："+username);
        logger.info("配置文件中password"+password);
        ds.setPassword(StringUtils.decryptBASE64(password));
        logger.info("解密后的密码"+ StringUtils.decryptBASE64(password));
        ds.setDriverClassName(driverClassName);
        logger.info("加载路径"+driverClassName);
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
            con=DriverManager.getConnection(ds.getPoolProperties().getUrl(),ds.getPoolProperties().getUsername(),ds.getPoolProperties().getPassword());
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
    public JdbcTemplate jdbcTemplate(
            @Qualifier("dsuser") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="UserTransactionManager")
    public DataSourceTransactionManager GoodsTransactionManager(@Qualifier("dsuser")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }




















/*


    //@Primary
    @Bean(name = "entityManagerUser")
    public EntityManager entityManagerUser(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryUser(builder).getObject().createEntityManager();
    }

    @Resource
    private Properties jpaProperties;

    *//**
     * 设置实体类所在位置
     *//*
    @Primary
    @Bean(name = "entityManagerFactoryUser")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryUser(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dsuser())
                .packages("com.nsc.Amoski.entity")
                .persistenceUnit("primaryPersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    //@Primary
    @Bean(name = "transactionManagerUser")
    public PlatformTransactionManager transactionManagerUser(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryUser(builder).getObject());
    }*/















/*
    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource secondaryDataSource;

    @Bean(name = "entityManagerSecondary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactorySecondary(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactorySecondary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(secondaryDataSource)
                .properties(getVendorProperties(secondaryDataSource))
                .packages("per.yhq.domain.secondary") //设置实体类所在位置
                .persistenceUnit("secondaryPersistenceUnit")
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerSecondary")
    PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactorySecondary(builder).getObject());
    }
*/


/*
    @Bean(name = "JpaProperties")
    public JpaProperties orderJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "EntityManagerSecondary")
    public EntityManager goodsEntityManagerSecondary(@Qualifier("dsuser")DataSource Datasource , EntityManagerFactoryBuilder builder) {
        return goodsEntityManagerFactorySecondary(Datasource,builder).getObject().createEntityManager();
    }

    @Bean(name = "EntityManagerFactorySecondary")
    public LocalContainerEntityManagerFactoryBean goodsEntityManagerFactorySecondary (@Qualifier("dsuser")DataSource Datasource , EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dsuser())
                .properties(getVendorProperties(dsuser()))
                //.packages("com.nsc.Amoski.entity") //设置实体类所在位置
                //.persistenceUnit("DataSourceConfig")
                .build();
    }


    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return orderJpaProperties().getHibernateProperties(dataSource);
    }

    @Bean(name = "TransactionManagerSecondary")
    PlatformTransactionManager transactionManagerSecondary(@Qualifier("dsuser")DataSource Datasource , EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(goodsEntityManagerFactorySecondary(Datasource,builder).getObject());
    }*/

/*
*
     * 注册一个：filterRegistrationBean
     *
     * @return
    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
*/


}
