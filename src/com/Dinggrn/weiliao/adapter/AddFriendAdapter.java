package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.PushListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.ui.BaseActivity;
import com.dd.CircularProgressButton;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AddFriendAdapter extends MyBaseAdapter<BmobChatUser>{
	
	public AddFriendAdapter(Context context, List<BmobChatUser> datasource) {
		super(context, datasource);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder vh;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_addfriend_layout, parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		final BmobChatUser user = getItem(position);
		//加载头像
		if(TextUtils.isEmpty(user.getAvatar())){
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		}else{
			ImageLoader.getInstance().displayImage(user.getAvatar(), vh.ivAvatar);
		}
		//设置用户名
		vh.tvUsername.setText(user.getUsername());
		vh.tvAdded.setVisibility(View.INVISIBLE);
		vh.btnAdd.setVisibility(View.VISIBLE);
		
		//TODO 为按钮添加单击事件监听器
		vh.btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vh.btnAdd.setIndeterminateProgressMode(true);
				vh.btnAdd.setProgress(50);
				//开始发送tag为"add"的添加好友消息
				BmobChatManager.getInstance(context).sendTagMessage(
						"add", 
						user.getObjectId(),
						new PushListener() {
							
							@Override
							public void onSuccess() {
								vh.btnAdd.setProgress(100);
								vh.btnAdd.setVisibility(View.INVISIBLE);
								vh.tvAdded.setVisibility(View.VISIBLE);
								if(context instanceof BaseActivity){
									((BaseActivity)context).toast("好友申请已发送");
								}
								
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								vh.btnAdd.setProgress(-1);
								//TODO
								
							}
						});
				
			}
		});
		
		return convertView;
	}
	
	public class ViewHolder{
		@Bind(R.id.iv_item_addfriend_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_addfrined_usernmae)
		TextView tvUsername;
		@Bind(R.id.btn_item_addfriend_add)
		CircularProgressButton btnAdd;
		@Bind(R.id.tv_item_addfriend_added)
		TextView tvAdded;
		
		public ViewHolder(View convertView) {
			ButterKnife.bind(this,convertView);
		}
	}

}
