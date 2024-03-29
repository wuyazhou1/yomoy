package com.nsc.AmoskiUser.client;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

public class ShrioRedisCacheManager extends AbstractCacheManager{
	private RedisTemplate<byte[], Object> redisTemplate;
	
	public ShrioRedisCacheManager(RedisTemplate<byte[], Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected Cache<byte[], Object> createCache(String name) throws CacheException {
		return new ShrioRedisCacheService<byte[], Object>(redisTemplate, name);
	}
}
