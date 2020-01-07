/**
 * Copyright 2017 Inc.
 **/
package com.nsc.AmoskiUser.chlientTest;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author zhangshaoyong
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

  private Resource addressConfig;
  private String addressKeyPrefix;
  private JedisCluster jedisCluster;
  private Integer timeout;
  private Integer maxRedirections;
  private GenericObjectPoolConfig genericObjectPoolConfig;
  private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
  private Set<HostAndPort> haps;

  public void setHaps(Set<HostAndPort> haps) {
    this.haps = haps;
  }

  public JedisClusterFactory() {
  }

  public JedisCluster getObject() throws Exception {
    return this.jedisCluster;
  }
  public void setObject(JedisCluster JedisCluster) {
    this.jedisCluster=JedisCluster;
  }

  public Class<? extends JedisCluster> getObjectType() {
    return this.jedisCluster != null?this.jedisCluster.getClass():JedisCluster.class;
  }

  public boolean isSingleton() {
    return true;
  }
  public void afterPropertiesSet() throws Exception {
    this.jedisCluster = new JedisCluster(haps, this.timeout.intValue(), this.maxRedirections.intValue(), this.genericObjectPoolConfig);
  }

  public void setAddressConfig(Resource addressConfig) {
    this.addressConfig = addressConfig;
  }

  public void setTimeout(int timeout) {
    this.timeout = Integer.valueOf(timeout);
  }

  public void setMaxRedirections(int maxRedirections) {
    this.maxRedirections = Integer.valueOf(maxRedirections);
  }

  public void setAddressKeyPrefix(String addressKeyPrefix) {
    this.addressKeyPrefix = addressKeyPrefix;
  }

  public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
    this.genericObjectPoolConfig = genericObjectPoolConfig;
  }

}
