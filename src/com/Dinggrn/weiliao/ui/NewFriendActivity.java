package com.Dinggrn.weiliao.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobInvitation;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.adapter.InvitationAdapter;
import com.Dinggrn.weiliao.constant.Constant.Position;


public class NewFriendActivity extends BaseActivity {
	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.lv_newfriend_invitations)
	ListView listView;
	
	//数据源
	List<BmobInvitation> invitations;
	//适配器
	InvitationAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friend);
		ButterKnife.bind(this);
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {
		setHeaderTitle(headerView, "好友申请",Position.CENTER);
		setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	private void initListView() {
		invitations = new ArrayList<BmobInvitation>();
		adapter = new InvitationAdapter(this, invitations);
		listView.setAdapter(adapter);
		//TODO 添加长按删除
		//listView.setOnItemLongClickListener(listener);
		//1)弹出对话框，提示用户“确实要删除XXX的好友申请吗？”
		//2)从数据库中删除 bmobDB.deleteInviteMsg(invitation.getFromId(), invitation.getTime()+"");
		//删除的时候，是只删除用户点击的那一条？还是把所有相关的邀请都删除掉？
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		//2个test,2个老王
		List<BmobInvitation> list = bmobDB.queryBmobInviteList();
		HashMap<String,BmobInvitation> map = new HashMap<String,BmobInvitation>();
		for(BmobInvitation bi:list){
			map.put(bi.getFromid(), bi);
		}
		list.clear();
		for(Entry<String,BmobInvitation> entry:map.entrySet() ){
			list.add(entry.getValue());
		}
		//现在list中只有两个元素1个test，1个laowang
		adapter.addAll(list,true);
	}


}
