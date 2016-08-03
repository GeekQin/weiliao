package com.Dinggrn.weiliao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	/**
	 * 将传入的秒数，转为描述性的时间值
	 * 如果聊天信息发生时间与当前时间间隔在一天以内，则直接显示hh:mm，例如15:35
	 * 如果聊天信息发生时间在昨天，则显示“昨天 hh:mm”，例如昨天 15:35
	 * 如果聊天信息发生时间在前天，则显示“前天 hh:mm”，例如前天 15:35
	 * 如果聊天信息发生的时间更早，则显示“yyy/MM/dd HH:mm” 例如2016/4/15 15:35
	 * @param seconds 传入的时间戳，注意该时间戳的值为秒，不是毫秒
	 * @return
	 */
	public static String getTime(long seconds){

		StringBuilder sb = new StringBuilder();
		long now = System.currentTimeMillis();//13位数，毫秒值
		int span = (int) (now/1000/24/3600-seconds/24/3600);
		//span = (int)(now-seconds*1000)/1000/24/3600
		
		SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
		
		switch (span) {
		case 0:
			sb.append(spf.format(new Date(seconds*1000)));
			break;
		case 1:
			sb.append("昨天 ").append(spf.format(new Date(seconds*1000)));
			break;
		case 2:
			sb.append("前天 ").append(spf.format(new Date(seconds*1000)));
			break;
		default:
			SimpleDateFormat spf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			sb.append(spf2.format(new Date(seconds*1000)));
			break;
		}

		return sb.toString();
	}

}
