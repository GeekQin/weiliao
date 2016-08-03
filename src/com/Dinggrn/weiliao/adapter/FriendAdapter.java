package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.bean.User;
import com.nostra13.universalimageloader.core.ImageLoader;


public class FriendAdapter extends MyBaseAdapter<User> implements SectionIndexer{
	
	public FriendAdapter(Context context, List<User> friends) {
		super(context,friends);
	}
	
	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder vh;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_friend_layout, parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		User user = getItem(position);
		//显示头像
		if(TextUtils.isEmpty(user.getAvatar())){
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		}else{
			ImageLoader.getInstance().displayImage(user.getAvatar(), vh.ivAvatar);
		}
		//显示用户名
		vh.tvUsername.setText(user.getUsername());
		
		//姓名的分组字母是否显示
		if(position==getPositionForSection(
				     getSectionForPosition(
						position))){
			//显示
			vh.tvSortLetter.setVisibility(View.VISIBLE);
			vh.tvSortLetter.setText(user.getSortLetter());
		}else{
			//不显示
			vh.tvSortLetter.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	
	public class ViewHolder{
		
		@Bind(R.id.tv_item_friend_index)
		TextView tvSortLetter;
		@Bind(R.id.iv_item_friend_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_frined_usernmae)
		TextView tvUsername;
		
		public ViewHolder(View convertView) {
			
			ButterKnife.bind(this,convertView);
		
		}
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		for(int i=0;i<getCount();i++){
			if(section==getSectionForPosition(i)){
				return i;
			}
		}
		return -1;
	}
    
	@Override
	public int getSectionForPosition(int position) {
		//position位置的User的sortLetter属性所对应的char
		return getItem(position).getSortLetter();
	}






}
