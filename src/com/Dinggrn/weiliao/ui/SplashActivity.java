package com.Dinggrn.weiliao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.app.MyApp;
import com.Dinggrn.weiliao.bean.User;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class SplashActivity extends BaseActivity {

	@Bind(R.id.tv_splash_login)
	TextView tvstart;

	LocationClient client;//百度定位客户端
	MyBDLocationListener listener;//定位回调监听器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//让SplashActivity真正占满全屏幕
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_splash);
		ButterKnife.bind(this);//绑定黄油刀
		initBaiduLocation();// 初始化百度地图定位客户端，并发起定位
	}
	
	/**
	 * 初始化百度地图定位客户端，并发起定位
	 */
	private void initBaiduLocation() {
		//创建百度地图定位客户端对象
		client = new LocationClient(this);
		//配置百度地图定位参数（从百度开发者网站copy的代码）
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy
				);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
		int span=1000;
		option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
		option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		//为定位客户端设置参数
		client.setLocOption(option);
		//创建定位完成后的监听器
		listener = new MyBDLocationListener();
		//为客户端注册监听器
		client.registerLocationListener(listener);
		//发起定位请求
		client.start();//--->百度地图的定位Service(Bind方式启动com.baidu.location.f，有时候会报ServiceLeak异常)
	}

	public class MyBDLocationListener implements BDLocationListener{
		//定位完成后回回调该方法，并将定位结果以参数的形式传入到回调方法中
		@Override
		public void onReceiveLocation(BDLocation result) {
			
			int code = result.getLocType();
			
			double lat = 0;
			double lng = 0;
			//只有code为61或66或161的时候，才表示获得了真实的位置信息
			if(code==61||code==66||code==161){
				lat = result.getLatitude();
				lng = result.getLongitude();
			}else{
				//如果不能正确获得信息，就指定一个位置（该种情况仅仅针对模拟器）
				lat = 39.876457;
				lng = 116.464899;
			}
			//利用获得的经纬度为MyApp.lastPoint赋值
			MyApp.lastPoint = new BmobGeoPoint(lng, lat);
			//定位成功后，开始动画
			startAnimation();
			//定位成功后，停止反复定位
			if(client.isStarted()){
				client.stop();
				client.unRegisterLocationListener(listener);
			}
		}

	}
	
	public void startAnimation() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
		tvstart.setVisibility(View.VISIBLE);
		tvstart.startAnimation(anim);
		//new Handler().postDelay(Runnable,3000);
		//为属性动画添加一个动画监听器
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			//当动画结束的时候会回调该方法
			@Override
			public void onAnimationEnd(Animation animation) {
				//如果当前有登录的用户（曾经有用户在这台设备上登录过并且一直没有进行登出操作，
				//那么这个用户就一直保持着登录的状态）
				//从SplashActivity跳转到MainActivity
				//反之，说明当前是没有登录用户的，应该跳转到登录界面LoginActivity
				tvstart.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(bmobUserManager.getCurrentUser()!=null){

							updateUserLocation(bmobUserManager.getCurrentUser(User.class));


							//					Intent intent = new Intent(SplashActivity.this,MainActivity.class);
							//					startActivity(intent);
							//					finish();
							jump(MainActivity.class, true);
						}else{
							//					Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
							//					startActivity(intent);
							//					finish();
							jump(LoginActivity.class,true);
						}
					}
				});
				
			}


		});
	}
}
