package com.xoado.client;

public class Cache {
	private String key;// 缓存ID
	/**
	 *@see (App存储："黑名单，白名单，系统无信息")
	 *@see (Token存储："token类")
	 */
	private Object value;// 缓存数据
	private long creatTime;// 过期时间
	private long outTime;// 过期时间
	private boolean expired; // 是否终止
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Cache() {
		super();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}

	public long getOutTime() {
		return outTime;
	}

	public void setOutTime(long outTime) {
		this.outTime = outTime;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Cache(String key, Object value, long creatTime, long outTime, boolean expired,String type) {
		super();
		this.key = key;
		this.value = value;
		this.creatTime = creatTime;
		this.outTime = outTime;
		this.expired = expired;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Cache [key=" + key + ", value=" + value + ", creatTime=" + creatTime + ", outTime=" + outTime
				+ ", expired=" + expired + ", type=" + type + "]";
	}

	
	
}