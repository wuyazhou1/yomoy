package com.nsc.Amoski.chlientTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: wusiyao
 * @date:2019-04-08
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
    private Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);
    public static JedisConnectionFactory jedisConnectionFactory;
    @Value("${spring.redis.database}")
    private String redisDatabase;
    @Value("${spring.redis.timeout}")
    private String redisTimeOut;
    @Value("${spring.redis.pool.max-idle}")
    private String redisPoolMaxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private String redisPoolMinIdle;
    @Value("${spring.redis.pool.max-total}")
    private String redisPoolMaxTotal;
    @Value("${spring.redis.pool.max-max-active}")
    private String redisPoolMaxMaxActive;
    @Value("${spring.redis.cluster.nodes}")
    private String redisClusterNodes;
    @Bean
    public RedisClusterConfiguration getRedisCluster() {
        logger.info("进入getRedisCluster");
        StringUtil.redisDatabase=this.redisDatabase;
        StringUtil.redisTimeOut=this.redisTimeOut;
        StringUtil.redisPoolMaxIdle=this.redisPoolMaxIdle;
        StringUtil.redisPoolMinIdle=this.redisPoolMinIdle;
        StringUtil.redisPoolMaxTotal=this.redisPoolMaxTotal;
        StringUtil.redisPoolMaxMaxActive=this.redisPoolMaxMaxActive;
        StringUtil.redisClusterNodes=this.redisClusterNodes;
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        String[] clusterNodes = redisClusterNodes.split(",");
        Set<RedisNode> jedisClusterNodes = new HashSet<RedisNode>();
        for(String node:clusterNodes){
            String[] ipPort = node.split(":");
            logger.info("redis初始化"+ipPort[0]+":"+ipPort[1]);
            jedisClusterNodes.add(new RedisNode(ipPort[0], Integer.parseInt(ipPort[1])));
        }
        redisClusterConfiguration.setClusterNodes(jedisClusterNodes);
        return redisClusterConfiguration;
    }

    @Bean
    @DependsOn("getRedisCluster")
    public JedisPoolConfig getJedisPoolConfig() {

        logger.info("进入getJedisPoolConfig");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.parseInt(redisPoolMaxTotal));
        jedisPoolConfig.setMaxIdle(Integer.parseInt(redisPoolMaxIdle));
        jedisPoolConfig.setMinIdle(Integer.parseInt(redisPoolMinIdle));
        return jedisPoolConfig;
    }

    @Bean
    @DependsOn("getJedisPoolConfig")
    public JedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration, JedisPoolConfig jedisPoolConfig) {
        logger.info("进入redisConnectionFactory");
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration);
        redisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory = redisConnectionFactory;
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory cf) {
        logger.info("进入redisTemplate");
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        /*Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);*/

        // 设置value的序列化规则和 key的序列化规则
//        ClassLoader classLoader = this.getClass().getClassLoader();
        redisTemplate.setValueSerializer(new MyRedisSerializer());
        redisTemplate.setKeySerializer(new MyRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        logger.info("进入cacheManager");
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(Integer.parseInt(redisTimeOut));
        return cacheManager;
    }

    @Bean
    public KeyGenerator customKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName());
                sb.append(method.getName());
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                System.out.println(sb.toString());
                return sb.toString();
            }
        };
    }
}