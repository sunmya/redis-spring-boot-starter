package com.wuku.stater.auth.redisClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wuku.stater.auth.properties.RedisProperties;
import com.wuku.stater.auth.service.RedisService;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterClient extends RedisService {

	private static Logger logger = LoggerFactory.getLogger(JedisClusterClient.class);

	private JedisCluster cluster;

	public JedisClusterClient(RedisProperties redisProperties) {
		super(redisProperties);
	}

	private JedisCluster getJedisCluster() {
		if (cluster == null) {
			cluster = createJedis();
		}
		return cluster;
	}

	private JedisCluster createJedis() {

		String host = redisProperties.getHost();
		int port = redisProperties.getPort();
		String password = redisProperties.getPassword();
		int timeout = redisProperties.getTimeOut();

		String[] split = host.split(",");
		Set<HostAndPort> nodes = new HashSet<>();
		for (String str : split) {

			if (str.contains(":")) {
				String[] split2 = str.split(":");

				try {
					port = Integer.parseInt(split2[1]);
				} catch (Exception e) {
					logger.error("{}", e);
				}
				nodes.add(new HostAndPort(split2[0], port));
			} else {
				nodes.add(new HostAndPort(str, port));
			}
		}

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMinIdle(100);
		poolConfig.setMaxTotal(200);
		poolConfig.setMaxWaitMillis(30000L);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(60000);
		poolConfig.setTimeBetweenEvictionRunsMillis(30000);
		poolConfig.setNumTestsPerEvictionRun(-1);
		return new JedisCluster(nodes, timeout, timeout, 3, password, poolConfig);
	}

	/**
	 * redis设置值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {

		JedisCluster redisClient = getJedisCluster();
		redisClient.set(key, value);
		return null;
	}

	/**
	 * 获取字符串对象
	 *
	 * @param key
	 * @return
	 */
	public String get(String key) {

		JedisCluster redisClient = getJedisCluster();
		String string = redisClient.get(key);
		return string;
	}

	/**
	 * 获取Map对象
	 *
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) {

		JedisCluster redisClient = getJedisCluster();
		Map<String, String> hgetAll = redisClient.hgetAll(key);
		return hgetAll;

	}

	/**
	 * 获取集合中所有元素
	 *
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key) {

		JedisCluster redisClient = getJedisCluster();
		Set<String> smembers = redisClient.smembers(key);
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

		JedisCluster redisClient = getJedisCluster();
		Boolean sismember = redisClient.sismember(key, member);
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

		JedisCluster redisClient = getJedisCluster();
		Long sadd = redisClient.sadd(key, members);
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

		JedisCluster redisClient = getJedisCluster();
		Long expire = redisClient.expire(key, ttl);
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

		JedisCluster redisClient = getJedisCluster();
		for (String key : keys) {
			redisClient.expire(key, ttl);
		}
		return true;

	}

}
