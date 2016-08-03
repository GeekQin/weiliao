package com.Dinggrn.weiliao.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.app.MyApp;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.constant.Constant.Position;


public class BaseActivity extends FragmentActivity{
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int CENTER = 2;

	BmobUserManager bmobUserManager;
	BmobChatManager bmobChatManager;
	BmobDB bmobDB;

	Toast toast;

	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		bmobUserManager = BmobUserManager.getInstance(this);
		bmobChatManager = BmobChatManager.getInstance(this);
		bmobDB = BmobDB.create(this);
	}

	//Toast和Log的输出
	public void toast(String text){
		toast.setText(text);
		toast.show();
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
		Intent intent  = new Intent(this,clazz);
		startActivity(intent);
		if(isFinish){
			this.finish();
		}
	}

	public void jump(Intent intent,boolean isFinish){
		startActivity(intent);
		if(isFinish){
			this.finish();
		}
	}

	//设置头部布局
	//设置头部标题，标题居中显示
	public void setHeaderTitle(View headerView,String text){
//		TextView tv = (TextView) headerView.findViewById(R.id.tv_headerview_title);
//		if(text==null){
//			tv.setText("");
//		}else{
//			tv.setText(text);
//		}
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
	/**
	 * 更新指定用户的位置信息
	 * @param user
	 */
	public void updateUserLocation(User user) {
		//更新一下用户的位置，把位置设置成MyApp.lastPoint
		//获得了当前的登录用户
		//User user = bmobUserManager.getCurrentUser(User.class);
		//更新这个用户point属性的值
		User user2 = new User();
		user2.setPoint(MyApp.lastPoint);
		user2.update(this, user.getObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				toast("位置更新成功");//???
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("位置更新失败", arg0+": "+arg1);
			}
		});
	}
}
