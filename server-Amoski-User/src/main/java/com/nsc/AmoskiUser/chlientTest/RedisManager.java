/**
 * Copyright 2017 Inc.
 **/
package com.nsc.AmoskiUser.chlientTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;


import java.util.TreeSet;

/**
 * @author zhangshaoyong
 */
public class RedisManager {
  Logger logger = LoggerFactory.getLogger(RedisManager.class);

  private JedisCluster jedisCluster;

  private RedisOperator redisOperator;

  private int expire = 0;

  public RedisOperator getRedisOperator() {
    return redisOperator;
  }

  public void setRedisOperator(RedisOperator redisOperator) {
    this.redisOperator = redisOperator;
  }

  public int getExpire() {
    return expire;
  }

  public void setExpire(int expire) {
    this.expire = expire;
  }

  public JedisCluster getJedisCluster() {
    return jedisCluster;
  }

  public void setJedisCluster(JedisCluster jedisCluster) {
    this.jedisCluster = jedisCluster;
  }

  public RedisManager() {

  }

  /**
   * 初始化方法
   */
  public void init() {
    //logger.info("RedisManager==>info");
  }

  /**
   * get value from redis
   *
   * @param key
   * @return
   */
  public byte[] get(byte[] key) {
    //logger.info("RedisManager==>get");
    byte[] value = null;
    value = jedisCluster.get(key);
    return value;
  }

  /**
   * set
   *
   * @param key
   * @param value
   * @return
   */
  public byte[] set(byte[] key, byte[] value) {
    //logger.info("RedisManager==>set");
    jedisCluster.set(key, value);
    if (this.expire != 0) {
      jedisCluster.expire(key, this.expire);
    }
    return value;
  }

  /**
   * set
   *
   * @param key
   * @param value
   * @param expire
   * @return
   */
  public byte[] set(byte[] key, byte[] value, int expire) {
    //logger.info("RedisManager==>set");
    jedisCluster.set(key, value);
    if (expire != 0) {
      jedisCluster.expire(key, expire);
    }
    return value;
  }

  /**
   * del
   *
   * @param key
   */
  public void del(byte[] key) {
    //logger.info("RedisManager==>del");
    jedisCluster.del(key);
  }

  /**
   * flush
   */
  public void flushDB() throws Exception {
    //logger.info("RedisManager==>flushDB");
    redisOperator.flushDB();
  }

  /**
   * size
   */
  public Long dbSize(String pattern) {
    Long dbSize = 0L;
    TreeSet<String> keys = null;
    try {
      keys = keys(pattern);
    } catch (Exception e) {
      e.printStackTrace();
    }
    dbSize = Long.valueOf(keys.size());
    return dbSize;
  }

  /**
   * keys
   *
   * @param pattern
   * @return
   */
  public TreeSet<String> keys(String pattern) throws Exception {
    TreeSet<String> treeSet = new TreeSet<String>();
    treeSet = redisOperator.keys(pattern);
    return treeSet;
  }

}
