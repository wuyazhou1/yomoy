package com.nsc.datasource;
/*

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import java.util.Map;




@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryGoods",
        transactionManagerRef="transactionManagerGoods",
        basePackages= { "com.nsc.AmoskiGoods.Dao.*" }) //设置Repository所在位置
public class GoodsConfig {

    @Autowired
    @Qualifier("dsgoods")
    private DataSource dsgoods;

    @Bean(name = "entityManagerGoods")
    public EntityManager entityManagerGoods(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryGoods(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryGoods")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryGoods (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dsgoods)
                .properties(getVendorProperties(dsgoods))
                .packages("com.nsc.Amoski.entity") //设置实体类所在位置
                .persistenceUnit("goodsPersistenceUnit")
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerGoods")
    PlatformTransactionManager transactionManagerGoods(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryGoods(builder).getObject());
    }
}
*/
