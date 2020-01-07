package com.nsc.Amoski.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsc.Amoski.util.StringUtils;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        StringUtils.redisDatabase=this.redisDatabase;
        StringUtils.redisTimeOut=this.redisTimeOut;
        StringUtils.redisPoolMaxIdle=this.redisPoolMaxIdle;
        StringUtils.redisPoolMinIdle=this.redisPoolMinIdle;
        StringUtils.redisPoolMaxTotal=this.redisPoolMaxTotal;
        StringUtils.redisPoolMaxMaxActive=this.redisPoolMaxMaxActive;
        StringUtils.redisClusterNodes=this.redisClusterNodes;

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
        jedisConnectionFactory=redisConnectionFactory;
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory cf) {
        logger.info("进入redisTemplate");
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化

/*        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);*/

        // 设置value的序列化规则和 key的序列化规则
//        ClassLoader classLoader = this.getClass().getClassLoader();
        redisTemplate.setValueSerializer(new MyRedisSerializer());
        redisTemplate.setKeySerializer(new MyRedisSerializer());

        /*redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());*/

        /*redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());*/


        /*redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());*/


        redisTemplate.afterPropertiesSet();




        return redisTemplate;
    }

    /*@Bean
    public RedisTemplate<String, String> StringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        setSerializer(template); //设置序列化工具，这样ReportBean不需要实现Serializable接口
        template.afterPropertiesSet();
        return template;
    }*/

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        logger.info("进入cacheManager");
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(Integer.parseInt(redisTimeOut));
        Map<String,Long> expiresMap=new HashMap<>();
        expiresMap.put("user",3600L*72);
        expiresMap.put("user",3600L*24);
        cacheManager.setExpires(expiresMap);
        return cacheManager;
    }

    @Bean
    public KeyGenerator customKeyGenerator() {
        logger.info("进入customKeyGenerator");
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                logger.info("进入generate");
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






/*    private Logger log= LoggerFactory.getLogger(RedisCacheConfig.class);

    public static JedisConnectionFactory jedisConnectionFactory;

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public RedisClusterConfiguration getRedisCluster() {
        StringUtils.redisDatabase=redisProperties.getRedisDatabase();
        StringUtils.redisTimeOut=redisProperties.getCommandTimeout()+"";
        StringUtils.redisPoolMaxIdle=redisProperties.getMaxIdle()+"";
        StringUtils.redisPoolMinIdle=redisProperties.getMinIdle()+"";
        StringUtils.redisPoolMaxTotal=redisProperties.getMaxTotal();
        StringUtils.redisPoolMaxMaxActive=redisProperties.getMaxActive()+"";
        StringUtils.redisClusterNodes=redisProperties.getNodes();

        log.info(">>>>>>>>>>>>>...redisProperties"+redisProperties);
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        Set<RedisNode> jedisClusterNodes = new HashSet<RedisNode>();
        String [] nodeArr=redisProperties.getNodes().split(",");
        for(int i=0;i<nodeArr.length;i++){
            String [] singNodeArr=nodeArr[i].split(":");
            log.info(">>>>>>all redis nodes>>>>>i:"+i+">>>>>>..singNodeArr:"+singNodeArr);
            jedisClusterNodes.add(new RedisNode(singNodeArr[0], Integer.parseInt(singNodeArr[1])));
        }
        redisClusterConfiguration.setClusterNodes(jedisClusterNodes);
        return redisClusterConfiguration;
    }

    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxActive());
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisProperties.getMinIdle());
        jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(redisProperties.getTestOnBorrow()));
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWait());
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration, JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration);
        redisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory = redisConnectionFactory;
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        *//*redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());*//*

        redisTemplate.setValueSerializer(new MyRedisSerializer());
        redisTemplate.setKeySerializer(new MyRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(redisProperties.getDefaultTime());//默认1小时
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
    }*/
}
