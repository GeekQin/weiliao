package com.Dinggrn.weiliao.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.bean.Blog;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.view.NumberProgressBar;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;

public class PostBlogActivity extends BaseActivity {
	@Bind(R.id.headerview)
	View headerView;//头布局
	@Bind(R.id.et_postblog_content)
	EditText etContent;//输入blog文本内容

	@Bind(R.id.iv_postblog_blogimg0)
	ImageView ivBlogImg0;//显示第一张blog配图
	@Bind(R.id.iv_postblog_blogimg1)
	ImageView ivBlogImg1;//显示第二张blog配图
	@Bind(R.id.iv_postblog_blogimg2)
	ImageView ivBlogImg2;//显示第三张blog配图
	@Bind(R.id.iv_postblog_blogimg3)
	ImageView ivBlogImg3;//显示第四张blog配图

	@Bind(R.id.iv_postblog_delimg0)
	ImageView ivDelImg0;//删除第一张blog配图的小红叉
	@Bind(R.id.iv_postblog_delimg1)
	ImageView ivDelImg1;//删除第二张blog配图的小红叉
	@Bind(R.id.iv_postblog_delimg2)
	ImageView ivDelImg2;//删除第三张blog配图的小红叉
	@Bind(R.id.iv_postblog_delimg3)
	ImageView ivDelImg3;//删除第四张blog配图的小红叉

	@Bind(R.id.tv_postblog_picnumber)
	TextView tvPicNumber;//显示已经添加了多少副图片
	@Bind(R.id.npb_postblog_progressbar)
	NumberProgressBar npbProgressBar;//上传blog的进度

	@Bind(R.id.btn_postblog_plus)
	ImageButton ibPlus;//加号按钮

	@Bind(R.id.btn_postblog_addpicture)
	ImageButton ibPicture;//添加图片按钮
	@Bind(R.id.btn_postblog_addphoto)
	ImageButton ibCamera;//拍照按钮
	@Bind(R.id.btn_postblog_addlocation)
	ImageButton ibLocation;//添加位置按钮

	List<ImageView> ivBlogImgs;//用来装载四个显示blog配图的IamgeView
	List<ImageView> ivDelImgs;//用来装载四个删除blog配图的IamgeView

	boolean isExpanded;//当前图片拍照位置这三个按钮是否可见
	boolean isAniming;//图片拍照位置这三个按钮是否处于变化的动画中
	boolean isPosting;//当前是否正在上传Blog
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_blog);
		ButterKnife.bind(this);
		initView();
		initHeaderView();

	}

	private void initHeaderView() {
		setHeaderTitle(headerView, " ", Position.CENTER);
		setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		setHeaderImage(headerView, R.drawable.ic_upload,Position.RIGHT, new OnClickListener() {

			@Override
			public void onClick(View v) {
				//上传blog
				if(isPosting){
					return;
				}
				if(!TextUtils.isEmpty(etContent.getText().toString())||ivBlogImg0.getVisibility()==View.VISIBLE){
					isPosting = true;
					postPicture();//开始上传blog的配图
				}else{
					toast("跟大家分享些内容吧^.^");
				}

			}
		});
	}

	/**
	 * 上传blog的配图
	 */
	protected void postPicture() {
		if(ivBlogImg0.getVisibility()!=View.VISIBLE){
			postContent("");
			return;
		}
		
		List<String> paths = new ArrayList<String>();
		for(int i=0;i<ivBlogImgs.size();i++){
			if(ivBlogImgs.get(i).getVisibility()==View.VISIBLE){
				paths.add((String) ivBlogImgs.get(i).getTag());
			}
		}
		//获得当前所有blog配图在本地设备上的地址
		String[] filePaths = paths.toArray(new String[paths.size()]);
		//进度条显示
		npbProgressBar.setVisibility(View.VISIBLE);
		npbProgressBar.setProgress(0);
		tvPicNumber.setText("开始上传");
		
		BmobProFile.getInstance(this).uploadBatch(filePaths, new UploadBatchListener() {
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO 
				
			}
			/**
			 * 每上传一张图片到服务器成功后，onSuccess都会被回调一次
			 * 只不过，每次换掉时，arg0都会传入false，只有最后一张图也上传成功了，arg0才会是true
			 * arg1:上传的每一副图像的文件名
			 * arg2:上传的每一副图像在服务器上的url，该url不能直接使用，需要经过MD5加密才可以
			 * arg3:BmobFile[]，里面包含了可以直接使用的，图片在Bmob服务器上的地址
			 */
			@Override
			public void onSuccess(boolean arg0, String[] arg1, String[] arg2,
					BmobFile[] arg3) {
				if(arg0==true){
					//所有的图片都上传成功了
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<arg3.length;i++){
						sb.append(arg3[i].getUrl()).append("&");
					}
					//获得所有上传成功的图像地址拼接而成的字符串
					String urls = sb.substring(0,sb.length()-1);
					npbProgressBar.setVisibility(View.INVISIBLE);
					postContent(urls);
				}
				
			}
			/**
			 * arg0:表示当前是第几个文件在上传
			 * arg1:是当前这个文件上传的进度
			 * arg2:表示一共要上传多少个文件
			 * arg3:上传的总体进度
			 */
			@Override
			public void onProgress(int arg0, int arg1, int arg2, int arg3) {
				npbProgressBar.setProgress(arg3); 
			}
		});
		
	}

	/**
	 * 将一个完整的Blog对象保存到Bmob服务器
	 * @param urls
	 */
	private void postContent(String urls) {
		 Blog blog = new Blog();
		 blog.setUser(bmobUserManager.getCurrentUser(User.class));
		 blog.setImages(urls);
		 blog.setContent(etContent.getText().toString());
		 blog.setLove(0);
		 
		 blog.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				toast("博客已发布");
				isPosting = false;
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("发布失败，请重试", arg0+": "+arg1);
				isPosting = false;
			}
		});
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		ivBlogImgs = new ArrayList<ImageView>();
		ivBlogImgs.add(ivBlogImg0);
		ivBlogImgs.add(ivBlogImg1);
		ivBlogImgs.add(ivBlogImg2);
		ivBlogImgs.add(ivBlogImg3);

		ivDelImgs = new ArrayList<ImageView>();
		ivDelImgs.add(ivDelImg0);
		ivDelImgs.add(ivDelImg1);
		ivDelImgs.add(ivDelImg2);
		ivDelImgs.add(ivDelImg3);

		for(int i=0;i<4;i++){
			ivBlogImgs.get(i).setVisibility(View.INVISIBLE);
			ivDelImgs.get(i).setVisibility(View.INVISIBLE);
		}

		tvPicNumber.setText("");

		npbProgressBar.setVisibility(View.INVISIBLE);

		ibPicture.setVisibility(View.INVISIBLE);
		ibCamera.setVisibility(View.INVISIBLE);
		ibLocation.setVisibility(View.INVISIBLE);
	}

	/**
	 * 点击“加号”按钮，切换另外三个按钮可见还是不可见
	 * @param v
	 */
	@OnClick(R.id.btn_postblog_plus)
	public void switchButtons(View v){
		//如果正处于按钮变化的动画中，则点击“加号”按钮无效
		if(isAniming){
			return;
		}

		if(isExpanded){
			closeButtons();
		}else{
			expandButtons();
		}
	}

	private void closeButtons() {
		// 图片拍照位置按钮不可见
		isAniming = true;

		Animation clickAnim = AnimationUtils.loadAnimation(this, R.anim.click_button_anim);
		ibPlus.startAnimation(clickAnim);

		Animation closeAnim = AnimationUtils.loadAnimation(this, R.anim.close_button_anim);
		ibPicture.startAnimation(closeAnim);
		ibCamera.startAnimation(closeAnim);
		ibLocation.startAnimation(closeAnim);

		closeAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				isAniming = false;
				isExpanded = false;
				ibPicture.setVisibility(View.INVISIBLE);
				ibCamera.setVisibility(View.INVISIBLE);
				ibLocation.setVisibility(View.INVISIBLE);
			}
		});
	}

	private void expandButtons() {
		// 图片拍照位置按钮可见
		isAniming = true;

		Animation clickAnim = AnimationUtils.loadAnimation(this, R.anim.click_button_anim);
		ibPlus.startAnimation(clickAnim);

		Animation expandAnim = AnimationUtils.loadAnimation(this, R.anim.expand_button_anim);
		ibPicture.setVisibility(View.VISIBLE);
		ibCamera.setVisibility(View.VISIBLE);
		ibLocation.setVisibility(View.VISIBLE);

		ibPicture.startAnimation(expandAnim);
		ibCamera.startAnimation(expandAnim);
		ibLocation.startAnimation(expandAnim);

		expandAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				isAniming = false;
				isExpanded = true;
			}
		});

	}

	@OnClick(R.id.btn_postblog_addpicture)
	public void addPicture(View v){
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, 101);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if(arg1==RESULT_OK){
			if(arg0==101){
				Uri uri = arg2.getData();
				Cursor c = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
				c.moveToNext();
				String path = c.getString(0);
				c.close();
				showBlogImage(path);
			}
			//TODO 
		}
	}

	/**
	 * 把path位置的图片，放到显示blog配图的ImageView中显示
	 * @param path 要显示的图片在设备上的存储位置
	 */
	private void showBlogImage(String path) {
		for(int i=0;i<ivBlogImgs.size();i++){
			if(ivBlogImgs.get(i).getVisibility()!=View.VISIBLE){
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				ivBlogImgs.get(i).setVisibility(View.VISIBLE);
				ivBlogImgs.get(i).setImageBitmap(bitmap);
				ivBlogImgs.get(i).setTag(path);//将图片的路径“存储”到ImageView的tag属性中
				ivDelImgs.get(i).setVisibility(View.VISIBLE);//与ImageView对应的小红叉显示
				tvPicNumber.setText(i + 1 + " / 4");//修改已经显示的图片的数量
				return;
			}
		}
		toast("最多只能添加四副图片哦^.^");
	}

	@OnClick({R.id.iv_postblog_delimg0,R.id.iv_postblog_delimg1,R.id.iv_postblog_delimg2,R.id.iv_postblog_delimg3})
	public void delBlogPic(View v){
		switch (v.getId()) {
		case R.id.iv_postblog_delimg0:
			delImage(0);
			break;
		case R.id.iv_postblog_delimg1:
			delImage(1);
			break;
		case R.id.iv_postblog_delimg2:
			delImage(2);
			break;
		case R.id.iv_postblog_delimg3:
			delImage(3);
			break;
		}
	}
	
	/**
	 * 具体删除博客配图的方法
	 * @param index
	 */
	private void delImage(int index) {
		int count = 0;//判断下现在一共有多少图片
		
		for(int i=0;i<ivBlogImgs.size();i++){
			if(ivBlogImgs.get(i).getVisibility()==View.VISIBLE){
				count += 1;
			}
		}
		
		if(index == count-1){
			//如果点击的恰好是最后一幅配图的小红叉
			ivBlogImgs.get(index).setVisibility(View.INVISIBLE);
			ivDelImgs.get(index).setVisibility(View.INVISIBLE);
		}else{
			//点击的小红叉不是最后一幅配图的红叉
			for(int i=index;i<count;i++){
				if(i==count-1){//如果已经到了最后一幅图
					ivBlogImgs.get(i).setVisibility(View.INVISIBLE);
					ivDelImgs.get(i).setVisibility(View.INVISIBLE);
				}else{
					//没到最后一幅图的时候
					//当前的ImageView显示后一个imageView中显示图片和路径
					String path = (String) ivBlogImgs.get(i+1).getTag();
					ivBlogImgs.get(i).setTag(path);
					ivBlogImgs.get(i).setImageBitmap(BitmapFactory.decodeFile(path));
				}
			}
		}
		
		if(count == 1){
			tvPicNumber.setText("");
		}else{
			tvPicNumber.setText(count - 1 +" / 4");
		}
		
	}

}
