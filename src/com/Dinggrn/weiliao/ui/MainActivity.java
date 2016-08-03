package com.Dinggrn.weiliao.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.adapter.MyPagerAdapter;
import com.Dinggrn.weiliao.app.MyApp;
import com.Dinggrn.weiliao.fragment.FriendFragment;
import com.Dinggrn.weiliao.fragment.MessageFragment;
import com.Dinggrn.weiliao.receiver.MyReceiver;
import com.Dinggrn.weiliao.util.SPUtil;
import com.Dinggrn.weiliao.view.BadgeView;
import com.Dinggrn.weiliao.view.MyTabIcon;


public class MainActivity extends BaseActivity implements EventListener{
	@Bind(R.id.vp_main_viewpager)
	ViewPager viewPager;
	MyPagerAdapter adapter;

	@Bind(R.id.myicon_main_message)
	MyTabIcon mtiMessage;
	@Bind(R.id.myicon_main_friend)
	MyTabIcon mtiFriend;
	@Bind(R.id.myicon_main_find)
	MyTabIcon mtiFind;
	@Bind(R.id.myicon_main_setting)
	MyTabIcon mtiSetting;

	MyTabIcon[] icons = new MyTabIcon[4];
	
	
	@Bind(R.id.iv_main_newfriend_tips)
	ImageView ivTips;

	SPUtil sp;
	
	@Bind(R.id.bv_main_messageunread)
	BadgeView badgeView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Log.d("TAG","经度："+MyApp.lastPoint.getLongitude()+" ,纬度："+MyApp.lastPoint.getLatitude());
		ButterKnife.bind(this);
		icons[0] = mtiMessage;
		icons[1] = mtiFriend;
		icons[2] = mtiFind;
		icons[3] = mtiSetting;
		sp = new SPUtil(bmobUserManager.getCurrentUserObjectId());
		initViewPager();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//MainActivity成为MyReceiver订阅者队列中的一员
		MyReceiver.regist(this);
		
		//如果有未处理的添加好友请求，那么就会显示ivTips
		if(bmobDB.hasNewInvite()){
			ivTips.setVisibility(View.VISIBLE);
		}else{
			ivTips.setVisibility(View.INVISIBLE);
		}
		
		//如果有未读的消息，那么就会显示badegeView
		if(bmobDB.hasUnReadMsg()){
			//1）让badgeView可见的
			badgeView.setVisibility(View.VISIBLE);
			//2) bmobDB.getAllUnreadedCount方法获得所有未读消息的数量，设置到badgeView中
			//   设置BadgeView数量的方法，setBadgeCount(int)方法
			setbadgeViewCount();
		}else{
			//
			badgeView.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 设置badgeView中要显示的数量
	 */
	public void setbadgeViewCount() {
		//获得本地数据库中Recent数据表中总的未读消息数量
		int count = bmobDB.getAllUnReadCount();
		if(count > 0){
			badgeView.setVisibility(View.VISIBLE);
			badgeView.setBadgeCount(count);
		}else{
			badgeView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		//MainActivity不再是MyReceiver订阅者队列中的一员
		MyReceiver.unregist(this);
	}
	
	private void initViewPager() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		//初始化的时候，应该让第一个MyTabIcon显示绿色
		//其余的都是灰色
		for(int i=0;i<icons.length;i++){
			icons[i].setMyTabIconAlpha(0);
		}
		icons[0].setMyTabIconAlpha(255);
		//为ViewPager添加滑动监听器
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				//当页面选定后，应该让当前页面对应的按钮是绿色的
				//其余都是灰色的
				for(int i=0;i<icons.length;i++){
					icons[i].setMyTabIconAlpha(0);
				}
				icons[arg0].setMyTabIconAlpha(255);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// 在ViewPager的滑动过程中，该方法会被不断的调用
				//参数的含义：arg0 当前的页面;arg1 滑动的百分比 0--->1;arg2根据滑动百分比计算出来的滑动距离
				
				//log(arg0+" / "+arg1+" / "+arg2);
				
				if(arg0<adapter.getCount()-1){
					icons[arg0].setMyTabIconAlpha((int)((1-arg1)*255));
					icons[arg0+1].setMyTabIconAlpha((int)(arg1*255));
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	@OnClick({R.id.myicon_main_message,R.id.myicon_main_friend,R.id.myicon_main_find,R.id.myicon_main_setting})
	public void setCurrentFragment(View v){
		switch (v.getId()) {
		case R.id.myicon_main_message:
			viewPager.setCurrentItem(0,false);
			break;
		case R.id.myicon_main_friend:
			viewPager.setCurrentItem(1,false);
			break;
		case R.id.myicon_main_find:
			viewPager.setCurrentItem(2,false);
			break;
		case R.id.myicon_main_setting:
			viewPager.setCurrentItem(3,false);
			break;
		}
	}


	@Override
	public void onMessage(BmobMsg message) {
		//MyReceiver收到了服务器推送过来聊天类型的信息
		//1)在mtiMessage按钮的右上方显示一个未读消息的数量
		//  BadgeView(实际就是一个TextView)
		setbadgeViewCount();
		//2)更新MessageFragment中显示的内容
		//  调用MessageFragment的refresh方法
		MessageFragment fragment = (MessageFragment) adapter.getItem(0);
		fragment.refresh();
	}


	@Override
	public void onReaded(String conversionId, String msgTime) {
		//MainActivity没有发送聊天消息的界面
		//所以该方法对MainActivity没用
	}


	@Override
	public void onNetChange(boolean isNetConnected) {
		//方法调用时机：
		//当MainActivity处于与用户可交互的时候，
		//MainActivity是MyReceiver订阅者队列中的一员
		//MyReceiver的onReceive方法调用解析服务器推送过来的内容之前
		//MyReceiver会判断一下网络的状况
		//如果没有网络，则会调用订阅者队列中订阅者的onNetChange方法
		//此时，MyReceiver订阅者队列，仅有一个订阅者就是MainActivity
		//所以，MainActivity的onNetChange方法会被调用
		toast("当前网络不可用");
	}


	@Override
	public void onAddUser(BmobInvitation message) {
		//添加我为好友
		//1)让mtiFriend的右上角显示一个红点
		ivTips.setVisibility(View.VISIBLE);
		//如果允许声音提示，使用MyApp中的mediaPlayer播放一个提示音
		if(sp.isAllowSound()){
			MyApp.mediaPlayer.start();
		}
		//2)让FriendFragment的“新的好友”图标右上角也显示一个红点
		FriendFragment fragment = (FriendFragment) adapter.getItem(1);
		fragment.hasNewInvitation();
	}


	@Override
	public void onOffline() {
		// MainActivity处于可交互状态
		// 当MyReceiver接收到了一个“offline”类型的消息
		// 就会调用该方法
		//该方法弹出一个对话框给用户提示，让用户下线
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setTitle("通知");
		builder.setMessage("您的账号已在另一台设备登录，请确认是否是本人操作！");
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				bmobUserManager.logout();
				jump(SplashActivity.class,true);
			}
		});
		//显示对话框
		builder.create().show();
		
		
	}
}
