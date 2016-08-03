package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.PushListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.app.MyApp;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NearFriendAdapter extends MyBaseAdapter<User>{

	public NearFriendAdapter(Context context, List<User> ts) {
		super(context, ts);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_near_friend_layout, parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		final User user = getItem(position);
		BmobGeoPoint userPoint = user.getPoint();
		//写一个方法计算两个经纬度之间的距离
		//http://www.cnblogs.com/ycsfwhh/archive/2010/12/20/1911232.html
		double distance = getDistance(MyApp.lastPoint,userPoint);
		vh.tvDistance.setText(distance+"米");
		vh.tvUsername.setText(user.getUsername());
		vh.tvLastTime.setText("上一次登录时间："+user.getUpdatedAt());
		String url = user.getAvatar();
		if(TextUtils.isEmpty(url)){
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		}else{
			ImageLoader.getInstance().displayImage(url, vh.ivAvatar);
		}
		vh.btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//单击按钮，向某个用户发送一个"添加好友"的"邀请"(Invitation)
				BmobChatManager.getInstance(context).sendTagMessage("add", user.getObjectId(), new PushListener() {

					@Override
					public void onSuccess() {
						vh.btnAdd.setVisibility(View.INVISIBLE);
						Toast.makeText(context, "已发送", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(context, "服务器繁忙，稍后重试", Toast.LENGTH_SHORT).show();

					}
				});


			}
		});
		return convertView;
	}

	private double EARTH_RADIUS = 6378137;//地球半径（单位：米）
	private double rad(double d){
		return d * Math.PI / 180.0;
	}

	public double getDistance(BmobGeoPoint p1,BmobGeoPoint p2){
		return getDistance(p1.getLatitude(), p1.getLongitude(),p2.getLatitude(),p2.getLongitude());
	}

	public double getDistance(double lat1, double lng1, double lat2, double lng2){
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
				Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public class ViewHolder{
		@Bind(R.id.iv_item_avatar)
		CircleImageView ivAvatar;
		@Bind(R.id.tv_item_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_distance)
		TextView tvDistance;
		@Bind(R.id.tv_item_lasttime)
		TextView tvLastTime;
		@Bind(R.id.btn_item_near_friend_add)
		Button btnAdd;
		public ViewHolder(View view){
			ButterKnife.bind(this,view);
		}
	}

}
