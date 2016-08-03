package com.Dinggrn.weiliao.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.bmob.im.BmobRecordManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.inteface.OnRecordChangeListener;
import cn.bmob.im.inteface.UploadListener;
import cn.bmob.im.util.BmobNetUtil;
import cn.bmob.v3.listener.PushListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.adapter.ChatAdapter;
import com.Dinggrn.weiliao.adapter.EmoAdapter;
import com.Dinggrn.weiliao.adapter.EmoPagerAdapter;
import com.Dinggrn.weiliao.bean.Emo;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.receiver.MyReceiver;
import com.Dinggrn.weiliao.util.ChatUtil;
import com.viewpagerindicator.CirclePageIndicator;

public class ChatActivity extends BaseActivity implements EventListener{
	@Bind(R.id.headerview)
	View headerView;
	@Bind(R.id.lv_chat_messages)
	ListView listView;
	@Bind(R.id.et_chat_input)
	EditText etInput;//文本聊天信息的输入框
	@Bind(R.id.btn_chat_add)
	Button btnAdd;//“加号”按钮
	@Bind(R.id.btn_chat_send)
	Button btnSend;//文本聊天信息的发送按钮（“飞机”按钮）

	List<BmobMsg> messages;//listView中显示的与某个用户之间的所有聊天信息
	ChatAdapter adapter;//适配器

	BmobChatUser targetUser;//与你聊天的用户
	String targetId;//targetUser的objectId；
	String targetUsername;//targetUser的用户名
	String myId;//当前登录用户的objectId


	//表情相关
	@Bind(R.id.ll_chat_morelayoutcontainer)
	LinearLayout morelayoutContainer;//对应示意图中“红色”的区域

	RelativeLayout emoContainer;//对应示意图中“蓝色”的区域
	ViewPager emoViewPager;//emoContainer中的viewPager
	EmoPagerAdapter emoPagerAdapter;//与emoViewPager配合管理示意图中“绿色”区域的pagerAdapter
	CirclePageIndicator pageIndicator;//viewPager的“指示器”

	//添加图片、相片相关、位置相关
	LinearLayout piclocContainer;
	String cameraPath;
	String filePath;

	//发送语音相关的属性
	@Bind(R.id.ll_textinput_container)
	LinearLayout textinputContainer;//文本输入相关的容器
	@Bind(R.id.ll_voicinput_container)
	LinearLayout voiceinputContainer;//语音输入相关的容器
	@Bind(R.id.ll_chat_voicerecordcontainer)
	LinearLayout voicerecordContainer;

	@Bind(R.id.iv_chat_recordvolume)
	ImageView ivRecordVolume;//录音时随着音量大小显示不同的图片
	@Bind(R.id.tv_chat_recordtips)
	TextView tvRecordTips;//录音时随着手指的位置显示不同的提示信息
	@Bind(R.id.btn_chat_voiceinput)
	Button btnVoiceInput;//“长按说话”按钮

	BmobRecordManager bmobRecordManager;//BmobIMSDK的录音辅助类
	Drawable[] recordDrawables;//存放表示不同音量的图片


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ButterKnife.bind(this);
		targetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
		targetId = targetUser.getObjectId();
		targetUsername = targetUser.getUsername();
		myId = bmobUserManager.getCurrentUserObjectId();
		initHeaderView();
		initListView();
		initView();
	}


	private void initView() {
		// 1)为etInput添加一个TextWathcer监听器
		// 该监听器会根据etInput中文本内容的变化调用相应的方法
		etInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				//当etInput内容发生改变后，etInput中的文本
				//会作为参数传入该方法中
				if(s.length()>0){
					//etInput中有内容
					btnAdd.setVisibility(View.INVISIBLE);
					btnSend.setVisibility(View.VISIBLE);
				}else{
					btnAdd.setVisibility(View.VISIBLE);
					btnSend.setVisibility(View.INVISIBLE);
				}


			}
		});

		//2为“表情”相关的属性赋值
		emoContainer = (RelativeLayout) getLayoutInflater().inflate(R.layout.emo_layout, morelayoutContainer,false);
		emoViewPager = (ViewPager) emoContainer.findViewById(R.id.vp_emolayout);
		pageIndicator = (CirclePageIndicator) emoContainer.findViewById(R.id.pagerindicator_emolayout);

		List<View> views = new ArrayList<View>();
		for(int i=0;i<2;i++){
			View view = getLayoutInflater().inflate(R.layout.emo_gridview_layout, emoContainer,false);

			GridView gridView = (GridView) view.findViewById(R.id.gv_emogridview);

			List<Emo> emos = new ArrayList<Emo>();
			if(i==0){
				//emos中存放ChatUtil.emos中前21个表情
				emos = ChatUtil.emos.subList(0,21);
			}else{
				//emos中存放ChatUtil.emos中后21个表情
				emos = ChatUtil.emos.subList(21, ChatUtil.emos.size());
			}
			final EmoAdapter adapter = new EmoAdapter(this, emos);
			gridView.setAdapter(adapter);
			//点击表情，将表情放到etInput中显示
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Emo emo = adapter.getItem(position);
					String emoId = emo.getId();//[emo]ue057
					etInput.append(ChatUtil.getSpannableString(emoId));
					//					String string = etInput.getText().toString();
					//					etInput.setText(ChatUtil.getSpannableString(string));
				}
			});
			views.add(view);
		}
		emoPagerAdapter = new EmoPagerAdapter(views );
		emoViewPager.setAdapter(emoPagerAdapter);
		//pageIndicator与viewPager进行绑定的语句
		//一定要写在viewPager与PagerAdapter绑定的语句的后面
		pageIndicator.setViewPager(emoViewPager,0);
		//指定pageIndicator用什么颜色来填充“圈”
		pageIndicator.setFillColor(Color.BLACK);

		//3 为图片和位置相关的属性赋值
		piclocContainer = (LinearLayout) getLayoutInflater().inflate(R.layout.add_layout, morelayoutContainer,false);
		View takePic = piclocContainer.findViewById(R.id.tv_picloc_takepic);
		takePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 101);
			}
		});
		View makePhoto = piclocContainer.findViewById(R.id.tv_picloc_makephoto);
		makePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".png");
				cameraPath = file.getAbsolutePath();
				Uri imgUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
				startActivityForResult(intent, 101);

			}
		});
		View location = piclocContainer.findViewById(R.id.tv_picloc_location);
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到地图界面进行定位
				Intent intent = new Intent(ChatActivity.this,LocationActivity.class);
				intent.putExtra("type", "button");
				startActivityForResult(intent, 102);


			}
		});

		//4. 与语音聊天相关的属性赋值
		recordDrawables = new Drawable[]{
				getResources().getDrawable(R.drawable.chat_icon_voice1),
				getResources().getDrawable(R.drawable.chat_icon_voice2),
				getResources().getDrawable(R.drawable.chat_icon_voice3),
				getResources().getDrawable(R.drawable.chat_icon_voice4),
				getResources().getDrawable(R.drawable.chat_icon_voice5),
				getResources().getDrawable(R.drawable.chat_icon_voice6)
		};

		bmobRecordManager = BmobRecordManager.getInstance(this);
		bmobRecordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

			@Override
			public void onVolumnChanged(int value) {
				//录音过程中，当音量发生变化时，该方法会被回调
				//方法参数中的value就对应当前bmobRecordManager所检测到的音量大小
				//value取值范围0-5，恰好对应recordDrawables数组中图像的下标值
				ivRecordVolume.setImageDrawable(recordDrawables[value]);
			}

			@Override
			public void onTimeChanged(int value, String localPath) {
				//录音过程中，随着时间的流逝，每一秒钟会被调用一次
				//value代表当前录音的长度，它的最大值不能超过60秒
				//如果一旦value值大于60了，要强行结束当前的语音录制，并把已经录制好的60秒内容发送出去
				//localPath代表当前录音文件被保存的路径
				if(value>BmobRecordManager.MAX_RECORD_TIME){

					btnVoiceInput.setPressed(false);
					btnVoiceInput.setClickable(false);
					voicerecordContainer.setVisibility(View.INVISIBLE);
					sendVoiceMessage(value,localPath);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							btnVoiceInput.setClickable(true);
						}
					}, 1500);
					toast("已达到最长录制时间");
				}
			}
		});

	}

	/**
	 * 发送语音类型的聊天信息
	 * @param value
	 * @param localPath
	 */
	protected void sendVoiceMessage(int length, String localPath) {
		// 发送语音消息的业务逻辑与发送图像类型的消是一模一样的
		bmobChatManager.sendVoiceMessage(targetUser, localPath, length, new UploadListener() {
			
			@Override
			public void onSuccess() {
				// 在语音文件上传成功，调用该onSucess方法之前
				// BmobIMSDK会去改写数据库中保存的原msg的信息
				// 修改数据表中msg的content字段的值：
				// 改为localPath&url&length
				// 比如：/storage/emulated/0/BmobChat/voice/fe23343a53c23173cab71b641adca0b9/24f1a6ccea/1445339948947.amr&http://s.bmob.cn/dJrfaF&3
				// 修改数据表中msg的status字段的值：
				// 改为BmobConfig.STATUS_SEND_SUCCESS
				bmobDB.resetUnread(targetId);
			}
			
			@Override
			public void onStart(final BmobMsg msg) {
				// msg对象是BmobIMSDK根据咱们提供的参数生成的
				// msg的正文(content)：localPath&length
				// 比如：/storage/emulated/0/BmobChat/voice/fe23343a53c23173cab71b641adca0b9/24f1a6ccea/1445339948947.amr&3
				msg.setStatus(BmobConfig.STATUS_SEND_START);
				bmobChatManager.saveReceiveMessage(false, msg);
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						adapter.add(msg);
					}
				});
			}
			
			@Override
			public void onFailure(int error, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});

	}


	private void initHeaderView() {
		setHeaderTitle(headerView, targetUsername, Position.CENTER);
		setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	private void initListView() {
		messages = new ArrayList<BmobMsg>();
		adapter = new ChatAdapter(this, messages);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MyReceiver.regist(this);
		refresh();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyReceiver.unregist(this);
	}

	private void refresh() {
		adapter.addAll(bmobDB.queryMessages(targetId, 0),true);
		//用代码让ListView滚到到最后一条聊天信息进行显示
		listView.setSelection(adapter.getCount()-1);
	}


	@OnClick(R.id.btn_chat_send)
	public void sendTextMessage(View v){
		String string = etInput.getText().toString();
		if(TextUtils.isEmpty(string)){
			return;
		}
		if(!BmobNetUtil.isNetworkAvailable(this)){
			toast("当前网络不可用");
			return;
		}
		//真正发送文本类型聊天内容
		//调用BmobMsg中的一个方法，根据string来创建BmobMsg对象
		final BmobMsg msg = BmobMsg.createTextSendMsg(this, targetId, string);
		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				//发送成功
				//1)清空etInput中已发送的内容
				etInput.setText("");
				//2)把刚才创建的bmobmsg对象放到ListView中显示
				adapter.add(msg);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				//发送失败
				toastAndLog("服务器繁忙，请稍后重试",arg0+":"+arg1);
			}
		});

	}


	@Override
	public void onMessage(BmobMsg message) {
		//当收到了别人发给我的聊天消息的时候，该方法会被调用
		//将收到的消息放到ListView中显示
		//只处理当前聊天用户发送的BmobMsg
		//其余人发送的内容不处理
		//当离开ChatActivity的时候，在MainActivity中去处理
		if(message.getBelongId().equals(targetId)){
			adapter.add(message);
			//把message的已读未读状态从未读--->已读
			bmobDB.resetUnread(targetId);
		}
	}


	@Override
	public void onReaded(String conversionId, String msgTime) {
		//该方法被调用，意味着发送方发送的消息对方已经接受到了
		//1) 将发送的消息（在之前sendImageMessage中监听器中onStart方法中的参数）的状态置为Success_Received
		//BmobMsg message = bmobDB.getMessage(conversionId, msgTime);
		//message.setStatus(BmobConfig.STATUS_SEND_RECEIVERED);
		bmobDB.updateTargetMsgStatus(BmobConfig.STATUS_SEND_RECEIVERED, conversionId, msgTime);
		//2) 通知ChatAdapter去改变发送消息的显示状况（比如说有progressBar的，将progressBar隐藏）
		//adapter.notifyDataSetChanged();//ListView中已有数据源的内容
		//此时执行refresh方法从chat表中取聊天信息
		//与图像消息开始发送时，数据发生了变化
		//content变化了，status变化了
		//原先content的内容：file:////storage/emulated/0/Samsung/Image/008.jpg
		//更改后的content的内容：file:////storage/emulated/0/Samsung/Image/008.jpg&http://s.bmob.cn/xdQTrw
		//原先status的内容：BmobConfig.STATUS_SEND_START
		//更改后的status的内容：BmobConfig.STATUS_SEND_RECEIVED
		log("发送的图像消息对方已收到");
		refresh();
	}


	@Override
	public void onNetChange(boolean isNetConnected) {
		toast("当前网络不可用");

	}


	@Override
	public void onAddUser(BmobInvitation message) {
		//与某用户聊天时遇到添加好友不处理
		//当从ChatActivity返回到MainActivity
		//交给MainActivity去处理

	}


	@Override
	public void onOffline() {
		// MainActivity处于可交互状态
		// 当MyReceiver接收到了一个“offline”类型的消息
		// 就会调用该方法
		//该方法弹出一个对话框给用户提示，让用户下线
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setTitle("通知");
		builder.setMessage("您的账号已经在另一台设备登录！");
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				bmobUserManager.logout();
				jump(SplashActivity.class,true);
			}
		});
		//显示对话框
		builder.create().show();
	}

	@OnClick(R.id.btn_chat_emotion)
	public void showEmos(View v){
		if(morelayoutContainer.getVisibility()==View.VISIBLE){
			morelayoutContainer.setVisibility(View.GONE);
		}else{
			morelayoutContainer.removeAllViews();
			morelayoutContainer.setVisibility(View.VISIBLE);
			morelayoutContainer.addView(emoContainer);
		}
	}

	@OnClick(R.id.btn_chat_add)
	public void showPicLocation(View v){
		if(morelayoutContainer.getVisibility()==View.VISIBLE){
			morelayoutContainer.setVisibility(View.GONE);
		}else{
			//如果现在是处于语音输入这套按钮的时候
			//把语音输入这套按钮变为文本输入这套按钮
			if(voiceinputContainer.getVisibility()==View.VISIBLE){
				voiceinputContainer.setVisibility(View.GONE);
				textinputContainer.setVisibility(View.VISIBLE);
			}
			morelayoutContainer.removeAllViews();
			morelayoutContainer.setVisibility(View.VISIBLE);
			morelayoutContainer.addView(piclocContainer);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == RESULT_OK){
			if(arg0 == 101){
				if(arg2!=null){//从图库选图
					Uri uri = arg2.getData();
					Cursor c = getContentResolver().query(uri, new String[]{Media.DATA}, null, null, null);
					c.moveToNext();
					filePath = c.getString(0);
					c.close();
				}else{//从相机拍照
					filePath = cameraPath;
				}
				//根据filePath构建图像类型的聊天信息发送
				//log("filePath:"+filePath);
				bmobChatManager.sendImageMessage(targetUser, filePath, new UploadListener() {

					@Override
					public void onSuccess() {
						//图像已经成功上传到了Bmob服务器
						//BmobChatManager在图像成功上传到Bmob服务器后，
						//在调用onSuccess方法之前，它偷偷做了一件事
						//把onStart方法参数的那个BmobMsg对象在数据库中的内容做了修改
						//主要修改两个内容：content字段值修改了，status字段值也会修改
						//原先content的内容：file:////storage/emulated/0/Samsung/Image/008.jpg
						//更改后的content的内容：file:////storage/emulated/0/Samsung/Image/008.jpg&http://s.bmob.cn/xdQTrw
						//把msg的“已读未读”状态(isReaded属性代表BmobMsg对象的已读未读状态),从默认的未读状态设置为已读
						//原先status的内容：BmobConfig.STATUS_SEND_START
						//更改后的status的内容：BmobConfig.STATUS_SEND_SUCCESS
						bmobDB.resetUnread(targetId);
					}

					@Override
					public void onStart(final BmobMsg msg) {
						//msg中包含了要发送的图像聊天信息中，图像的路径
						//设定该msg的status为发送开始
						msg.setStatus(BmobConfig.STATUS_SEND_START);
						//msg保存到chat表，recent表
						bmobChatManager.saveReceiveMessage(false, msg);
						//把这个msg放到listView中显示
						//代码要在ui线程执行，需要借助ui线程的Handler来完成
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							@Override
							public void run() {
								adapter.add(msg);
							}
						});


					}

					@Override
					public void onFailure(int error, String arg1) {
						// TODO Auto-generated method stub

					}
				});
			}

			if(arg0==102){
				//处理位置相关内容
				//从arg2中取值(从LocationActivity传递过来)
				String address = arg2.getStringExtra("address");
				String url = arg2.getStringExtra("url");//地图截图在服务器地址
				double lat = arg2.getDoubleExtra("lat", 0.0);
				double lng = arg2.getDoubleExtra("lng", 0.0);

				log("地址："+address+" ,截图地址："+url+" ,纬度/经度:("+lat+" , "+lng+")");

				//利用BmobChatManager发送一个位置相关的聊天信息
				//默认时，一个位置类型的聊天信息的content是：address&lat&lng
				//咱们构建的位置类型的聊天信息的content是: address&url&lat&lng
				final BmobMsg msg = BmobMsg.createLocationSendMsg(this, targetId, address+"&"+url, lat, lng);
				bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

					@Override
					public void onSuccess() {
						adapter.add(msg);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
			}
		}
	}

	@OnClick(R.id.btn_chat_voice)
	public void showVoiceInput(View v){
		textinputContainer.setVisibility(View.GONE);
		morelayoutContainer.setVisibility(View.GONE);
		voiceinputContainer.setVisibility(View.VISIBLE);
	}
	@OnClick(R.id.btn_chat_keyboard)
	public void showTextInput(View v){
		voiceinputContainer.setVisibility(View.GONE);
		textinputContainer.setVisibility(View.VISIBLE);
	}

	@OnTouch(R.id.btn_chat_voiceinput)
	public boolean record(View v,MotionEvent event){
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			//录音开始
			
			btnVoiceInput.setPressed(true);
			voicerecordContainer.setVisibility(View.VISIBLE);
			tvRecordTips.setText("手指上移，取消发送");
			
			bmobRecordManager.startRecording(targetId);
			break;
		case MotionEvent.ACTION_MOVE:
			//随着手指位置的不同，tvRecordTips要给不同的提示信息
			float y = event.getY();
			if(y<0){
				//手指在按钮的外面
				tvRecordTips.setText("松开手指，取消发送");
			}else{
				tvRecordTips.setText("手指上移，取消发送");
			}
			break;
		case MotionEvent.ACTION_UP:
			btnVoiceInput.setPressed(false);
			//手指抬起的位置
			if(event.getY()<0){
				bmobRecordManager.cancelRecording();
			}else{
				int recordLength = bmobRecordManager.stopRecording();
				if(recordLength<BmobRecordManager.MIN_RECORD_TIME){
					toast("录音时间过短");
				}else{
					sendVoiceMessage(recordLength, bmobRecordManager.getRecordFilePath(targetId));
				}
			}
			voicerecordContainer.setVisibility(View.INVISIBLE);
			break;
		}
		return true;
	}
}
