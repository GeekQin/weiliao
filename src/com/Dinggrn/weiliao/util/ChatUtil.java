package com.Dinggrn.weiliao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.Dinggrn.weiliao.app.MyApp;
import com.Dinggrn.weiliao.bean.Emo;


/**
 * 用来处理聊天中表情的工具类
 * 
 * @author pjy
 *
 */
public class ChatUtil {
	public static List<Emo> emos = new ArrayList<Emo>();
	static{
		emos.add(new Emo("[emo]ue056"));
		emos.add(new Emo("[emo]ue057"));
		emos.add(new Emo("[emo]ue058"));
		emos.add(new Emo("[emo]ue059"));
		emos.add(new Emo("[emo]ue105"));
		emos.add(new Emo("[emo]ue106"));
		emos.add(new Emo("[emo]ue107"));
		emos.add(new Emo("[emo]ue108"));
		emos.add(new Emo("[emo]ue11a"));
		emos.add(new Emo("[emo]ue401"));
		emos.add(new Emo("[emo]ue402"));
		emos.add(new Emo("[emo]ue403"));
		emos.add(new Emo("[emo]ue404"));
		emos.add(new Emo("[emo]ue405"));
		emos.add(new Emo("[emo]ue406"));
		emos.add(new Emo("[emo]ue407"));
		emos.add(new Emo("[emo]ue408"));
		emos.add(new Emo("[emo]ue409"));
		emos.add(new Emo("[emo]ue40a"));
		emos.add(new Emo("[emo]ue40b"));
		emos.add(new Emo("[emo]ue40c"));
		emos.add(new Emo("[emo]ue40d"));
		emos.add(new Emo("[emo]ue40e"));
		emos.add(new Emo("[emo]ue410"));
		emos.add(new Emo("[emo]ue411"));
		emos.add(new Emo("[emo]ue412"));
		emos.add(new Emo("[emo]ue413"));
		emos.add(new Emo("[emo]ue414"));
		emos.add(new Emo("[emo]ue415"));
		emos.add(new Emo("[emo]ue416"));
		emos.add(new Emo("[emo]ue417"));
		emos.add(new Emo("[emo]ue418"));
		emos.add(new Emo("[emo]ue420"));
		emos.add(new Emo("[emo]ue421"));
		emos.add(new Emo("[emo]ue422"));
		emos.add(new Emo("[emo]ue423"));
		emos.add(new Emo("[emo]ue452"));
		emos.add(new Emo("[emo]ue453"));
		emos.add(new Emo("[emo]ue454"));
		emos.add(new Emo("[emo]ue455"));
		emos.add(new Emo("[emo]ue456"));
		emos.add(new Emo("[emo]ue457"));
	}
	
	
	
	/**
	 * [emo]ue057---->笑脸
	 * @param string 普通的字符串 "你好[emo]ue057" 
	 * @return "你好^_^"
	 */
	
	public static SpannableString getSpannableString(String string){
		if(!TextUtils.isEmpty(string)){
			SpannableString spannableString = new SpannableString(string);
			
			Pattern pattern = Pattern.compile("\\[emo\\]ue[0-9a-z]{3}");
			Matcher matcher = pattern.matcher(string);
			while(matcher.find()){
				String emoId = matcher.group();//[emo]ue057
				String id = emoId.substring(5);//ue057;
				//ue057---->R.drawable.ue057
				int resId = MyApp.context.getResources().getIdentifier(id, "drawable", MyApp.context.getPackageName());
				//R.drawable.ue057--->bitmap
				Bitmap bitmap = BitmapFactory.decodeResource(MyApp.context.getResources(), resId);
				//把bitmap放到一个“imageView”中
				ImageSpan imgSpan = new ImageSpan(MyApp.context, bitmap);
				//imgSpan放到spannableString中，替换掉spannableString的[emo]ue057
				spannableString.setSpan(imgSpan,matcher.start(), matcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return spannableString;
		}else{
			return new SpannableString("");
		}
	}
	
}
