/**
 * Copyright 2017 Inc.
 **/
package com.nsc.AmoskiUser.chlientTest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * @author zhangshaoyong
 */
public class RedisSessionDAO extends AbstractSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	/**
	 * shiro-redis的session对象前缀
	 */
	private RedisManager redisManager;

	/**
	 * The Redis key prefix for the sessions
	 */
	private String keyPrefix = "shiro_redis_session:";

	@Override
	public void update(Session session) throws UnknownSessionException {
		//logger.info("RedisSessionDAO==>update");
		this.saveSession(session);
	}

	/**
	 * save session
	 * @param session
	 * @throws UnknownSessionException
	 */
	private void saveSession(Session session) throws UnknownSessionException{
		//logger.info("RedisSessionDAO==>saveSession");
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}

		byte[] key = getByteKey(session.getId());
		byte[] value = SerializeUtils.serialize(session);
		session.setTimeout(redisManager.getExpire()*1000);
		this.redisManager.set(key, value, redisManager.getExpire());
	}

	@Override
	public void delete(Session session) {
		//logger.info("RedisSessionDAO==>delete");
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}
		redisManager.del(this.getByteKey(session.getId()));

	}

	@Override
	public Collection<Session> getActiveSessions() {
		//logger.info("RedisSessionDAO==>getActiveSessions");
		Set<Session> sessions = new HashSet<Session>();
		TreeSet<String> treeSet = new TreeSet<String>();
		try {
			treeSet = redisManager.keys(this.keyPrefix + "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(treeSet != null && treeSet.size()>0){
			Iterator it = treeSet.iterator();
			while (it.hasNext()) {
				Session s = (Session) SerializeUtils.deserialize(
						redisManager.get(SerializeUtils.serialize(it.next())));
				sessions.add(s);
			}
		}

		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		//logger.info("RedisSessionDAO==>doCreate");
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		//logger.info("RedisSessionDAO==>doReadSession");
		if(sessionId == null){
			logger.error("session id is null");
			return null;
		}
		Session s = (Session)SerializeUtils.deserialize(redisManager.get(this.getByteKey(sessionId)));
		return s;
	}

	/**
	 * 获得byte[]型的key
	 * @param sessionId
	 * @return
	 */
	private byte[] getByteKey(Serializable sessionId){
		//logger.info("RedisSessionDAO==>getByteKey");
		String preKey = this.keyPrefix + sessionId;
		return preKey.getBytes();
	}

	public RedisManager getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(RedisManager redisManager) {
		//logger.info("RedisSessionDAO==>setRedisManager");
		this.redisManager = redisManager;

		/**
		 * 初始化redisManager
		 */
		this.redisManager.init();
	}

	/**
	 * Returns the Redis session keys
	 * prefix.
	 * @return The prefix
	 */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * Sets the Redis sessions key 
	 * prefix.
	 * @param keyPrefix The prefix
	 */
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}


}
