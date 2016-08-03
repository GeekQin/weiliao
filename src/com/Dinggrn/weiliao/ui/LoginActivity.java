package com.Dinggrn.weiliao.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobNetUtil;
import cn.bmob.v3.listener.SaveListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.bean.User;
import com.dd.CircularProgressButton;

public class LoginActivity extends BaseActivity {

	@Bind(R.id.headerview)
	View headerview;//顶部的HeaderView

	@Bind(R.id.et_uname)
	EditText etUsername;//输入用户名

	@Bind(R.id.et_upwd)
	EditText etPassword;//输入密码

	@Bind(R.id.btn_login_login)
	CircularProgressButton btLogin;//登录按钮

	@Bind(R.id.tv_login_regist)
	TextView tvRegist;//注册

	long firstPress;//记录第一次按下“back”键的时间戳

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
//		initHeaderView();
	}

	/**
	 * 设置headerView中的内容
	 */
	private void initHeaderView() {
		setHeaderTitle(headerview, "欢迎使用");
		//是否设置图像？图像用什么颜色？自己看着办
	}

	@OnClick(R.id.tv_login_regist)
	public void toRegist(View v){
		jump(RegisterActivity.class,true);
	}

	@OnClick(R.id.btn_login_login)
	public void doLogin(View v){
		//1) 判空
		if(isEmpty(etPassword,etUsername)){
			return;
		}
		//2) 判网
		if(!BmobNetUtil.isNetworkAvailable(this)){
			toast("当前网络不给力");
			return;
		}
		//3) 真正进行注册，更改ProgressButton的状态
		btLogin.setIndeterminateProgressMode(true);
		btLogin.setProgress(50);
		// 3.1) 利用用户输入的用户名和密码，构建一个BmobChatUser对象
		BmobChatUser user = new BmobChatUser();
		user.setUsername(etUsername.getText().toString());
		user.setPassword(etPassword.getText().toString());
		// 3.2) BmobUserManager中的login方法进行登录，把3.1创建的对象传入，同时提供一个监听器
		bmobUserManager.login(user, new SaveListener() {

			@Override
			public void onSuccess() {
				// 3.3) 如果登录成功，更改ProgressButton的状态，要更新用户的位置，要跳转界面到MainActivity
				btLogin.setProgress(100);
				updateUserLocation(bmobUserManager.getCurrentUser(User.class));
				jump(MainActivity.class, true);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// 3.4) 如果登录失败，更改ProgressButton的状态，要给用户提示信息（越准确越好）
				btLogin.setProgress(-1);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						btLogin.setProgress(0);
					}
				}, 1500);

				switch (arg0) {
				case 101:
					toastAndLog("用户名或密码错误", arg0+": "+arg1);
					break;

				default:
					toastAndLog("登录失败", arg0+": "+arg1);
					break;
				}

			}
		});
	}

	/**
	 * 两次按back键退出
	 */

	@Override
	public void onBackPressed() {

		if(firstPress+2000>System.currentTimeMillis()){
			super.onBackPressed();
		}else{
			firstPress = System.currentTimeMillis();
			toast("你敢再按一次吗？");
		}

	}


}
