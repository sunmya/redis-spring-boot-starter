package com.wuku.stater.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wuku.redis")
public class RedisProperties {

	private String host;
	private int port=6379;
	private int iscluster;
	private String password;
	private int timeOut;


	public boolean iscluster() {
		return iscluster == 1;
	}

	public void setIscluster(int iscluster) {
		this.iscluster = iscluster;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getIscluster() {
		return iscluster;
	}


}
