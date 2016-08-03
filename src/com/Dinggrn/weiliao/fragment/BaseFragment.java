package com.Dinggrn.weiliao.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.app.MyApp;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.ui.MainActivity;

public class BaseFragment extends Fragment{
	
	BmobUserManager bmobUserManager;
	BmobChatManager bmobChatManager;
	BmobDB bmobDB;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bmobUserManager = BmobUserManager.getInstance(MyApp.context);
		bmobChatManager = BmobChatManager.getInstance(MyApp.context);
		bmobDB = BmobDB.create(MyApp.context);
	}
	
	public void toast(String text){
		//方法不能在Fragment的onCreate里面调用
		//因为getActivity有可能在onCreate方法中会得到null
		MainActivity activity = (MainActivity) getActivity();
		activity.toast(text);
	}

	public void log(String log){
		Log.d("TAG", this.getClass().getName()+"输出："+log);
	}

	public void toastAndLog(String text,String log){
		toast(text);
		log(log);
	}
	
	//界面的跳转
		public void jump(Class<?> clazz,boolean isFinish){
			Intent intent  = new Intent(getActivity(),clazz);
			startActivity(intent);
			if(isFinish){
				getActivity().finish();
			}
		}

		public void jump(Intent intent,boolean isFinish){
			startActivity(intent);
			if(isFinish){
				getActivity().finish();
			}
		}
		
		
		public void setHeaderTitle(View headerView,String text){
//			TextView tv = (TextView) headerView.findViewById(R.id.tv_headerview_title);
//			if(text==null){
//				tv.setText("");
//			}else{
//				tv.setText(text);
//			}
			setHeaderTitle(headerView, text, Position.CENTER);
		}
		//设置头部标题的重载方法，可以明确指定标题的位置
		public void setHeaderTitle(View headerView,String text,Position pos){
			TextView tv = (TextView) headerView.findViewById(R.id.tv_headerview_title);
			//		switch (pos) {
			//		case 0:
			//			tv.setGravity(LEFT);
			//			break;
			//
			//		default:
			//			break;
			//		}

			switch (pos) {
			case LEFT:
				tv.setGravity(Gravity.LEFT);
				break;
			case RIGHT:
				tv.setGravity(Gravity.RIGHT);
				break;
			case CENTER:
				tv.setGravity(Gravity.CENTER);
				break;
			}
			
			if(text==null){
				tv.setText("");
			}else{
				tv.setText(text);
			}
		}
		/**
		 * 用来设置头部左侧或右侧的图像
		 * @param headerView 头部headerview
		 * @param resId 显示图像的资源id
		 * @param pos pos如果是LEFT就设置左侧的图像，如果是CENTER或RIGHT均为设置右侧图像
		 */
		public void setHeaderImage(View headerView,int resId,Position pos,OnClickListener listener){
			ImageView iv = null;
			switch (pos) {
			
			case LEFT:
				iv = (ImageView) headerView.findViewById(R.id.iv_haderview_left);
				break;
			
			case CENTER:
			case RIGHT:
				iv = (ImageView) headerView.findViewById(R.id.iv_haderview_right);
				break;
			}
			iv.setImageResource(resId);
			iv.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
			
			if(listener!=null){
				iv.setOnClickListener(listener);
			}
		}
		
		/**
		 * 判空
		 * 返回true，就说明有EditText未输入内容
		 * 返回false，就说明EditText都输入了
		 */
		public boolean isEmpty(EditText... ets){
			
			for (EditText editText : ets) {
				if(TextUtils.isEmpty(editText.getText().toString())){
					//如果直接为setError方法提供Stirng类型参数
					//有可能出现的提示文字使用的颜色，与错误提示框的背景色重复
					//造成提示文字不可见
					//但是，setError接受的参数类型是CharSequence类型
					//所以，更换一下参数的类型，不是用标准的Stirng，而是使用安卓提供的
					//可扩展String，为可扩展Stirng指定一个不同于提示框背景的颜色
					//<font color="颜色值">
					editText.setError(Html.fromHtml("<font color=#ff0000>请输入完整</font>"));
					return true;
				}
			}
			
			return false;
		}
	
}
