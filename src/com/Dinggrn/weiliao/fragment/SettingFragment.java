package com.Dinggrn.weiliao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.ui.LoginActivity;
import com.Dinggrn.weiliao.ui.SplashActivity;
import com.Dinggrn.weiliao.ui.UserInfoActivity;
import com.Dinggrn.weiliao.util.SPUtil;


public class SettingFragment extends BaseFragment{
	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.tv_setting_username)
	TextView tvUsername;
	
	@Bind(R.id.iv_setting_notificationswitch)
	ImageView ivNotification;
	@Bind(R.id.iv_setting_soundswitch)
	ImageView ivSound;
	@Bind(R.id.iv_setting_vibrateswitch)
	ImageView ivVibrate;
	
	SPUtil sp;//操作当前登录用户对应的偏好设置文件
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view  = inflater.inflate(R.layout.fragment_setting, container,false);
		ButterKnife.bind(this,view);
		initHeaderView();
		initView();
		return view;
	}

	private void initView() {
		tvUsername.setText(bmobUserManager.getCurrentUserName());
		sp = new SPUtil(bmobUserManager.getCurrentUserObjectId());
		if(sp.isAcceptNotify()){
			ivNotification.setImageResource(R.drawable.ic_switch_on);
		}else{
			ivNotification.setImageResource(R.drawable.ic_switch_off);
		}
		if(sp.isAllowSound()){
			ivSound.setImageResource(R.drawable.ic_switch_on);
		}else{
			ivSound.setImageResource(R.drawable.ic_switch_off);
		}
		if(sp.isAllowVibrate()){
			ivVibrate.setImageResource(R.drawable.ic_switch_on);
		}else{
			ivVibrate.setImageResource(R.drawable.ic_switch_off);
		}
	}

	private void initHeaderView() {
		setHeaderTitle(headerView, "设置", Position.CENTER);
	}
	
	@OnClick(R.id.btn_setting_logout)
	public void logout(View v){
		//登出（没有当前登录用户了）
		bmobUserManager.logout();
		//跳转界面
		jump(LoginActivity.class, true);
	}
	
	@OnClick(R.id.iv_setting_infoedit)
	public void browseUserInfo(View v){
		Intent intent = new Intent(getActivity(),UserInfoActivity.class);
		intent.putExtra("from", "me");//跳转到UserInfoActivity时，显示编辑个人相关信息的界面
		jump(intent, false);
	}
	
	@OnClick(R.id.iv_setting_blackedit)
	public void toBlackActivity(View v){
		//TODO 暂时先不写该界面
	}
	
	@OnClick(R.id.iv_setting_notificationswitch)
	public void switchNotification(View v){
		if(sp.isAcceptNotify()){
			closeNotification();
		}else{
			openNotification();
		}
	}

	private void openNotification() {
		//打开通知按钮
		ivNotification.setImageResource(R.drawable.ic_switch_on);
		//修改偏好设置文件
		sp.setAcceptNotify(true);
		//允许用户单独点击声音和震动按钮，修改状态
		ivSound.setClickable(true);
		ivVibrate.setClickable(true);
	}

	private void closeNotification() {
		//关闭通知按钮，还要关闭声音,震动
		ivNotification.setImageResource(R.drawable.ic_switch_off);
		ivSound.setImageResource(R.drawable.ic_switch_off);
		ivVibrate.setImageResource(R.drawable.ic_switch_off);
		//修改偏好设置文件
		sp.setAcceptNotify(false);
		sp.setAllowSound(false);
		sp.setAllowVibrate(false);
		//不允许单独设置声音,震动
		ivSound.setClickable(false);
		ivVibrate.setClickable(false);
	}
	
	@OnClick(R.id.iv_setting_soundswitch)
	public void switchSound(View v){
		if(sp.isAllowSound()){
			ivSound.setImageResource(R.drawable.ic_switch_off);
			sp.setAllowSound(false);
		}else{
			ivSound.setImageResource(R.drawable.ic_switch_on);
			sp.setAllowSound(true);
		}
	}
	
	@OnClick(R.id.iv_setting_vibrateswitch)
	public void switchVibrate(View v){
		if(sp.isAllowVibrate()){
			ivVibrate.setImageResource(R.drawable.ic_switch_off);
			sp.setAllowVibrate(false);
		}else{
			ivVibrate.setImageResource(R.drawable.ic_switch_on);
			sp.setAllowVibrate(true);
		}
	}
}
