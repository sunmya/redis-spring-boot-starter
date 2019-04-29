package com.wuku.stater.auth.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wuku.stater.auth.properties.RedisProperties;

/**
 * 
 * 
 * @author SunMy
 *
 */
public abstract class RedisService {

	protected RedisProperties redisProperties;

	public RedisService(RedisProperties redisProperties) {
		this.redisProperties = redisProperties;
	}

	/**
	 * redis设置值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public abstract String set(String key, String value);

	/**
	 * 获取字符串对象
	 *
	 * @param key
	 * @return
	 */
	public abstract String get(String key);

	/**
	 * 获取Map对象
	 *
	 * @param key
	 * @return
	 */
	public abstract Map<String, String> hgetAll(String key);

	/**
	 * 获取集合中所有元素
	 *
	 * @param key
	 * @return
	 */
	public abstract Set<String> smembers(String key);

	/**
	 * 判断集合中是否存在元素
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract boolean sismember(String key, String member);

	/**
	 * 向集合中添加多个元素
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public Long sadd(String key, List<String> members) {

		String[] strArray = new String[members.size()];
		strArray = members.toArray(strArray);
		return sadd(key, strArray);

	}

	/**
	 * 向集合中添加多个元素
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public abstract Long sadd(String key, String... members);

	/**
	 * 设置ttl值
	 * 
	 * @param key
	 * @param ttl
	 * @return
	 */
	public abstract Long ttl(String key, int ttl);

	/**
	 * 批量设置ttl值
	 * 
	 * @param ttl
	 * @param keys
	 * @return
	 */
	public abstract boolean ttl(int ttl, String... keys);

}