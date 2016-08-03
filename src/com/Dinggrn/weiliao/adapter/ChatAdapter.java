package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.constant.Constant.Position;
import com.Dinggrn.weiliao.ui.LocationActivity;
import com.Dinggrn.weiliao.util.ChatUtil;
import com.Dinggrn.weiliao.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ChatAdapter extends MyBaseAdapter<BmobMsg>{

	private static final int SEND_TEXTMESSAGE = 0;
	private static final int RECEIVE_TEXTMESSAGE = 1;
	private static final int SEND_IMAGEMESSAGE = 2;
	private static final int RECEIVE_IMAGEMESSAGE = 3;
	private static final int SEND_LOCATIONMESSAGE = 4;
	private static final int RECEIVE_LOCATIONMESSAGE = 5;
	private static final int SEND_VOICEMESSAGE = 6;
	private static final int RECEIVE_VOICEMESSAGE = 7;
	String myid;//是当前登录用户的objectId

	public ChatAdapter(Context context, List<BmobMsg> datasource) {
		super(context, datasource);
		myid = BmobUserManager.getInstance(context).getCurrentUserObjectId();
	}

	@Override
	public View getItemView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		BmobMsg msg = getItem(position);
		int messageType = msg.getMsgType();
		switch (messageType) {
		case 1:
			if(convertView==null){
				int type = getItemViewType(position);
				if(type == SEND_TEXTMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_send_textmessage, parent,false);
				}
				if(type == RECEIVE_TEXTMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_receive_textmessage, parent,false);
				}
				vh = new ViewHolder();
				vh.tvChatTime = (TextView) convertView.findViewById(R.id.tv_chat_item_time);
				vh.tvContent = (TextView) convertView.findViewById(R.id.tv_chat_item_content);
				vh.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_chat_item_avatar);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}

			break;
		case 2:
			if(convertView==null){
				int type = getItemViewType(position);
				if(type == SEND_IMAGEMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_send_imagemessage, parent,false);
				}
				if(type == RECEIVE_IMAGEMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_receive_imagemessage, parent,false);
				}
				vh = new ViewHolder();
				vh.tvChatTime = (TextView) convertView.findViewById(R.id.tv_chat_item_time);
				vh.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_chat_item_avatar);
				vh.ivImgContent = (ImageView) convertView.findViewById(R.id.iv_chat_item_content);
				//当接收图像信息的时候，convertview使用左侧布局时，vh.pbSending的值是null
				vh.pbSending = (ProgressBar) convertView.findViewById(R.id.pb_chat_item_sending);

				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}

			break;
		case 3:
			
			if(convertView==null){
				int type = getItemViewType(position);
				if(type == SEND_LOCATIONMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_send_locationmessage, parent,false);
				}
				if(type == RECEIVE_LOCATIONMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_receive_locationmessage, parent,false);
				}
				vh = new ViewHolder();
				vh.tvChatTime = (TextView) convertView.findViewById(R.id.tv_chat_item_time);
				vh.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_chat_item_avatar);
				vh.ivImgContent = (ImageView) convertView.findViewById(R.id.iv_chat_item_content);
				vh.tvAddress = (TextView) convertView.findViewById(R.id.tv_chat_item_address);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			break;
		
		case 4:
			if(convertView==null){
				int type = getItemViewType(position);
				if(type == SEND_VOICEMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_send_voicemessage, parent,false);
				}
				if(type == RECEIVE_VOICEMESSAGE){
					convertView = inflater.inflate(R.layout.item_chat_receive_voicemessage, parent,false);
				}
				vh = new ViewHolder();
				vh.tvChatTime = (TextView) convertView.findViewById(R.id.tv_chat_item_time);
				vh.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_chat_item_avatar);
				vh.ivImgContent = (ImageView) convertView.findViewById(R.id.iv_chat_item_content);
				//当接收语音信息的时候，convertview使用左侧布局时，vh.pbSending的值是null
				vh.pbSending = (ProgressBar) convertView.findViewById(R.id.pb_chat_item_sending);
				vh.tvVoiceLength = (TextView) convertView.findViewById(R.id.tv_chat_item_voicelength);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			
			break;
			
		default:
			vh = null;
			break;
		}


		switch (messageType) {
		case 1:
			//聊天信息的时间
			vh.tvChatTime.setText(TimeUtil.getTime(Long.parseLong(msg.getMsgTime())));
			//头像
			if(TextUtils.isEmpty(msg.getBelongAvatar())){
				vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
			}else{
				ImageLoader.getInstance().displayImage(msg.getBelongAvatar(), vh.ivAvatar);
			}
			//聊天正文
			//vh.tvContent.setText(msg.getContent());
			//显示带表情的内容，需要利用ChatUtil的getSpannableString
			vh.tvContent.setText(ChatUtil.getSpannableString(msg.getContent()));
			break;

		case 2:
			//聊天信息的时间
			vh.tvChatTime.setText(TimeUtil.getTime(Long.parseLong(msg.getMsgTime())));
			//头像
			if(TextUtils.isEmpty(msg.getBelongAvatar())){
				vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
			}else{
				ImageLoader.getInstance().displayImage(msg.getBelongAvatar(), vh.ivAvatar);
			}
			//图像类型聊天正文msg.getContent;
			//对接收方来说：正文就是图像在服务器上的地址
			//            比如：http://s.bmob.cn/xdQTrw
			//对发送方来说：正文会根据发送阶段的不同而不一样
			//在发送开始阶段（status为BombConfig.SEND_START）时
			//正文是一个图像在发送者设备上的本地路径
			//             比如：file:////storage/emulated/0/Samsung/Image/008.jpg
			//在对方已经收到阶段(status为BmobConfig.SEND_RECEIVED)时
			//正文是图像在发送者设备上的本地路径+"&"+该图像在服务器上存储的地址
			//             比如：file:////storage/emulated/0/Samsung/Image/008.jpg&http://s.bmob.cn/xdQTrw

			String imgAddress = msg.getContent();
			if(imgAddress.indexOf("&")>0){
				//如果imgAddress中包含"&"
				//说明当前imgAddress的格式为：file:////storage/emulated/0/Samsung/Image/008.jpg&http://s.bmob.cn/xdQTrw
				String url = imgAddress.split("&")[1];//http://s.bmob.cn/xdQTrw
				ImageLoader.getInstance().displayImage(url, vh.ivImgContent);
			}else{
				if(msg.getBelongId().equals(myid)){
					//对于发送方来说，imgAddress的格式为file:////storage/emulated/0/Samsung/Image/008.jpg
					String url = imgAddress.split("///")[1];
					Bitmap bitmap = BitmapFactory.decodeFile(url);
					vh.ivImgContent.setImageBitmap(bitmap);
				}else{
					//对于接收方来说，imgAddress的格式为http://s.bmob.cn/xdQTrw
					ImageLoader.getInstance().displayImage(imgAddress, vh.ivImgContent);
				}
			}

			if(vh.pbSending!=null){
				//根据msg的status决定pbSending是否可见
				if(msg.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED){
					vh.pbSending.setVisibility(View.INVISIBLE);
				}else{
					vh.pbSending.setVisibility(View.VISIBLE);
				}
			}
			break;
			
		case 3:
			
			//聊天信息的时间
			vh.tvChatTime.setText(TimeUtil.getTime(Long.parseLong(msg.getMsgTime())));
			//头像
			if(TextUtils.isEmpty(msg.getBelongAvatar())){
				vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
			}else{
				ImageLoader.getInstance().displayImage(msg.getBelongAvatar(), vh.ivAvatar);
			}
			//位置类型聊天正文msg.getContent;
			//华威南路&http://s.bmob.cn/XXXXX.jpg&纬度&经度

			final String location = msg.getContent();
			
			vh.tvAddress.setText(location.split("&")[0]);
			ImageLoader.getInstance().displayImage(location.split("&")[1], vh.ivImgContent);
			
			//为vh.ivImgContent添加单机事件监听器，点击跳转到LocationActivity
			//在LocationActivity中把该聊天信息中所包含的位置信息(街道名称，纬度，经度)显示在地图上
			vh.ivImgContent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,LocationActivity.class);
					intent.putExtra("type", "item");
					intent.putExtra("address", location.split("&")[0]);
					intent.putExtra("lat", location.split("&")[2]);
					intent.putExtra("lng", location.split("&")[3]);
					context.startActivity(intent);
				}
			});
			
			break;
		case 4:
			//聊天信息的时间
			vh.tvChatTime.setText(TimeUtil.getTime(Long.parseLong(msg.getMsgTime())));
			//头像
			if(TextUtils.isEmpty(msg.getBelongAvatar())){
				vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
			}else{
				ImageLoader.getInstance().displayImage(msg.getBelongAvatar(), vh.ivAvatar);
			}
			//语音类型聊天正文msg.getContent;
			//对接收方来说：正文就是：语音文件在服务器上的地址&语音文件的长度
			//            比如：http://s.bmob.cn/xdQTrw&3
			//对发送方来说：正文会根据发送阶段的不同而不一样
			//在发送开始阶段（status为BombConfig.SEND_START）时
			//正文是一个语音文件在发送者设备上的本地路径&语音文件的长度
			//             比如：/storage/emulated/0/BmobChat/voice/fe23343a53c23173cab71b641adca0b9/24f1a6ccea/1445339948947.amr&3
			//在对方已经收到阶段(status为BmobConfig.SEND_RECEIVED)时
			//正文是语音文件在发送者设备上的本地路径&该语音在服务器上存储的地址&语音文件的长度
			//             比如：/storage/emulated/0/BmobChat/voice/fe23343a53c23173cab71b641adca0b9/24f1a6ccea/1445339948947.amr&http://s.bmob.cn/dJrfaF&3

			final String voice = msg.getContent();

			if(vh.pbSending!=null){
				//说明现在是发送一条语音消息(右侧布局)
				//根据msg的status决定pbSending是否可见
				if(msg.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED){
					vh.pbSending.setVisibility(View.INVISIBLE);
					vh.tvVoiceLength.setVisibility(View.VISIBLE);
					vh.tvVoiceLength.setText(voice.split("&")[2]+"'");
				}else{
					vh.pbSending.setVisibility(View.VISIBLE);
					vh.tvVoiceLength.setVisibility(View.INVISIBLE);
				}
			}else{
				//语音文件的接收方
				vh.tvVoiceLength.setText(voice.split("&")[1]+"'");
			}
			
			vh.ivImgContent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//1)播放语音文件
					//2)播放语音文件过程中，播放一个帧动画
					String url="";
					Position pos;
					if(getItemViewType(position)==SEND_VOICEMESSAGE){
						pos = Position.RIGHT;
						url = voice.split("&")[1];
					}else{
						pos = Position.LEFT;
						url = voice.split("&")[0];
					}
					playVoiceMessage(url, pos, vh.ivImgContent);
					
				}
			});
			
			
			break;
		default:
			break;
		}




		return convertView;
	}

	/**
	 * 播放语音文件同时播放帧动画
	 * @param url 要播放的语音文件的地址
	 * @param position LEFT、RIGHT
	 * @param iv 播放帧动画的ImageView
	 */
	protected void playVoiceMessage(String url,final Position pos,final ImageView iv) {
		try {
			MediaPlayer mp = new MediaPlayer();
			mp.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					//帧动画
					if(pos==Position.LEFT){
						Drawable drawable = context.getResources().getDrawable(R.drawable.voice_left_anim);
						iv.setImageDrawable(drawable);
						((AnimationDrawable)drawable).start();
					}else{
						Drawable drawable = context.getResources().getDrawable(R.drawable.voice_right_anim);
						iv.setImageDrawable(drawable);
						((AnimationDrawable)drawable).start();
					}
				}
			});
			
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.stop();
					Drawable drawable = iv.getDrawable();
					((AnimationDrawable)drawable).stop();
					if(pos == Position.LEFT){
						iv.setImageResource(R.drawable.voice_right3);
					}else{
						iv.setImageResource(R.drawable.voice_left3);
					}
					mp.release();
				}
			});
			mp.setDataSource(url);
			mp.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public int getViewTypeCount() {
		//在ListView中要显示几种不同的条目视图
		//该方法就返回几
		return 8;
	}

	@Override
	public int getItemViewType(int position) {
		//在显示数据源中下标为position的数据时
		//应该使用哪种条目视图
		BmobMsg msg = getItem(position);
		//objId代表当时发送msg这条聊天信息的用户的objectId
		String objId = msg.getBelongId();
		//messageType代表信息的类型
		//取值范围：1 文本， 2 图像， 3 位置， 4 语音
		int messageType = msg.getMsgType();
		switch (messageType) {
		case 1:
			if(objId.equals(myid)){
				//证明msg这条消息是我发出的
				return SEND_TEXTMESSAGE;
			}else{
				//证明msg这条消息不是我发出的(是收到的)
				return RECEIVE_TEXTMESSAGE;
			}
		case 2:
			if(objId.equals(myid)){
				return SEND_IMAGEMESSAGE;
			}else{
				return RECEIVE_IMAGEMESSAGE;
			}
		case 3: 
			//位置信息相关
			if(objId.equals(myid)){
				return SEND_LOCATIONMESSAGE;
			}else{
				return RECEIVE_LOCATIONMESSAGE;
			}
		case 4: 
			//语音信息相关
			if(objId.equals(myid)){
				return SEND_VOICEMESSAGE;
			}else{
				return RECEIVE_VOICEMESSAGE;
			}
		default:
			//return -1;
			throw new RuntimeException("错误的消息类型");
		}
	}

	public class ViewHolder{
		TextView tvChatTime;
		ImageView ivAvatar;
		TextView tvContent;

		ImageView ivImgContent;//显示聊天信息中的图像
		ProgressBar pbSending;//发送图像聊天信息时的提示

		TextView tvAddress;//显示位置类型聊天信息的地址信息
		
		TextView tvVoiceLength;//显示语音类型聊天信息的语音长度
		
	}

}
