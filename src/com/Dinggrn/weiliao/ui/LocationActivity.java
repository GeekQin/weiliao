package com.Dinggrn.weiliao.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.UpdateListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.app.MyApp;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
/**
 * 地图界面
 * 
 * @author pjy
 *
 */
public class LocationActivity extends BaseActivity {

	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.mapview_location)
	MapView mapView;


	String type;//可能取值"button","item"
	double lat;//纬度值
	double lng;//经度值
	String address;//(lat,lng)坐标点所对应的街道名称

	BaiduMap baiduMap;
	LocationClient client;
	MyLocationListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		ButterKnife.bind(this);
		type = getIntent().getStringExtra("type");
		log("type------>"+type);
		initHeaderView();
	}

	private void initHeaderView() {
		if(type.equals("button")){
			//标题
			setHeaderTitle(headerView, "我的位置");
			//左侧回退按钮
			setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			
			//右侧截图按钮
			setHeaderImage(headerView, R.drawable.ic_map_snap, Position.RIGHT, new OnClickListener() {

				@Override
				public void onClick(View v) {
					//截图开始前给用户一个提示
					final ProgressDialog pd = ProgressDialog.show(LocationActivity.this, "", "地图截图中...");
					pd.show();
					//进行地图截图
					baiduMap.snapshot(new SnapshotReadyCallback() {
						@Override
						public void onSnapshotReady(Bitmap bitmap) {
							try {
								
								//1)bitmap地图截图上传到Bmob服务器
								File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".png");
								String filePath = file.getAbsolutePath();
								OutputStream stream = new FileOutputStream(file);
								bitmap.compress(CompressFormat.PNG, 30, stream );
								BmobProFile.getInstance(LocationActivity.this).upload(filePath, new UploadListener() {
									
									@Override
									public void onError(int arg0, String arg1) {
										// TODO Auto-generated method stub
										pd.dismiss();
									}
									
									@Override
									public void onSuccess(String arg0, String arg1, BmobFile arg2) {
										pd.dismiss();
										String url = arg2.getUrl();//地图截图上传到服务器上的地址
										//2)把截图在服务器上的地址从LocationActivity传递给ChatActivity
										Intent data = new Intent();
										data.putExtra("url", url);
										data.putExtra("address", address);
										data.putExtra("lat",lat);
										data.putExtra("lng", lng);
										setResult(RESULT_OK, data);
										finish();
									}
									
									@Override
									public void onProgress(int arg0) {
										// TODO Auto-generated method stub
										
									}
								});
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
							
						}
					});

				}
			});
			
			getLocation();//进行定位
			
		}else{
			//从item获得地理位置信息，显示到标题位置
			String titleaddress = getIntent().getStringExtra("address");
			if(!TextUtils.isEmpty(titleaddress)){
				setHeaderTitle(headerView, titleaddress);
			}else{
				setHeaderTitle(headerView, "好友位置");
			}
			
			setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			showLocation();//显示位置
		}
	}

	private void getLocation() {
		baiduMap = mapView.getMap();
		baiduMap.setMaxAndMinZoomLevel(20, 15);
		client = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy
				);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
		int span=5*60*1000;
		option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
		option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		listener = new MyLocationListener();
		client.registerLocationListener(listener);
		client.start();
	}

	private void showLocation() {
		//显示从聊天信息中获得的地理位置内容
		baiduMap = mapView.getMap();
		baiduMap.setMaxAndMinZoomLevel(20, 15);
		double locLat = Double.parseDouble(getIntent().getStringExtra("lat"));
		double locLng = Double.parseDouble(getIntent().getStringExtra("lng"));
		log("坐标：("+locLat+" , "+locLng+" )");
		MarkerOptions overlay = new MarkerOptions();
		overlay.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
		overlay.position(new LatLng(locLat, locLng));
		baiduMap.addOverlay(overlay);
		//移动屏幕中心
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(locLat, locLng));
		baiduMap.animateMapStatus(msu);
	}

	@Override  
	protected void onDestroy() {  
		super.onDestroy();  
		mapView.onDestroy();  
	}  
	@Override  
	protected void onResume() {  
		super.onResume();  
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
		mapView.onResume();  
	}  
	@Override  
	protected void onPause() {  
		super.onPause();  
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
		mapView.onPause();  
	} 
	
	/**
	 * 如果lat,lng与最近一次定位结果MyApp.lastPoint不一致
	 * 则更新MyApp.lastPoint的值
	 * 并且更细服务器上当前登录用户的坐标值
	 */
	public void check(double latitude, double longitude) {
		if(latitude == MyApp.lastPoint.getLatitude() && longitude==MyApp.lastPoint.getLongitude()){
			return;
		}else{
			
			MyApp.lastPoint.setLatitude(latitude);
			MyApp.lastPoint.setLongitude(longitude);
			
			User user = new User();
			user.setPoint(new BmobGeoPoint(longitude, latitude));
			user.update(this, bmobUserManager.getCurrentUserObjectId(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					toast("更新位置成功");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					toast("更新位置失败");
				}
			});
		}
	}

	public class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation arg0) {

			int code = arg0.getLocType();
			
			if(code==61||code==66||code==161){
				lat = arg0.getLatitude();
				lng = arg0.getLongitude();
				/**
				 * 如果lat,lng与最近一次定位结果MyApp.lastPoint不一致
				 * 则更新MyApp.lastPoint的值
				 * 并且更细服务器上当前登录用户的坐标值
				 */
				check(lat,lng);

			}else{
				lat = MyApp.lastPoint.getLatitude();
				lng = MyApp.lastPoint.getLongitude();
			}
			
			if(client.isStarted()){
				client.stop();
				client.unRegisterLocationListener(listener);
			}
			
			//根据定位得到的(lat,lng)，在地图上显示一个覆盖物
			showMarker(lat,lng);
			//根据定位得到的(lat,lng)，获得该位置所对应的街道信息
			getAddress(lat,lng);
		}
	}

	public void showMarker(double latitude, double longitude) {
		//添加覆盖物
		MarkerOptions overlay = new MarkerOptions();
		overlay.position(new LatLng(latitude, longitude));
		overlay.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
		baiduMap.addOverlay(overlay);
		//添加信息窗
		View view = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
		InfoWindow infoWindow = new InfoWindow(view,new LatLng(latitude, longitude),-50);
		baiduMap.showInfoWindow(infoWindow);
		//挪动屏幕中心
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
		baiduMap.animateMapStatus(msu);
	}
	
	public void getAddress(double latitude, double longitude) {
		// 根据(latitude, longitude)发起反向地理位置信息查询
		// 根据一个地址--->(lat,lng) 地理编码查询
		// 根据一个(lat,lng)--->地址  反向地理编码查询
		GeoCoder geoCoder = GeoCoder.newInstance();
		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				//反向地理编码查询结束回调该方法
				if(result==null||result.error!=SearchResult.ERRORNO.NO_ERROR){
					toast("找不到对应的街道信息");
					address = "无名道路";
				}else{
					address = result.getAddress();
					toast(address);
				}
				
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO 地理编码查询结束回调该方法
				
			}
		});
		
		ReverseGeoCodeOption options = new ReverseGeoCodeOption();
		options.location(new LatLng(latitude, longitude));
		geoCoder.reverseGeoCode(options);
	}


}
