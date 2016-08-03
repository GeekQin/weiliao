package com.Dinggrn.weiliao.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.adapter.RecentAdapter;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.ui.ChatActivity;
import com.Dinggrn.weiliao.ui.MainActivity;
/**
 * 这个界面用一个ListView显示
 * 当前登录用户与所用好友之间聊天内容中最近一条聊天内容
 * 最近一条聊天内容的数据类型是BmobRecent
 * 所以ListView的数据源就是List<BmobRecent>
 * 
 * BmobRecent相关的一些方法：
 * 
 * bmobDB.hasUnReadMsg 当前数据库中，是否有未读的消息
 * 
 * bmobDB.getAllUnReadedCount 当前数据库中，所有未读消息的数量
 * 
 * bmobDB.queryRecents 获得当前数据库中Recent数据表中的所有数据，这些数据被封装为List<BmobRecnt>类型
 * 
 * bmobDB.getUnreadCount(uid) 获得与某一个用户之间的所有未读消息数量。如果是与abc用户之间的未读消息数，那么参数uid传入的就是abc的objectId
 * 
 * bmobDB.deleteRecent(uid) 删除与某一个人之间的最近一条聊天信息。
 *                          比如，要删除与abc这个用户之间的最后一条聊天信息
 *                          那么方法参数uid就是abc这个用户的objecetId
 * bmobDB.deleteMessages(uid) 删除与某一个人之间的全部聊天信息。
 *                           比如，要删除与abc这个用户之间的所有聊天信息
 *                           那么方法参数uid就是abc这个用户的objectId
 * 
 *
 * BmobRecent中还有的一些属性：
 * private String targetid;//目标用户id，比如你与abc这个用户之间的聊天，那么targetID就是abc这个用户的objectId
 * private String userName;//目标用户名，比如你与abc这个用户之间的聊天，那么userName就是abc这个用户的userName
 * private String nick;//目标用户的昵称
 * private String avatar;//目标用户的头像，这个属性的值就是该用户头像在Bmob服务器上存储的地址
 * private String message;//最近这条聊天消息的正文
 * private long time;//最近这条消息发生的时间。需要注意，该属性值是10为long类型，只精确到秒。所以在做日期处理的时候，需要在该数值基础上*1000，转为毫秒值
 * private int type;//最近这条消息的消息类型。该属性取值只可能有四种：1 文本 2 图像 3 位置 4 语音。不同类型的BmobRecent，它的message属性的属性值会不一样
 * 
 * 
 * @author pjy
 *
 */

public class MessageFragment extends BaseFragment{
	
	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.lv_message_messages)
	ListView listView;
	
	List<BmobRecent> recents;//数据源
	RecentAdapter adapter;//适配器
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view  = inflater.inflate(R.layout.fragment_message, container,false);
		ButterKnife.bind(this,view);
		initHeaderView();
		initListView();
		return view;
	}
	
	private void initHeaderView() {
		setHeaderTitle(headerView, "微聊",Position.CENTER);
	}

	private void initListView() {
		recents = new ArrayList<BmobRecent>();
		adapter = new RecentAdapter(getActivity(), recents);
		listView.setAdapter(adapter);
		//TODO 为listView添加单击、长按事件监听器
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//1)更新该会话中所有未读消息-->已读消息
					BmobRecent recent = adapter.getItem(position);
					bmobDB.resetUnread(recent.getTargetid());
				//2)调用MainActivity的setbadgeViewCount方法，更新总的未读消息数
					((MainActivity)getActivity()).setbadgeViewCount();
				//3)跳转到ChatActivity界面，跳转时要传递相关参数
				//  直接将recent所对应的那个用户传递到ChatActivity
					List<BmobChatUser> friends = bmobDB.getContactList();
					for(BmobChatUser bcu:friends){
						if(bcu.getUsername().equals(recent.getUserName())){
							Intent intent = new Intent(getActivity(),ChatActivity.class);
							intent.putExtra("user", bcu);
							jump(intent, false);
							break;
						}
					}
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setIcon(android.R.drawable.ic_menu_info_details);
				builder.setTitle("删除");
				builder.setMessage("您是否要删除与"+adapter.getItem(position).getUserName()+"之间的聊天信息吗？");
				builder.setPositiveButton("删之", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BmobRecent recent = adapter.getItem(position);
						//1)从本地数据库的Recent表中删除
						bmobDB.deleteRecent(recent.getTargetid());
						//2)从本地数据库的Chat表中删除
						bmobDB.deleteMessages(recent.getTargetid());
						//3)从ListView的数据源中删
						adapter.remove(recent);
						//4)更新一下MainActivity的badgeView中的数量
						((MainActivity)getActivity()).setbadgeViewCount();
					}
				});
				
				builder.setNegativeButton("再想想", null);
				
				builder.create().show();
				
				return true;
			}
		});
		
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void refresh() {
		//为数据源添加真正的数据
		//bmobDB.queryRecents
		adapter.addAll(bmobDB.queryRecents(), true);
	}
}
