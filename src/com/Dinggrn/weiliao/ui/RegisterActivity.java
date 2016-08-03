package com.Dinggrn.weiliao.ui;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.im.util.BmobNetUtil;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.util.PinYinUtil;
import com.Dinggrn.weiliao.view.NumberProgressBar;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.dd.CircularProgressButton;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RegisterActivity extends BaseActivity {
	
	@Bind(R.id.headerview)
	View headerView;
	
	@Bind(R.id.et_regist_username)
	EditText etUsername;
	
	@Bind(R.id.et_regist_password)
	EditText etPassword;
	
	@Bind(R.id.et_regist_repassword)
	EditText etRePassword;//再次输入密码
	
	@Bind(R.id.rg_regist_gendergroup)
	RadioGroup rgGender;
	
	@Bind(R.id.btn_regist_regist)
	CircularProgressButton cpbRegist;//注册按钮
	
	@Bind(R.id.iv_uimage)
	ImageView ivAvatar;
	@Bind(R.id.npb_userinfo_progressbar)
	NumberProgressBar npb;
	String camPath;
	String filePath;
	String avatarUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.bind(this);
		//数字进度条初始不可见
		npb.setVisibility(View.INVISIBLE);
		initHeaderView();
		
	}


	private void initHeaderView() {

		setHeaderTitle(headerView, "新用户");
		setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT,new OnClickListener() {
			@Override
			public void onClick(View v) {
				jump(LoginActivity.class,true);
			}
		});
	}
	/**
	 * 设置头像
	 */
	@OnClick(R.id.set_avatar)
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
	/**
	 * 注册用户
	 * @param v
	 */
	@OnClick(R.id.btn_regist_regist)
	public void registUser(View v){
		//1)判空
		if(isEmpty(etUsername,etPassword,etRePassword)){
			return;
		}
		//2)两次密码是否一致
		if(!etPassword.getText().toString().equals(etRePassword.getText().toString())){
			toast("两次输入密码不一致，请重新输入");
			etRePassword.setText("");
			etPassword.setText("");
			return;
		}
		//3)看看有没有网络
		if(!BmobNetUtil.isNetworkAvailable(this)){
			toast("网络不可用，请检查网络设置");
			return;
		}
		//4)进行联网注册
		
		//改变cpbRegist按钮的状态，从正常状态--->运行中状态
		
		cpbRegist.setIndeterminateProgressMode(true);
		cpbRegist.setProgress(50);
		
		final User user = new User();
		user.setUsername(etUsername.getText().toString());
		user.setPassword(etPassword.getText().toString());//MD5加密？随你便
		if(rgGender.getCheckedRadioButtonId()==R.id.rb_regist_boy){
			user.setGender(true);
		}else{
			user.setGender(false);
		}
		//pyname
		user.setPyname(PinYinUtil.getPinYin(user.getUsername()));
		user.setSortLetter(user.getPyname().charAt(0));
		//设定当前用户使用的设备类型，只能输入“android”或“ios”
		user.setDeviceType("android");
		//把当前设备的ID与用户进行一个绑定
		user.setInstallId(BmobInstallation.getInstallationId(this));
		//真正向服务器进行注册
		user.signUp(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				//cpbReigst从运行中状态--->运行结束成功状态
				cpbRegist.setProgress(100);
				//1)要在设备表(_Installation)中，将当前登录用户的用户名与设备ID再进行一次绑定
				//bindInstallationForRegister(user.getUsername())该方法执行也就意味着完成了“登录”操作
				bmobUserManager.bindInstallationForRegister(user.getUsername());
				//2)更新用户的位置(用MyApp.lastPoint设置用户的point属性的值)
				updateUserLocation(user);
				//3)用户头像的绑定
				user.setAvatar(avatarUrl);
				//4)界面跳转到MainActivity
				jump(LoginActivity.class,true);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				//cpbRegist从运行中状态-->运行结束失败状态
				cpbRegist.setProgress(-1);
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					
					@Override
					public void run() {
						cpbRegist.setProgress(0);
					}
				}, 1500);
				
				switch (arg0) {
				case 202:
				case 401:
					toastAndLog("该用户名已存在", arg0+": "+arg1);
					break;

				default:
					toastAndLog("注册失败，稍后重试", arg0+": "+arg1);
					break;
				}
				
			}
		});
	}
	
	/**
	 * 点击系统的BACK键会调用该方法
	 */
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		jump(LoginActivity.class, true);
	}
	

}
