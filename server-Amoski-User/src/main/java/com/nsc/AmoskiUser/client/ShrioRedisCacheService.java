package com.nsc.AmoskiUser.client;

import com.google.common.collect.Sets;
import com.nsc.AmoskiUser.service.LoginUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

public class ShrioRedisCacheService<K, V> implements Cache<K, V> {

	private Logger log = LoggerFactory.getLogger(ShrioRedisCacheService.class);
	@Autowired
	private RedisTemplate<byte[], V> redisTemplate;

	private LoginUserService loginUserService;

	private String prefix = "shiro_redis:";

	public ShrioRedisCacheService(RedisTemplate<byte[], V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ShrioRedisCacheService(RedisTemplate<byte[], V> redisTemplate, String prefix) {
		this(redisTemplate);
		this.prefix = prefix;
	}

	@Override
	public V get(K key) throws CacheException {
		log.info("ShrioRedisCacheService==>get");
		if(log.isDebugEnabled()) {
			log.debug("Key: {}", key);
		}
		if(key == null) {
			return null;
		}

		//loginUserService = (LoginUserService)StringUtils.applicationContext.getBean("loginUserServiceImpl");
		//Object obj = loginUserService.getShiroRedisObject(key);
		//return (V)obj;

		byte[] bkey = getByteKey(key);
		V v = redisTemplate.opsForValue().get(bkey);
		return v;

	}

	@Override
	public V put(K key, V value) throws CacheException {
		log.info("ShrioRedisCacheService==>put");
		if(log.isDebugEnabled()) {
			log.debug("Key: {}, value: {}", key, value);
		}
		
		if(key == null || value == null) {
			return null;
		}
		/*loginUserService = (LoginUserService)StringUtils.applicationContext.getBean("loginUserServiceImpl");
		Object obj = loginUserService.getShiroRedisObject(key,value);*/

		byte[] bkey = getByteKey(key);
		redisTemplate.opsForValue().set(bkey, value);
		return value;
	}

	@Override
	public V remove(K key) throws CacheException {
		log.info("ShrioRedisCacheService==>remove");
		if(log.isDebugEnabled()) {
			log.debug("Key: {}", key);
		}
		
		if(key == null) {
			return null;
		}
		
		byte[] bkey = getByteKey(key);
		ValueOperations<byte[], V> vo = redisTemplate.opsForValue();
		V value = vo.get(bkey);
		redisTemplate.delete(bkey);
		return value;
	}

	@Override
	public void clear() throws CacheException {
		log.info("ShrioRedisCacheService==>clear");
		redisTemplate.getConnectionFactory().getConnection().flushDb();
	}

	@Override
	public int size() {
		log.info("ShrioRedisCacheService==>size");
		Long len = redisTemplate.getConnectionFactory().getConnection().dbSize();
		return len.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		log.info("ShrioRedisCacheService==>keys");
		byte[] bkey = (prefix+"*").getBytes();
		Set<byte[]> set = redisTemplate.keys(bkey);
		Set<K> result = Sets.newHashSet();
		
		if(CollectionUtils.isEmpty(set)) {
			return Collections.emptySet();
		}
		
		for(byte[] key: set) {
			result.add((K)key);
		}
		return result;
	}

	@Override
	public Collection<V> values() {
		log.info("ShrioRedisCacheService==>values");
		Set<K> keys = keys();
		List<V> values = new ArrayList<>(keys.size());
		for(K k: keys) {
			byte[] bkey = getByteKey(k);
			values.add(redisTemplate.opsForValue().get(bkey));
		}
		return values;
	}
	
	private byte[] getByteKey(K key){
		log.info("ShrioRedisCacheService==>getByteKey");
		if(key instanceof String){
			String preKey = this.prefix + key;
    		return preKey.getBytes();
    	}else{
    		return SerializeUtils.serialize(key);
    	}
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
