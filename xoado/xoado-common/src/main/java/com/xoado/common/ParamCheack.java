package com.xoado.common;

//import java.lang.annotation.Annotation;
//import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;


@Service
public class ParamCheack {
	/**
	 * 
	 * @param map
	 *            (参数1userid,deptid,organizeId，参数2,member......)
	 * @return boolean
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	/**
	 * 
	 * @param map ：controller层所有需要传入的参数
	 * @param classs  传入的类(其中的属性为mustMap)
	 * @param mustMap  必须要传参的属性
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "null", "rawtypes" })
	public Object membercheack(Map map, Object classs, Map mustMap){
		Field[] fields = classs.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		try {
			for (int j = 0; j < fields.length; j++) { // 遍历所有属性
				String name = fields[j].getName();   //获取属性声明时名字
				String type = fields[j].getGenericType().toString();  //返回属性声明的type类型
				Class cla = classs.getClass();
				Field f = cla.getDeclaredField(name);  //获取指定名称的属性
				f.setAccessible(true);
				String value = (String) map.get(name);
				String mustValue = (String) mustMap.get(name);
				if (name.equals(mustValue)) {
					if (this.paramCheack(value, type) == false) {
						return null;
					}
					f.set(classs, value);
				} else {
					if (value != null) {
						if (this.paramCheack(value, type) == false) {
							return null;
						}
						f.set(classs, value);
					}
	
				}
	
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

		return classs;
	}

	public Boolean paramCheack(String value, String type) {
		Boolean isCkeack = false;
		String strReg = "^[0-9A-Za-z]+$";
		String numReg = "^[0-9]+$";
		Pattern strPattern = Pattern.compile(strReg);
		Pattern numPattern = Pattern.compile(numReg);
		if (null != value || value.length() > 0) {
			if (type.equals("class java.lang.Integer")) {
				Matcher matcher = numPattern.matcher(value);
				if (matcher.matches() == true) {
					return true;
				}
			}
		}
		if (null != value || value.length() > 0) {
			if (type.equals("class java.lang.Long")) {
				Matcher matcher = numPattern.matcher(value);
				if (matcher.matches() == true) {
					return true;
				}
			}
		}
		if (null != value || value.length() > 0) {
			if (type.equals("class java.lang.String")) {
				Matcher matcher = strPattern.matcher(value);
				if (matcher.matches() == true) {
					return true;
				}
			}
		}
		if (null != value || value.length() > 0) {
			if (type.equals("boolean")) {
				Matcher matcher = strPattern.matcher(value);
				if (matcher.matches() == true) {
					return true;
				}
			}
		}
		if (null != value || value.length() > 0) {
			if (type.equals("interface java.util.List")) {
				return true;
			}
		}
		if (null != value || value.length() > 0) {
			if (type.equals("interface java.util.Map")) {
				return true;
			}
		}
		if (null != value || value.length() > 0) {
			if (type.equals("com.alibaba.fastjson.JSON")) {
				return true;
			}
		}
		return isCkeack;
	}



}
