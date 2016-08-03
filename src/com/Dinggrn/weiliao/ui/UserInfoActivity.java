package com.Dinggrn.weiliao.ui;

import java.io.File;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.view.NumberProgressBar;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends BaseActivity {
	
	String from;//me,friend,stranger代表着从不同地方来
	String username;//用户名
	
	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.iv_userinfo_avatar)
	ImageView ivAvatar;//头像
	@Bind(R.id.iv_userinfo_avatareidtor)
	ImageView ivAvatarEditor;//编辑头像的铅笔
	
	@Bind(R.id.tv_userinfo_nickname)
	TextView tvNickname;//显示昵称的TextView
	@Bind(R.id.iv_userinfo_nicknameeidtor)
	ImageView ivNicknameEditor;//编辑昵称的铅笔
	@Bind(R.id.ll_userinfo_editnicknamecontainer)
	View nicknameEditContainer;//LinearLayout，里面放着编辑昵称的EditText和两个按钮
	@Bind(R.id.et_userinfo_eidtnickname)
	EditText etNickname;//昵称的编辑框
	@Bind(R.id.btn_userinfo_savenickname)
	ImageButton btnSaveNickname;//保存昵称按钮
	@Bind(R.id.btn_userinfo_cancelnickname)
	ImageButton btnCancelNickname;//放弃编辑昵称
	
	@Bind(R.id.tv_userinfo_username)
	TextView tvUsername;//显示用户名
	
	@Bind(R.id.iv_userinfo_gender)
	ImageView ivGender;//显示性别
	
	@Bind(R.id.btn_userinfor_updateinfo)
	Button btnUpdateInfo;//更新用户资料
	
	@Bind(R.id.btn_userinfor_startchat)
	Button btnStartChat;//开始聊天
	
	@Bind(R.id.btn_userinfor_addblack)
	Button btnAddBlack;//加入黑名单
	
	@Bind(R.id.npb_userinfo_progressbar)
	NumberProgressBar npb;
	
	ProgressDialog pd;
	String camPath;
	String filePath;
	String avatarUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ButterKnife.bind(this);
		from = getIntent().getStringExtra("from");
		if("me".equals(from)){
			username = bmobUserManager.getCurrentUserName();
		}else{
			username = getIntent().getStringExtra("username");
		}
		initHeaderView();
		initView();
		
	}


	private void initHeaderView() {
		String text = "";
		if("me".equals(from)){
			text = "我的资料";
		}else{
			text = username;
		}
		setHeaderTitle(headerView, text, Position.CENTER);
		
		setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	
	}


	private void initView() {
		//给用户提示正在加载username对应的用户信息
		if(pd==null){
			pd = ProgressDialog.show(this, "", "正在加载用户信息，请稍后...");
		}
		pd.show();
		//1)要根据username--->获得对应的User对象
		bmobUserManager.queryUser(username, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				pd.dismiss();
				toastAndLog("加载失败，稍后重试", arg0+":"+arg1);
			}

			@Override
			public void onSuccess(List<User> arg0) {
				//2)根据User对象中的属性，放到对应的视图中显示
				pd.dismiss();
				User user = arg0.get(0);
				showUserInfo(user);
			}
			
		});
		
		if("me".equals(from)){
			ivAvatarEditor.setVisibility(View.VISIBLE);
			ivNicknameEditor.setVisibility(View.VISIBLE);
		}else{
			ivAvatarEditor.setVisibility(View.INVISIBLE);
			ivNicknameEditor.setVisibility(View.INVISIBLE);
		}
		
		if("me".equals(from)){
			btnUpdateInfo.setVisibility(View.VISIBLE);
		}else{
			btnUpdateInfo.setVisibility(View.INVISIBLE);
		}
		
		if("friend".equals(from)){
			btnStartChat.setVisibility(View.VISIBLE);
			btnAddBlack.setVisibility(View.VISIBLE);
		}else{
			btnStartChat.setVisibility(View.INVISIBLE);
			btnAddBlack.setVisibility(View.INVISIBLE);
		}
		//数字进度条初始不可见
		npb.setVisibility(View.INVISIBLE);
		
	}

	/**
	 * 把用户信息放到界面中各个视图呈现
	 * @param user
	 */
	protected void showUserInfo(User user) {
		//头像
		if(TextUtils.isEmpty(user.getAvatar())){
			ivAvatar.setImageResource(R.drawable.ic_launcher);
		}else{
			ImageLoader.getInstance().displayImage(user.getAvatar(), ivAvatar);
		}
		//昵称
		tvNickname.setText(user.getNick());
		//用户名
		tvUsername.setText(user.getUsername());
		//性别
		if(user.getGender()){
			ivGender.setImageResource(R.drawable.boy);
		}else{
			ivGender.setImageResource(R.drawable.girl);
		}
	}
	
	@OnClick(R.id.iv_userinfo_nicknameeidtor)
	public void setNickname(View v){
		
		tvNickname.setVisibility(View.INVISIBLE);
		String nickname = tvNickname.getText().toString();
		
		nicknameEditContainer.setVisibility(View.VISIBLE);
		if(!TextUtils.isEmpty(nickname)){
			etNickname.setText(nickname);
		}
	}
	
	@OnClick(R.id.btn_userinfo_savenickname)
	public void saveNickname(View v){
		String nickname = etNickname.getText().toString();
		etNickname.setText("");
		nicknameEditContainer.setVisibility(View.INVISIBLE);
		tvNickname.setVisibility(View.VISIBLE);
		tvNickname.setText(nickname);
	}
	
	@OnClick(R.id.btn_userinfo_cancelnickname)
	public void cancelNickname(View v){
		etNickname.setText("");
		nicknameEditContainer.setVisibility(View.INVISIBLE);
		tvNickname.setVisibility(View.VISIBLE);
	}
	
	@OnClick(R.id.iv_userinfo_avatareidtor)
	public void setAvatar(View v){
		
		Intent intent1 = new Intent(Intent.ACTION_PICK);
		intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		
		Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".png");
		camPath = file.getAbsolutePath();
		Uri imageUri = Uri.fromFile(file );
		intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri );
		
		Intent chooser = Intent.createChooser(intent1, "选择头像");
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent2});
		startActivityForResult(chooser, 101);
		
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0==101 && arg1==RESULT_OK){
			if(arg2!=null){
				Uri uri = arg2.getData();
				Cursor c = getContentResolver().query(uri,new String[]{MediaStore.Images.Media.DATA}, null,null,null);
				c.moveToNext();
				filePath = c.getString(0);
				c.close();
			}else{
				filePath = camPath;
			}
			//数字进度条可见
			npb.setVisibility(View.VISIBLE);
			BmobProFile.getInstance(this).upload(filePath, new UploadListener() {
				
				@Override
				public void onError(int arg0, String arg1) {
					toast("头像上传失败，稍后重试");
					avatarUrl="";
					npb.setVisibility(View.INVISIBLE);
					npb.setProgress(0);
				}
				
				@Override
				public void onSuccess(String arg0, String arg1, BmobFile arg2) {
					avatarUrl = arg2.getUrl();
					ImageLoader.getInstance().displayImage(avatarUrl, ivAvatar);
					npb.setVisibility(View.INVISIBLE);
					npb.setProgress(0);
				}
				
				@Override
				public void onProgress(int arg0) {
					npb.setPrefix("已上传");
					npb.setProgress(arg0);
					npb.setSuffix("啦！");
				}
			});
		}
	}
	
	@OnClick(R.id.btn_userinfor_updateinfo)
	public void updateInfo(View v){
		if(pd==null){
			pd = ProgressDialog.show(this, "","正在加载用户信息，请稍后..." );
		}
		pd.show();
		User newuser = new User();
		newuser.setAvatar(avatarUrl);
		newuser.setNick(tvNickname.getText().toString());
		newuser.update(this, bmobUserManager.getCurrentUserObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				pd.dismiss();
				toast("资料更新完毕");
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				pd.dismiss();
				toastAndLog("资料更新失败",arg0+": "+arg1);
			}
		});
		
	}
	
	
	@OnClick(R.id.btn_userinfor_startchat)
	public void startChat(View v){
		//根据传递过来的用户名，获得该用户名对应的用户
		//将该用户传递到ChatActivity中
		List<BmobChatUser> friends = bmobDB.getContactList();
		for(BmobChatUser bcu:friends){
			if(bcu.getUsername().equals(username)){
				Intent intent = new Intent(this,ChatActivity.class);
				intent.putExtra("user", bcu);
				jump(intent, true);
				break;
			}
		}
		
	}
}
