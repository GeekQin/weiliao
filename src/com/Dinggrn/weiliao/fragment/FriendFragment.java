package com.Dinggrn.weiliao.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.adapter.FriendAdapter;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.ui.AddFriendActivity;
import com.Dinggrn.weiliao.ui.BaseActivity;
import com.Dinggrn.weiliao.ui.NearFriendActivity;
import com.Dinggrn.weiliao.ui.NewFriendActivity;
import com.Dinggrn.weiliao.ui.TestActivity;
import com.Dinggrn.weiliao.ui.UserInfoActivity;
import com.Dinggrn.weiliao.util.PinYinUtil;
import com.Dinggrn.weiliao.view.MyLetterView;
import com.Dinggrn.weiliao.view.MyLetterView.OnTouchLetterListener;


public class FriendFragment extends BaseFragment{
	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.lv_friend_frineds)
	ListView listView;
	@Bind(R.id.tv_friend_letter)
	TextView tvLetter;//ListView上方覆盖的大字母
	@Bind(R.id.mlv_friend_letters)
	MyLetterView mlvLetters;//快速索引的自定义View
	/*@Bind(R.id.menuview)
	View menuview;*/
	//因为在ListView中显示friends的时候
	//需要把friends中的用户按照用户名的拼音格式进行排序
	//而仅仅User这种类型是有pyname这个属性
	//数据源要使用User这种类型
	List<User> friends;//数据源
	FriendAdapter adapter;//适配器
	
	
	ImageView ivNewFriendTips;//ListView的headerView中的小红点
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view  = inflater.inflate(R.layout.fragment_friend, container,false);
		ButterKnife.bind(this, view);
		initHeaderView();
		initListView();
		//mlvLetters添加监听器
		mlvLetters.setListener(new OnTouchLetterListener() {
			
			@Override
			public void onTouchLetter(String str) {
				//1)挪动ListView
				listView.setSelection(adapter.getPositionForSection(str.charAt(0)));
				//2)显示大字母
				tvLetter.setVisibility(View.VISIBLE);
				tvLetter.setText(str);
			}

			@Override
			public void onFinishTouch() {
				tvLetter.setVisibility(View.INVISIBLE);
			}
		});
		return view;
	}


	private void initListView() {
		friends = new ArrayList<User>();
		adapter = new FriendAdapter(getActivity(), friends);
		//为ListView添加一个headerview
		View listHeader = getActivity().getLayoutInflater().inflate(R.layout.listview_header_friend_layout,listView,false);
		View llNewFriend = listHeader.findViewById(R.id.ll_listview_header_newfriend);
		View llNearFriend = listHeader.findViewById(R.id.ll_header_listview_near);
		llNewFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jump(NewFriendActivity.class, false);
			}
		});
		llNearFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jump(TestActivity.class, false);
			}
		});
		
		listView.addHeaderView(listHeader);
		listView.setAdapter(adapter);
		ivNewFriendTips = (ImageView) listHeader.findViewById(R.id.iv_friend_newfriend_tips);
	
		//listView添加条目单击监听，跳转到UserInfoActivity
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),UserInfoActivity.class);
				intent.putExtra("from", "friend");
				//根据你构建界面的不同而不同
				//我是为ListView添加了一个Header
				//所以在取数据的时候，position-1
				intent.putExtra("username", adapter.getItem(position-1).getUsername());
				jump(intent, false);
			}
		});
		//listView添加条目长按监听，弹出提示对话框询问是否删除好友
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setIcon(android.R.drawable.ic_menu_info_details);
				builder.setTitle("通知");
				builder.setMessage("您确实要删除"+adapter.getItem(position-1).getUsername()+"嘛?");
				builder.setNegativeButton("留着吧", null);
				builder.setPositiveButton("删之", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						bmobUserManager.deleteContact(adapter.getItem(position-1).getObjectId(), new UpdateListener() {
							
							@Override
							public void onSuccess() {
								
								log("删除用户的id------》"+adapter.getItem(position-1).getObjectId());
								
								//目前，好友仅仅是从服务器和本地数据库中删除掉了
								//但是ListView中数据是来自于数据源的
								//为了更新ListView的显示，必须更新数据源
								adapter.remove(adapter.getItem(position-1));
								((BaseActivity)getActivity()).toast("删除成功");
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								((BaseActivity)getActivity()).toastAndLog("删除失败", arg0+": "+arg1);
							}
						});
						
					}
				});
				
				builder.create().show();
				
				return true;
			}
		});
	}

	private void initHeaderView() {
		setHeaderTitle(headerView, "微聊", Position.CENTER);
//		menuview.setVisibility(View.INVISIBLE);
		//设置右侧图标
		setHeaderImage(headerView, R.drawable.add_normal, Position.RIGHT, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*if(menuview.getVisibility()==View.VISIBLE){
					menuview.setVisibility(View.INVISIBLE);
				}else{
					menuview.setVisibility(View.VISIBLE);
				}*/
				jump(AddFriendActivity.class, false);
			}
			
		});
	/*	//设置目录的点击事件
		if(menuview.getVisibility()==View.VISIBLE){
			menuview.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch(getId()){
					case R.id.togetherchat:
						// 点击跳转到“添加好友”界面AddFriendActivity
					case R.id.add_newfriend:
						jump(AddFriendActivity.class, false);
						break;
					case R.id.scan:
					case R.id.feedback:
						break;
					}
				}
			});
		}*/
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
		//控制小红点的显示
		hasNewInvitation();
	}
	
	public void hasNewInvitation() {
		if(bmobDB.hasNewInvite()){
			ivNewFriendTips.setVisibility(View.VISIBLE);
		}else{
			ivNewFriendTips.setVisibility(View.INVISIBLE);
		}
	}


	/**
	 * 真正去刷新好友数据
	 */
	private void refresh() {
		//当前登录用户的好友列表在服务器上有一份
		bmobUserManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
			
			@Override
			public void onSuccess(List<BmobChatUser> list) {
				friends.clear();
				for(BmobChatUser bcu:list){
					User user = new User();
					user.setObjectId(bcu.getObjectId());
					user.setUsername(bcu.getUsername());
					user.setAvatar(bcu.getAvatar());
					user.setPyname(PinYinUtil.getPinYin(user.getUsername()));
					user.setSortLetter(user.getPyname().charAt(0));
					friends.add(user);
				}
				//对friends中的数据根据pyname进行排序
				Collections.sort(friends, new Comparator<User>() {

					@Override
					public int compare(User lhs, User rhs) {
						return lhs.getPyname().compareTo(rhs.getPyname());
					}
				});
				//将排序好的数据在ListView中显示
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		//在本地数据库中也有一份
		//bmobDB.getContactList();
		//无论用哪种方式查询，查询回来的数据类型是BmobChatUser类型
		//List<BmobChatUser> list = bmobDB.getContactList();
		//根据返回的list，自己构建friends中的数据
		
	}
}
