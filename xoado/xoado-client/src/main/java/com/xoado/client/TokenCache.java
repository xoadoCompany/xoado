package com.xoado.client;

import com.xoado.protocol.AccessIdentity;

public class TokenCache {
	/**
	 * 程序是否存在于缓存中
	 * @param appId
	 * @return
	 */
	public static Boolean  isExistCache(String XOADOXOADOTOKENID){
		Cache cache=CacheManager.getCacheInfo(XOADOXOADOTOKENID);
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
	public static Boolean insertCache(String XOADOXOADOTOKENID,AccessIdentity accessidentity,String describe){
		if(XOADOXOADOTOKENID.length()>0){
			CacheManager.putCacheInfo(XOADOXOADOTOKENID, accessidentity,describe);
			return true;
		}
		return false;
	}
	/**
	 * 根据id获取缓存类型信息
	 */
	public static String getCacheType(String XOADOXOADOTOKENID){
		if(null!=XOADOXOADOTOKENID){
			Cache cache = CacheManager.getCacheInfo(XOADOXOADOTOKENID);
			if(null!=cache&&null!=cache.getType()){
				return cache.getType();
			}
		}
		return null;
	}
	/**
	 * 根据id获取缓存内容信息
	 */
	public static AccessIdentity getCacheByKey(String XOADOTOKENID){
		AccessIdentity accessidentity = new AccessIdentity();
		if(null!=XOADOTOKENID){
			Cache cache = CacheManager.cacheIsExist(XOADOTOKENID);
			if(null!=cache&&null!=cache.getValue()){
				AccessIdentity accessidcache = (AccessIdentity) cache.getValue();
				return accessidcache;
			}
		}
		return accessidentity;
	}
}
