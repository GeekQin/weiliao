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
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

import com.Dinggrn.weiliao.R;
import com.dd.CircularProgressButton;
import com.nostra13.universalimageloader.core.ImageLoader;

public class InvitationAdapter extends MyBaseAdapter<BmobInvitation>{

	public InvitationAdapter(Context context, List<BmobInvitation> datasource) {
		super(context, datasource);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_newfriend_layout,parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		final BmobInvitation invitation = getItem(position);
		if(TextUtils.isEmpty(invitation.getAvatar())){
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		}else{
			ImageLoader.getInstance().displayImage(invitation.getAvatar(), vh.ivAvatar);
		}
		vh.tvUsername.setText(invitation.getFromname());
		
		int status = invitation.getStatus();
		if(status == BmobConfig.INVITE_ADD_AGREE){
			vh.tvAdded.setVisibility(View.VISIBLE);
			vh.btnAdd.setVisibility(View .INVISIBLE);
		}else{
			vh.tvAdded.setVisibility(View.INVISIBLE);
			vh.btnAdd.setVisibility(View.VISIBLE);
		}
		
		//点击按钮，添加对方为好友
		vh.btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vh.btnAdd.setIndeterminateProgressMode(true);
				vh.btnAdd.setProgress(50);
				BmobUserManager.getInstance(context).agreeAddContact(invitation, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						vh.btnAdd.setProgress(100);
						
						String objectId = invitation.getFromid();
						List<BmobInvitation> list = BmobDB.create(context).queryBmobInviteList();
						for(BmobInvitation b:list){
							if(b.getFromid().equals(objectId)){
								b.setStatus(BmobConfig.INVITE_ADD_AGREE);
							}
						}
						notifyDataSetChanged();
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
		
		@Bind(R.id.iv_item_newfriend_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_newfrined_usernmae)
		TextView tvUsername;
		@Bind(R.id.btn_item_newfriend_add)
		CircularProgressButton btnAdd;
		@Bind(R.id.tv_item_newfriend_added)
		TextView tvAdded;
		
		public ViewHolder(View convertView) {
			ButterKnife.bind(this,convertView);
		}
	}
	

}
