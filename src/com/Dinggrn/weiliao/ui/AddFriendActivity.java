package com.Dinggrn.weiliao.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.adapter.AddFriendAdapter;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class AddFriendActivity extends BaseActivity {
	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.et_addfriend_username)
	EditText etUsername;
	@Bind(R.id.lv_addfriend_users)
	PullToRefreshListView ptrListView;

	ListView listView;

	//如果使用CircularProgressButton需要声明2个属性

	//如果使用了PrgressBar的需要声明一个属性

	ProgressDialog pd;//加载过程中，给用户一个提示

	//数据源
	List<BmobChatUser> users;
	//适配器 AddFriendAdapter
	AddFriendAdapter adapter;
	int page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		ButterKnife.bind(this);
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {

		setHeaderTitle(headerView, "添加好友", Position.CENTER);

		setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void initListView() {
		listView = ptrListView.getRefreshableView();
		users = new ArrayList<BmobChatUser>();
		adapter= new AddFriendAdapter(this, users);
		listView.setAdapter(adapter);
		
		//为ptrListView设置一个上推监听器
		ptrListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				page += 1;
				
				bmobUserManager.queryUserByPage(
						true, 
						page, 
						etUsername.getText().toString(), 
						new FindListener<BmobChatUser>() {

							@Override
							public void onError(int arg0, String arg1) {
								ptrListView.onRefreshComplete();
								
							}

							@Override
							public void onSuccess(List<BmobChatUser> arg0) {
								ptrListView.onRefreshComplete();
								if(arg0.size()>0){
									adapter.addAll(arg0, false);
								}else{
									//这一页查询已经没有更多的数据了
									//也就没有必要再继续上推加载更多了
									//禁止上推
									ptrListView.setMode(Mode.DISABLED);
									toast("没有多更用户了...");
								}
								
							}
						});
				
			}
		});
		
		//为listView添加一个条目单击监听器，点击跳转到UserInfoActivity
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AddFriendActivity.this,UserInfoActivity.class);
				intent.putExtra("from", "stranger");
				intent.putExtra("username",adapter.getItem(position-1).getUsername());
				jump(intent, false);
			}
		});
		
	}

	/**
	 * 用户名精确搜索
	 * @param v
	 */
	@OnClick(R.id.btn_addfriend_search)
	public void search(View v){
		//1)BmobUserManager的方法queryUserByName(name,监听器<BmobChatUser>);
		//2)BmobUserManager的方法queryUser(name,监听器<T>);
		if(isEmpty(etUsername)){
			return;
		}else{
			//给用户一个提示，正在工作中
			if(pd==null){
				pd = ProgressDialog.show(AddFriendActivity.this, "", "正在搜索中，请稍后...");
			}
			pd.show();
			
			ptrListView.setMode(Mode.DISABLED);
			
			bmobUserManager.queryUserByName(etUsername.getText().toString(), new FindListener<BmobChatUser>() {

				@Override
				public void onSuccess(List<BmobChatUser> arg0) {
					pd.dismiss();
					if(arg0.size()>0){
						adapter.addAll(arg0, true);
					}else{
						toast("查无此人");
					}
				}

				@Override
				public void onError(int arg0, String arg1) {
					pd.dismiss();
					toastAndLog("服务器繁忙稍后重试", arg0+": "+arg1);
				}
			});
		}
	}

	/**
	 * 用户名的模糊搜索
	 * @param v
	 */
	@OnClick(R.id.btn_addfriend_searchmore)
	public void searchMore(View v){
		//判空
		if(isEmpty(etUsername)){
			return;
		}
		//显示提示框
		if(pd==null){
			pd = ProgressDialog.show(this, "", "正在搜索中，请稍后...");
		}
		pd.show();
		//设置页码从第一页开始查询
		page = 0;
		ptrListView.setMode(Mode.PULL_FROM_END);
		//真正发起查询
		bmobUserManager.queryUserByPage(
				false, 
				page, 
				etUsername.getText().toString(), 
				new FindListener<BmobChatUser>() {

					@Override
					public void onError(int arg0, String arg1) {
						pd.dismiss();
						toastAndLog("查询失败，稍后重试", arg0+": "+arg1);

					}

					@Override
					public void onSuccess(List<BmobChatUser> arg0) {
						pd.dismiss();
						if(arg0.size()>0){
							adapter.addAll(arg0, true);
						}else{
							toast("查无此人");
						}

					}
				});
	}

}
