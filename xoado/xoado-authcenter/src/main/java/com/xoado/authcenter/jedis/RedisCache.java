package com.xoado.authcenter.jedis;

public interface RedisCache {
	
	String get(String key);    //获取一条String类型的数据
	
	String set(String key, String value);    //插入一条String类型的数据
	
	String hget(String hkey, String key);     //获取某个哈希表里某个字段的值
	
	long hset(String hkey, String key, String value);   //存入一个哈希表
	
	long incr(String key);    //将key中存储的数字值增一
	
	long expire(String key, int second);   //设置一个key的生存时间  单位/秒
	
	long ttl(String key);    //返回给定key的有效时间  如果为-1则永远有效
	
	long del(String key);    //删除指定的key
	
	long hdel(String hkey, String key);     //删除指定哈希表中的指定字段

}
