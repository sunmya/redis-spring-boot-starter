package com.wuku.stater.auth.redisClient;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wuku.stater.auth.properties.RedisProperties;
import com.wuku.stater.auth.service.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClient extends RedisService {
	private static Logger logger = LoggerFactory.getLogger(JedisClient.class);
	
	
	public JedisClient(RedisProperties redisProperties) {
		super(redisProperties);
	}

	private JedisPool pool;

	private JedisPool getJedisPool() {
		if (pool == null) {
			pool = createPool();
		}
		return pool;
	}

	private JedisPool createPool() {
		JedisPool jedisPool = null;
		String host = redisProperties.getHost();
		int port = redisProperties.getPort();
		String password = redisProperties.getPassword();
		int timeout = redisProperties.getTimeOut();

		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(-1);

			// 设置最小空闲连接数为8，确保最少有8个空闲连接
			config.setMinIdle(8);
			// 设置最大空闲连接数为Integer.MAX_VALUE，保证新建连接用完不会被马上断开
			config.setMaxIdle(Integer.MAX_VALUE);
			// 设置清理间隔时间；如果为负数，则不运行逐出线程
			config.setTimeBetweenEvictionRunsMillis(180000);
			// 设置连接的最小空闲时间为5分钟，超出这个时间且空闲连接数大于最小空闲数则标记为可清理
			config.setSoftMinEvictableIdleTimeMillis(300000);
			// 设置逐出时间为最大值，屏蔽硬逐出行为
			config.setMinEvictableIdleTimeMillis(Long.MAX_VALUE);
			// 设置无连接时阻塞，一直等，保证不抛异常
			config.setBlockWhenExhausted(true);
			config.setMaxWaitMillis(-1);
			// 让线程先到先得，避免无响应
			config.setFairness(true);

			// 在获取连接的时候检查有效性, 默认false
			config.setTestOnBorrow(false);
			config.setTestOnReturn(true);
			// 在空闲时检查有效性, 默认false
			config.setTestWhileIdle(true);

			jedisPool = new JedisPool(config, host, port, timeout, password);

			// 预分配连接
			int i = 0;
			ArrayList<Jedis> arrJedis = new ArrayList<>();
			for (i = 0; i < config.getMinIdle(); i++) {
				arrJedis.add(jedisPool.getResource());
			}

			for (i = 0; i < config.getMinIdle(); i++) {
				arrJedis.get(i).close();
			}

		} catch (Exception e) {
			logger.error("{}", e);
		}
		return jedisPool;
	}

	/**
	 * redis设置值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {

		Jedis redisClient = getJedisPool().getResource();
		redisClient.set(key, value);
		redisClient.close();
		return null;
	}

	/**
	 * 获取字符串对象
	 *
	 * @param key
	 * @return
	 */
	public String get(String key) {

		Jedis redisClient = getJedisPool().getResource();
		String string = redisClient.get(key);
		redisClient.close();
		return string;
	}

	/**
	 * 获取Map对象
	 *
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) {

		Jedis redisClient = getJedisPool().getResource();
		Map<String, String> hgetAll = redisClient.hgetAll(key);
		redisClient.close();
		return hgetAll;

	}

	/**
	 * 获取集合中所有元素
	 *
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key) {

		Jedis redisClient = getJedisPool().getResource();
		Set<String> smembers = redisClient.smembers(key);
		redisClient.close();
		return smembers;

	}

	/**
	 * 判断集合中是否存在元素
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean sismember(String key, String member) {

		Jedis redisClient = getJedisPool().getResource();
		Boolean sismember = redisClient.sismember(key, member);
		redisClient.close();
		return sismember;

	}

	/**
	 * 向集合中添加多个元素
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public Long sadd(String key, String... members) {

		if (members == null || members.length < 1) {
			return 0L;
		}

		Jedis redisClient = getJedisPool().getResource();
		Long sadd = redisClient.sadd(key, members);
		redisClient.close();
		return sadd;
	}

	/**
	 * 设置ttl值
	 * 
	 * @param key
	 * @param ttl
	 * @return
	 */
	public Long ttl(String key, int ttl) {
		Jedis redisClient = getJedisPool().getResource();
		Long expire = redisClient.expire(key, ttl);
		redisClient.close();
		return expire;
	}

	/**
	 * 批量设置ttl值
	 * 
	 * @param ttl
	 * @param keys
	 * @return
	 */
	public boolean ttl(int ttl, String... keys) {
		Jedis redisClient = getJedisPool().getResource();
		for (String key : keys) {
			redisClient.expire(key, ttl);
		}
		redisClient.close();
		return true;
	}

}
