package com.Dinggrn.weiliao.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.Dinggrn.weiliao.app.MyApp;

public class SharedPreferenceUtil {
	public static Editor editor;
	public SharedPreferences sp;
	public SharedPreferenceUtil(String filename){
		sp = MyApp.context.getSharedPreferences(filename, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public SharedPreferenceUtil(){
		sp = PreferenceManager.getDefaultSharedPreferences(MyApp.context);
		editor = sp.edit();
	}
	//是否允许接收通知
	public boolean isAllowNotification(){
		return sp.getBoolean("notification", true);
	}
	//接收通知时是否允许声音提示
	public boolean isAllowSound(){
		return sp.getBoolean("sound", true);
	}
	//接收通知时是否允许震动提示
	public boolean isAllowVibrate(){
		return sp.getBoolean("vibrate", true);
	}
	//设置是否接受通知
	public void setAllowNotification(boolean flag){
		editor.putBoolean("notification", flag);
		editor.commit();
	}
	//设置是否允许声音
	public void setAllowSound(boolean flag){
		editor.putBoolean("sound", flag);
		editor.commit();
	}
	
	//设置是否允许震动
	public void setAllowVibrate(boolean flag){
		editor.putBoolean("vibrate", flag);
		editor.commit();
	}
	
	
}
