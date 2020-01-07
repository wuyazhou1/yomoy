package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBootDemo
 * Created by hai on 2018年1月15日 14:27:32
 */
@RestController
public class RedisDataController {
    /*
        spring boot集成redis进行数据缓存功能
        @Cacheable	表明Spring在调用方法之前，首先应该在缓存中查找方法的返回值。如果这个值能够找到，就会返回缓存的值。否则的话，这个方法就会被调用，返回值会放到缓存之中
        @cacheput	表明Spring应该将方法的返回值放到缓存中。在方法的调用前并不会 检查缓存，方法始终都会被调用
        @cacheevict	表明Spring应该在缓存中清除一个或多个条目
        @caching	这是一个分组的注解，能够同时应用多个其他的缓存注解
        @cacheconfig	可以在类层级配置一些共用的缓存配置
    */

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 保存user信息，数据库保存成功后会存储在redis中
     * @param id
     * @param name
     * @param age
     */
    @CachePut(value = "user_",key = "#root.caches[0].name+#id")
    @RequestMapping("/set")
    public void set(String id,String name,int age){
        User user = new User(age,id,name);
        userService.save(user);
    }

    /**
     * 指定id的数据
     * @param id
     * @return
     */
    @Cacheable(value = "user_",key = "#root.caches[0].name+#id")
    @RequestMapping("get/{id}")
    public User get(@PathVariable("id") String id){
        User user = userService.getUser(id);
        return user;
    }

    /**
     *
     * @param user
     */
    @RequestMapping("/updateUser")
    @CachePut(value = "user_",key = "#root.caches[0].name+#user.id")
    public void updateUser(User user){
        userService.updateByPrimaryKey(user);
    }

    /**
     *
     * @param id
     */
    @RequestMapping("/delete/{id}")
    @CacheEvict(value = "user_",key = "#root.caches[0].name+#id")
    public void delete(@PathVariable("id") String id){
        userService.deleteByPrimaryKey(id);
    }
}