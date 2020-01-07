package com.nsc.datasource;


import com.nsc.Amoski.util.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class GoodsDataSourceConfig {

    //oracle.jdbc.OracleDriver
    private Logger logger = LoggerFactory.getLogger(GoodsDataSourceConfig.class);
    @Value("${spring.profiles.active}")
    private String profiles;
    //链接字符串
    @Value("${spring.dsgoods.url}")
    public String url;
    //登入用户名
    @Value("${spring.dsgoods.username}")
    public String username;
    //登入密码
    @Value("${spring.dsgoods.password}")
    public String password;
    //驱动标识符
    @Value("${spring.dsgoods.driver-class-name}")
    public String driverClassName;
    //以毫秒表示的空闲对象驱逐进程由运行状态进入休眠状态的数值。值为非正数时表示不运行任何空闲对象驱逐进程。
    @Value("${spring.dsgoods.max-active}")
    public Integer maxActive;
    //池中最小空闲连接数，当连接数少于此值时，池会创建连接来补充到该值的数量
    @Value("${spring.dsgoods.max-idle}")
    public Integer maxIdle;
    //以毫秒表示的当连接池中没有可用连接时等待可用连接返回的时间，超时则抛出异常，值为-1时无限期等待。
    @Value("${spring.dsgoods.min-idle}")
    public Integer minIdle;
    //池中 工作连接的最大个数，此值为非正数是表述不限制
    @Value("${spring.dsgoods.initial-size}")
    public Integer initialSize;
    //（字符串）在将池中连接返回给调用者之前，用于验证这些连接的 SQL 查询。如果指定该值，则该查询不必返回任何数据，只是不抛出SQLException 异常。默认为 null。实例值为：SELECT 1（MySQL） select 1 from dual
    @Value("${spring.dsgoods.validation-query}")
    public String validationQuery;
    //（布尔值）默认值为 false。从池中借出对象之前，是否对其进行验证。如果对象验证失败，将其从池中清除，再接着去借下一个。注意：为了让 true 值生效
    @Value("${spring.dsgoods.test-on-borrow}")
    public Boolean testOnBorrow;
    //（布尔值）默认值为 false。将对象返回池之前，是否对齐进行验证。注意：为了让 true 值生效，validationQuery参数必须为非空字符串。
    @Value("${spring.dsgoods.test-on-return}")
    public Boolean testOnReturn;
    @Value("${spring.dsgoods.test-while-idle}")
    public Boolean testWhileIdle;
    //@Primary
    @Bean//(name = "dsgoods")
    //@Qualifier("dsgoods")
    public DataSource dsgoods(){
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

    @Bean(name="GoodsNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate GoodsNamedParameterJdbcTemplate(@Qualifier("dsgoods")DataSource Datasource) {
        return new NamedParameterJdbcTemplate(Datasource);
    }

    @Bean(name="jdbcGoodsTemplate")
    public JdbcTemplate jdbcGoodsTemplate(
            @Qualifier("dsgoods") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="GoodsTransactionManager")
    public DataSourceTransactionManager GoodsTransactionManager(@Qualifier("dsgoods")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }























/*

    //@Primary
    @Bean(name = "entityManagerGoods")
    public EntityManager entityManagerGoods(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryGoods(builder).getObject().createEntityManager();
    }

    @Resource
    private Properties jpaProperties;

    *//**
     * 设置实体类所在位置
     *//*
    @Primary
    @Bean(name = "entityManagerFactoryGoods")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryGoods(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dsgoods())
                .packages("com.nsc.Amoski.entity")
                .persistenceUnit("primaryPersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    //@Primary
    @Bean(name = "transactionManagerGoods")
    public PlatformTransactionManager transactionManagerGoods(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryGoods(builder).getObject());
    }*/
}
