package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.util.ChatUtil;
import com.Dinggrn.weiliao.util.TimeUtil;
import com.Dinggrn.weiliao.view.BadgeView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RecentAdapter extends MyBaseAdapter<BmobRecent>{

	public RecentAdapter(Context context, List<BmobRecent> datasource) {
		super(context, datasource);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder vh;
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_recent_layout, parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		BmobRecent recent = getItem(position);
		
		//显示头像
		if(TextUtils.isEmpty(recent.getAvatar())){
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		}else{
			ImageLoader.getInstance().displayImage(recent.getAvatar(), vh.ivAvatar);
		}
		//显示用户名
		vh.tvUsername.setText(recent.getUserName());
		//TODO 显示消息正文
		//vh.tvMessage.setText(recent.getMessage());

		int type = recent.getType();
		switch (type) {
		case 1:
			//文本类型
			//要显示消息正文中的表情，需要使用ChatUtil来进行处理
			//将普通的String变为可以放入表情图像的SpannableString
			vh.tvMessage.setText(ChatUtil.getSpannableString(recent.getMessage()));
			break;
		case 2:
			vh.tvMessage.setText("[图片]");
			break;
		case 3:
			vh.tvMessage.setText("[位置]");
			break;
		case 4:
			vh.tvMessage.setText("[语音]");
			break;
		default:
			break;
		}
		
		//显示时间
		vh.tvMsgtime.setText(TimeUtil.getTime(recent.getTime()));
		//显示未读消息的数量
		vh.bvUnreadCount.setBadgeCount(BmobDB.create(context).getUnreadCount(recent.getTargetid()));
		return convertView;
	}
	
	public class ViewHolder{
		
		@Bind(R.id.iv_item_recent_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_recent_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_recent_msgtime)
		TextView tvMsgtime;
		@Bind(R.id.tv_item_recent_message)
		TextView tvMessage;
		@Bind(R.id.bv_item_recent_unreadcount)
		BadgeView bvUnreadCount;
		
		public ViewHolder(View convertView) {
			ButterKnife.bind(this,convertView);
		}
		
		
	}
}
