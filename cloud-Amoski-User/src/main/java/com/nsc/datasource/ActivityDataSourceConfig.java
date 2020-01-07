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
public class ActivityDataSourceConfig {

    //oracle.jdbc.OracleDriver
    private Logger logger = LoggerFactory.getLogger(ActivityDataSourceConfig.class);
    @Value("${spring.profiles.active}")
    private String profiles;
    //链接字符串
    @Value("${spring.dsactivity.url}")
    public String url;
    //登入用户名
    @Value("${spring.dsactivity.username}")
    public String username;
    //登入密码
    @Value("${spring.dsactivity.password}")
    public String password;
    //驱动标识符
    @Value("${spring.dsactivity.driver-class-name}")
    public String driverClassName;
    //以毫秒表示的空闲对象驱逐进程由运行状态进入休眠状态的数值。值为非正数时表示不运行任何空闲对象驱逐进程。
    @Value("${spring.dsactivity.max-active}")
    public Integer maxActive;
    //池中最小空闲连接数，当连接数少于此值时，池会创建连接来补充到该值的数量
    @Value("${spring.dsactivity.max-idle}")
    public Integer maxIdle;
    //以毫秒表示的当连接池中没有可用连接时等待可用连接返回的时间，超时则抛出异常，值为-1时无限期等待。
    @Value("${spring.dsactivity.min-idle}")
    public Integer minIdle;
    //池中 工作连接的最大个数，此值为非正数是表述不限制
    @Value("${spring.dsactivity.initial-size}")
    public Integer initialSize;
    //（字符串）在将池中连接返回给调用者之前，用于验证这些连接的 SQL 查询。如果指定该值，则该查询不必返回任何数据，只是不抛出SQLException 异常。默认为 null。实例值为：SELECT 1（MySQL） select 1 from dual
    @Value("${spring.dsactivity.validation-query}")
    public String validationQuery;
    //（布尔值）默认值为 false。从池中借出对象之前，是否对其进行验证。如果对象验证失败，将其从池中清除，再接着去借下一个。注意：为了让 true 值生效
    @Value("${spring.dsactivity.test-on-borrow}")
    public Boolean testOnBorrow;
    //（布尔值）默认值为 false。将对象返回池之前，是否对齐进行验证。注意：为了让 true 值生效，validationQuery参数必须为非空字符串。
    @Value("${spring.dsactivity.test-on-return}")
    public Boolean testOnReturn;
    @Value("${spring.dsactivity.test-while-idle}")
    public Boolean testWhileIdle;
    //@Primary
    @Bean//(name = "dsactivity")
    //@Qualifier("dsactivity")
    public DataSource dsactivity(){
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

    @Bean(name="ActivityNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate ActivityNamedParameterJdbcTemplate(@Qualifier("dsactivity")DataSource Datasource) {
        return new NamedParameterJdbcTemplate(Datasource);
    }

    @Bean(name="jdbcActivityTemplate")
    public JdbcTemplate jdbcActivityTemplate(
            @Qualifier("dsactivity") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="ActivityTransactionManager")
    public DataSourceTransactionManager GoodsTransactionManager(@Qualifier("dsactivity")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }




















/*





    //@Primary
    @Bean(name = "entityManagerActivity")
    public EntityManager entityManagerActivity(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryActivity(builder).getObject().createEntityManager();
    }

    @Resource
    private Properties jpaProperties;

    *//**
     * 设置实体类所在位置
     *//*
    @Primary
    @Bean(name = "entityManagerFactoryActivity")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryActivity(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dsactivity())
                .packages("com.nsc.Amoski.entity")
                .persistenceUnit("primaryPersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    //@Primary
    @Bean(name = "transactionManagerActivity")
    public PlatformTransactionManager transactionManagerActivity(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryActivity(builder).getObject());
    }




    */









/*




    @Bean(name = "ActivityPersistenceUnitManager")
    public PersistenceUnitManager persistenceUnitManager(DataSource dataSource) {
        DefaultPersistenceUnitManager manager = new DefaultPersistenceUnitManager();
        //manager.setPersistenceXmlLocation("classpath:META-INF/pedal-test-persistence.xml");
        manager.setDefaultDataSource(dataSource);
        return manager;
    }
    @Bean(name = "ActivityJpaProperties")
    public JpaProperties orderJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "ActivityEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder ActivityEntityManagerFactoryBuilder(
            @Qualifier("ActivityJpaProperties") JpaProperties orderJpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(adapter,
                orderJpaProperties.getProperties(), persistenceUnitManager(dsactivity()));
    }

    @Primary
    @Bean(name = "ActivityEntityManagerPrimary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "ActivityEntityManagerFactoryPrimary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dsactivity())
                .properties(getVendorProperties(dsactivity()))
                .packages("com.didispace.domain.p") //设置实体类所在位置
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Primary
    @Bean(name = "ActivityTransactionManagerPrimary")
    public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
    }
*/




/*

    @Bean(name = "ActivityEntityManager")
    public EntityManager secondEntityManager(EntityManagerFactoryBuilder builder) {
        return secondEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "ActivityEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, String> map = new HashMap<>();
        map.put("hibernate.hbm2ddl.auto", "update");
        map.put("hibernate.show_sql", "true");

        return builder
                .dataSource(dsactivity())
                //.packages("com.nsc.AmoskiAcyivity.entity")
                .properties(map) // 设置实体类所在位置
                .persistenceUnit("GenericRepositoryActivityImpl").build();
                //.persistenceUnit("ActivityEntityManager")
                //.build();
    }

    @Bean(name = "ActivityTransactionManager")
    public PlatformTransactionManager secondTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(secondEntityManagerFactory(builder).getObject());
    }
*/





/*
    @Bean(name = "ActivityEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder ActivityEntityManagerFactoryBuilder(
            @Qualifier("ActivityJpaProperties") JpaProperties orderJpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(adapter,
                orderJpaProperties.getProperties(), persistenceUnitManager(dsactivity()));
    }

    @Bean(name = "ActivityPersistenceUnitManager")
    public PersistenceUnitManager persistenceUnitManager(DataSource dataSource) {
        DefaultPersistenceUnitManager manager = new DefaultPersistenceUnitManager();
        //manager.setPersistenceXmlLocation("classpath:META-INF/pedal-test-persistence.xml");
        manager.setDefaultDataSource(dataSource);
        return manager;
    }

    @Bean(name = "ActivityJpaProperties")
    public JpaProperties orderJpaProperties() {
        return new JpaProperties();
    }

    */
/**
     *
     * @param builder
     * @return
     *//*

    @Bean(name = "ActivityEntityManagerFactoryPrimary")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(@Qualifier("ActivityEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder) {
        return builder
                //设置数据源
                .dataSource(dsactivity())
                //设置数据源属性
                .properties(getVendorProperties(dsactivity()))
                //设置实体类所在位置.扫描所有带有 @Entity 注解的类
                .packages("com.nsc.Amoski.entity")
                // Spring会将EntityManagerFactory注入到Repository之中.有了 EntityManagerFactory之后,
                // Repository就能用它来创建 EntityManager 了,然后 EntityManager 就可以针对数据库执行操作
                //.persistenceUnit("GenericRepositoryGoodsImpl")
                .build();

    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return orderJpaProperties().getHibernateProperties(dataSource);
    }
*/





/*
    @Bean(name = "ActivityPersistenceUnitManager")
    public PersistenceUnitManager persistenceUnitManager(DataSource dataSource) {
        DefaultPersistenceUnitManager manager = new DefaultPersistenceUnitManager();
        //manager.setPersistenceXmlLocation("classpath:META-INF/pedal-test-persistence.xml");
        manager.setDefaultDataSource(dataSource);
        return manager;
    }
    @Bean(name = "ActivityJpaProperties")
    public JpaProperties orderJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "ActivityEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder ActivityEntityManagerFactoryBuilder(
            @Qualifier("ActivityJpaProperties") JpaProperties orderJpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(adapter,
                orderJpaProperties.getProperties(), persistenceUnitManager(dsactivity()));
    }


    @Bean(name = "ActivityEntityManager")
    public EntityManager secondEntityManager(
            @Qualifier("ActivityEntityManagerFactoryBuilder")EntityManagerFactoryBuilder builder) {
        return secondEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "ActivityEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            @Qualifier("ActivityEntityManagerFactoryBuilder")EntityManagerFactoryBuilder builder) {
        Map<String, String> map = new HashMap<>();
        map.put("hibernate.hbm2ddl.auto", "update");
        map.put("hibernate.show_sql", "true");
        LocalContainerEntityManagerFactoryBean build = builder.dataSource(dsactivity()).jta(true).build();
        return build;

    }*/

/*
    @Bean(name = "ActivityJpaProperties")
    public JpaProperties orderJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "ActivityEntityManagerSecondary")
    public EntityManager ActivityEntityManagerSecondary(@Qualifier("dsactivity")DataSource Datasource,EntityManagerFactoryBuilder builder) {
        return ActivityEntityManagerFactorySecondary(Datasource,builder).getObject().createEntityManager();
    }

    @Bean(name = "ActivityEntityManagerFactorySecondary")
    public LocalContainerEntityManagerFactoryBean ActivityEntityManagerFactorySecondary (@Qualifier("dsactivity")DataSource Datasource,EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(Datasource)
                .properties(getVendorProperties(Datasource))
                //.packages("com.nsc.Amoski.entity") //设置实体类所在位置
                //.persistenceUnit("ActivityTransactionManagerSecondary")
                .build();
    }


    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return orderJpaProperties().getHibernateProperties(dataSource);
    }

    @Bean(name = "ActivityTransactionManagerSecondary")
    PlatformTransactionManager ActivityTransactionManagerSecondary(@Qualifier("dsactivity")DataSource Datasource,EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(ActivityEntityManagerFactorySecondary(Datasource,builder).getObject());
    }*/






/*    @Bean(name = "activityEntityManager")
    public EntityManager activityEntityManager(){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dsactivity());
        InstrumentationLoadTimeWeaver InstrumentationLoadTimeWeaver = new InstrumentationLoadTimeWeaver();
        entityManagerFactory.setLoadTimeWeaver(InstrumentationLoadTimeWeaver);

        JpaVendorAdapter jpaProperties = new HibernateJpaVendorAdapter();
        ((HibernateJpaVendorAdapter) jpaProperties).setDatabasePlatform("ORACLE");
        ((HibernateJpaVendorAdapter) jpaProperties).setGenerateDdl(true);
        ((HibernateJpaVendorAdapter) jpaProperties).setShowSql(true);
        entityManagerFactory.setJpaVendorAdapter(jpaProperties);

        EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
        return entityManager;
    }*/
/*
    public LocalContainerEntityManagerFactoryBean getEntityManager(){

    }
*/
/*
    @Bean(name = "ActivityPersistenceUnitManager")
    public PersistenceUnitManager persistenceUnitManager(DataSource dataSource) {
        DefaultPersistenceUnitManager manager = new DefaultPersistenceUnitManager();
        //manager.setPersistenceXmlLocation("classpath:META-INF/pedal-test-persistence.xml");
        manager.setDefaultDataSource(dataSource);
        return manager;
    }
    @Bean(name = "ActivityJpaProperties")
    public JpaProperties orderJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "ActivityEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder orderEntityManagerFactoryBuilder(
            @Qualifier("ActivityJpaProperties") JpaProperties orderJpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(adapter,
                orderJpaProperties.getProperties(), persistenceUnitManager(dsactivity()));
    }


    @Bean(name = "ActivityEntityManager")
    public EntityManager secondEntityManager(
            @Qualifier("ActivityEntityManagerFactoryBuilder")EntityManagerFactoryBuilder builder) {
        return secondEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "ActivityEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            @Qualifier("ActivityEntityManagerFactoryBuilder")EntityManagerFactoryBuilder builder) {
        Map<String, String> map = new HashMap<>();
        map.put("hibernate.hbm2ddl.auto", "update");
        map.put("hibernate.show_sql", "true");
        return builder.dataSource(dsactivity()).jta(true).build();

    }*/

    /*@Bean(name = "ActivityTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("ActivityEntityManagerFactoryBuilder")EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(secondEntityManagerFactory(builder).getObject());
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
