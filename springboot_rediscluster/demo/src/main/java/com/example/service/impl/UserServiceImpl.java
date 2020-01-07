package com.example.service.impl;

import com.example.dao.UserDao;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * Created by Hai on 2018/1/18.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private UserDao userDao;

    @Override
    public void save(User user) {
        boolean flag = userDao.insert(user) > 0 ;
        /*if (flag) {
            valOps.set("user_"+user.getId(), user);
        }*/
    }

    @Override
    public User getUser(String id){
        return userDao.selectByPrimaryKey(Integer.valueOf(id));
    }

    @Override
    public boolean updateByPrimaryKey(User user) {
        return userDao.updateByPrimaryKey(user) > 0;
    }

    @Override
    public boolean deleteByPrimaryKey(String id) {
        return userDao.deleteByPrimaryKey(Integer.valueOf(id)) > 0;
    }
}
