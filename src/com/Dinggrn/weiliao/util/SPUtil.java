package com.Dinggrn.weiliao.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.Dinggrn.weiliao.app.MyApp;

/**
 * 用来操作偏好设置文件的工具类
 * @author pjy
 *
 */
public class SPUtil {
	
	private static Editor editor;
	private SharedPreferences sp;
	
	public SPUtil(String spName){
		sp = MyApp.context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	/**
	 * 是否接受通知
	 * @return
	 */
	public boolean isAcceptNotify(){
		return sp.getBoolean("notify", true);
	}
	/**
	 * 是否允许声音
	 * @return
	 */
	public boolean isAllowSound(){
		return sp.getBoolean("sound", true);
	}
	/**
	 * 是否允许震动
	 * @return
	 */
	public boolean isAllowVibrate(){
		return sp.getBoolean("vibrate", true);
	}
	/**
	 * 设置是否允许接受通知
	 * @param flag
	 */
	public void setAcceptNotify(boolean flag){
		editor.putBoolean("notify", flag);
		editor.commit();
	}
	/**
	 * 设置是否允许声音
	 * @param flag
	 */
	public void setAllowSound(boolean flag){
		editor.putBoolean("sound", flag);
		editor.commit();
	}
	/**
	 * 设置是否允许震动
	 * @param flag
	 */
	public void setAllowVibrate(boolean flag){
		editor.putBoolean("vibrate", flag);
		editor.commit();
	}
}
