package com.sb.framework.utils;

import com.sb.framework.SB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("NewApi")
public class SBConfig {
	private SharedPreferences sp;
	private static SBConfig instance;
	private SBConfig(Context context){
		sp = context.getSharedPreferences(SB.spName, Context.MODE_PRIVATE);
	}
	
	public static SBConfig getInstance(){
		if(instance == null) instance = new SBConfig(SB.context);
		return instance;
	}
	
	public void put(String name, boolean value){
		sp.edit().putBoolean(name, value).apply();
	}
	public boolean get(String name, boolean defaultValue){
		return sp.getBoolean(name, defaultValue);
	}
	
	public void put(String name, String value){
		sp.edit().putString(name, value).apply();
	}
	public String get(String name, String defaultValue){
		return sp.getString(name, defaultValue);
	}
	//-----------------
	public void put(String name, Object value){
		//boolean ,float, int long string, StringSet
		if(value instanceof String)
		{
			sp.edit().putString(name, (String)value).apply();
		}
		else if(value instanceof Boolean)
		{
			sp.edit().putBoolean(name, (Boolean)value).apply();
		}
		else if(value instanceof Integer)
		{
			sp.edit().putInt(name, (Integer)value).apply();
		}
		else if(value instanceof Float || value instanceof Double)
		{
			sp.edit().putFloat(name, (Float)value).apply();
		}
		
		else if(value instanceof Long)
		{
			sp.edit().putLong(name, (Long)value).apply();
		}
		
	}
	
	public Object get(String name, Object defaultValue){
		if(defaultValue instanceof String)
		{
			sp.getString(name, (String)defaultValue);
		}
		else if(defaultValue instanceof Boolean)
		{
			sp.getBoolean(name, (Boolean)defaultValue);
		}
		else if(defaultValue instanceof Integer)
		{
			sp.getInt(name, (Integer)defaultValue);
		}
		else if(defaultValue instanceof Float || defaultValue instanceof Double)
		{
			sp.getFloat(name, (Float)defaultValue);
		}
		else if(defaultValue instanceof Long)
		{
			sp.getLong(name, (Long)defaultValue);
		}
		
		return defaultValue;
	}
	
	//-----------------
	
	
}
