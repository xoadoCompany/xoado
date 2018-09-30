package com.xoado.client;

import com.xoado.protocol.AccessIdApplication;

public class AppLocalCache {
	/**
	 * 程序是否存在于缓存中
	 * @param appId
	 * @return
	 */
	public static Boolean  isExistCache(String XOADOAPPACCESSCODE){
		Cache cache=CacheManager.getCacheInfo(XOADOAPPACCESSCODE);
		if(cache!=null){
			return true;
		}
		return false;
		
	}
	/**
	 * 获取所有的缓存信息
	 * @return
	 */
	public static String getAllCache(){
		String jsonobj=CacheManager.getAllCache();
		return jsonobj;
	}
	/**
	 * 将缓存保存起来
	 * @return
	 */
	public static Boolean insertCache(String code,String value,String describe){
		if(code.length()>0){
			CacheManager.putCacheInfo(code, value,describe);
			return true;
		}
		return false;
	}
	/**
	 * 根据id获取缓存类型信息
	 */
	public static String getCacheType(String key){
		if(null!=key){
			Cache cache = CacheManager.getCacheInfo(key);
			if(null!=cache&&null!=cache.getType()){
				return cache.getType();
			}
		}
		return null;
	}
	/**
	 * 根据id获取缓存内容信息
	 */
	public static AccessIdApplication getCacheByKey(String code){
		AccessIdApplication accessidapplication = new AccessIdApplication();
		if(null!=code){
			Cache cache = CacheManager.cacheIsExist(code);
			if(null!=cache&&null!=cache.getValue()){
				accessidapplication.setcode(cache.getKey());
				accessidapplication.setvalue((String) cache.getValue());
				accessidapplication.setappId(cache.getType());
				return accessidapplication;
			}
		}
		return accessidapplication;
	}
} 
