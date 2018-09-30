package com.xoado.client;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.xoado.common.JsonUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: CacheManager
 * @Description:TODO 管理缓存
 * @author: c
 * @date: 2018年9月22日 上午11:36:22
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CacheManager {
	private static ConcurrentHashMap cacheMap = new ConcurrentHashMap();

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	private synchronized static Cache getCache(String key) {
		return (Cache) cacheMap.get(key);
	}
	/**
	 * 缓存是否存在
	 * 
	 * @param key
	 * @return
	 */
	private synchronized static boolean hasCache(String key) {
		return cacheMap.containsKey(key);
	}

	/**
	 * 获取所有缓存
	 */
	public synchronized static String getAllCache() {
		JSONObject json = new JSONObject();
		for(Object key : cacheMap.keySet()){
			json.put(key, cacheMap.get(key));
			///System.out.println(key+"\t"+cacheMap.get(key));
		}
		return  json.toString();
	}
	/**
	 * 清除所有缓存
	 */
	public synchronized static void clearAll() {
		cacheMap.clear();
	}

	/**
	 * 清除某一类缓存
	 * 
	 * @param type
	 */
	public synchronized static void clearAll(String type) {
		Iterator i = cacheMap.entrySet().iterator();
		String key;
		ArrayList arr = new ArrayList();
		try {
			while (i.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
				key = (String) entry.getKey();
				if (key.startsWith(type)) { // 如果匹配则删除掉
					arr.add(key);
				}
			}
			for (int k = 0; k < arr.size(); k++) {
				clearOnly((String) arr.get(k));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 清除指定的缓存
	 * 
	 * @param key
	 */
	public synchronized static void clearOnly(String key) {
		cacheMap.remove(key);
	}

	/**
	 * 载入缓存
	 * 
	 * @param key
	 *            缓存名称
	 * @param obj
	 *            缓存内容
	 */
	public synchronized static void putCache(String key, Cache obj) {
		cacheMap.put(key, obj);
	}

	/**
	 * 获取缓存信息
	 * 
	 * @param key
	 * @return
	 */
	public static Cache getCacheInfo(String key) {
		if (hasCache(key)) {
			Cache cache = getCache(key);
			if (cacheExpired(cache)) { // 调用判断是否终止方法
				cache.setExpired(true);
			}
			return cache;
		} else
			return null;
	}
	public static Cache cacheIsExist(String key) {
		if (hasCache(key)) {
			Cache cache = getCache(key);
			if (cacheExpired(cache)) { // 调用判断是否终止方法
				cache.setExpired(true);
			}
			return cache;
		} else
			return null;
	}
	/**
	 * 载入缓存信息
	 * 
	 * @param key
	 *            缓存名称
	 * @param obj
	 *            缓存内容
	 * @param dt
	 *            过期时间
	 * @param type 名单类型
	 */
	public static void putCacheInfo(String key, Object value,String type) {
		Cache cache = new Cache();
		cache.setKey(key);
		cache.setCreatTime(System.currentTimeMillis());
		cache.setOutTime(60 * 60 * 1000);
		cache.setValue(value);
		cache.setExpired(false);
		cache.setType(type);
		cacheMap.put(key, cache);
	}

	/**
	 * 判断缓存是否到期
	 * 
	 * @param cache
	 * @return
	 */
	public static boolean cacheExpired(Cache cache) {
		if (null == cache) { // 传入的缓存不存在
			return false;
		}
		long nowTime = System.currentTimeMillis(); // 系统当前的毫秒数
		long creatTime = cache.getCreatTime();
		long outTime = cache.getOutTime();// 缓存内的过期毫秒数
		if (creatTime <= 0 || nowTime - creatTime < outTime) { // 当前-创建<时间戳=未过期
			return false;
		} else { // 大于过期时间 即过期
			return true;
		}
	}

	/**
	 * 获取布尔值缓存(缓存是否存在)
	 * 
	 * @param key
	 * @return
	 */
	public static boolean getSimpleFlag(String key) {
		try {
			return (Boolean) cacheMap.get(key);
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 删除已经过期的缓存
	 */
	public void refresh() {
		for (Object key : cacheMap.keySet()) {
			Cache cache = (Cache) cacheMap.get(key);
			if (true == CacheManager.cacheExpired(cache)) {
				cacheMap.remove(key);
			}
		}
	}

	static {
		Timer t = new Timer();
		t.schedule(new ClearTimerTask(cacheMap), 0, 30 * 60 * 1000);// 每30分钟清理一次缓存
	}

	/**
	 * 自动清理缓存
	 */
	private static class ClearTimerTask extends TimerTask {
		Map<String, Cache> cache;

		public ClearTimerTask(Map<String, Cache> cache) {
			this.cache = cache;
		}

		@Override
		public void run() {
			Set<String> keys = cache.keySet();
			for (String key : keys) {
				Cache data = cache.get(key);
				if (data.getOutTime() <= 0) {// 当过期的时间戳为0，跳出
					continue;
				}
				if (data.getOutTime() > System.currentTimeMillis() - data.getCreatTime()) {// 当前时间戳与过期比较小
					continue;
				}
				cache.remove(key);
			}
		}
	}
}
