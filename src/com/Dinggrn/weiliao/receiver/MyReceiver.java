package com.Dinggrn.weiliao.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.inteface.OnReceiveListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.im.util.BmobNetUtil;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.listener.FindListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.ui.MainActivity;
import com.Dinggrn.weiliao.ui.SplashActivity;
import com.Dinggrn.weiliao.util.SPUtil;


public class MyReceiver extends BroadcastReceiver{
	//"订阅者"集合
	private static List<EventListener> list = new ArrayList<EventListener>();

	Context context;
	//BmobIMSDK提供的发送通知的“工具类”
	BmobNotifyManager bmobNotifyManager;
	//获得偏好设置文件中的内容
	SPUtil sp;

	String objectId;
	BmobUserManager bmobUserManager;
	BmobChatManager bmobChatManager;
	BmobDB bmobDB;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		bmobUserManager = BmobUserManager.getInstance(context);
		bmobChatManager = BmobChatManager.getInstance(context);
		objectId = bmobUserManager.getCurrentUserObjectId();
		bmobNotifyManager = BmobNotifyManager.getInstance(context);
		bmobDB = BmobDB.create(context);
		sp = new SPUtil(objectId);
		String action = intent.getAction();
		//"cn.bmob.push.action.MESSAGE"
		if(PushConstants.ACTION_MESSAGE.equals(action)){
			String json = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);

			if(!BmobNetUtil.isNetworkAvailable(context)){
				if(list.size()>0){
					for (EventListener listener : list) {
						listener.onNetChange(false);
					}
				}
			}else{
				//解析json字符串，根据不同类型的消息作出处理
				parse(json);
			}
		}

	}

	private void parse(final String json) {
		try{

			JSONObject jsonObject = new JSONObject(json);
			//根据tag的值区分5种消息类型
			//String tag = jsonObject.getString("tag");
			String tag = BmobJsonUtil.getString(jsonObject, "tag");
			//offline类型信息tag = "offline"
			if("offline".equals(tag)){
				if(list.size()>0){
					//如果有订阅者
					for(EventListener listener:list){
						listener.onOffline();
					}
				}else{
					//没有订阅者，就发送 一个通知
					bmobNotifyManager.showNotify(
							sp.isAllowSound(), 
							sp.isAllowVibrate(), 
							R.drawable.ic_notification, 
							"账号已在其它设备登录", 
							"账号异常", 
							"您的账号已在其它设备登录", 
							SplashActivity.class);
					//让当前登录用户登出
					bmobUserManager.logout();
				}
			}
			//add类型信息 tag="add"
			if("add".equals(tag)){
				//确认下，这条消息是不是发给当前登录用户的
				//服务器发送过来的json字符串中，被添加人的id
				String tId = BmobJsonUtil.getString(jsonObject, "tId");
				if(objectId.equals(tId)){
					//提取Json字符串信息，构造一个BmobInvitation对象
					BmobInvitation message = bmobChatManager.saveReceiveInvite(json, tId);
					if(list.size()>0){
						for(EventListener listener:list){
							listener.onAddUser(message);
						}
					}else{
						if(sp.isAcceptNotify()){
							bmobNotifyManager.showNotify(
									sp.isAllowSound(), 
									sp.isAllowVibrate(), 
									R.drawable.ic_notification, 
									message.getFromname()+"请求添加您为好友", 
									"添加好友", 
									message.getFromname()+"请求添加您为好友",  
									MainActivity.class);
						}
					}
				}
			}
			//agree类型信息 tag="agree"
			if("agree".equals(tag)){
				//别人同意了我添加好友的请求
				//判断，1）好友同意信息是不是发给我的？2）对方是不是已经是我的好友了
				String tId = BmobJsonUtil.getString(jsonObject, "tId");
				String fId = BmobJsonUtil.getString(jsonObject, "fId");
				if(objectId.equals(tId) && !isFriend(fId)){
					//把别人添加为我的好友
					final String targetName = BmobJsonUtil.getString(jsonObject, "fu");
					//向服务器上和本地数据库中同时保存好友
					bmobUserManager.addContactAfterAgree(targetName, new FindListener<BmobChatUser>() {
						
						@Override
						public void onSuccess(List<BmobChatUser> arg0) {
							//1)发一个通知"XXX同意添加你为好友了"
							if(sp.isAcceptNotify()){
								bmobNotifyManager.showNotify(
										sp.isAllowSound(), 
										sp.isAllowVibrate(), 
										R.drawable.ic_notification, 
										targetName+"同意了您的好友申请", 
										"好友添加", 
										targetName+"同意了您的好友申请", 
										MainActivity.class);
							}
							//2)创建一个你与对方之间的聊天信息"我通过了你的好友验证请求，我们可以开始聊天了!"
							BmobMsg.createAndSaveRecentAfterAgree(context, json);
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							//TODO
						}
					});
				}
			}
			//readed类型信息tag = "readed"
			if("readed".equals(tag)){
				String tId = BmobJsonUtil.getString(jsonObject, "tId");
				if(objectId.equals(tId)){
					//把我之前发送的BmobMsg的状态更新一下
					//把消息的状态从"发送中"变为“对方已收到”
					String conversionId = BmobJsonUtil.getString(jsonObject,"mId");
					String msgTime = BmobJsonUtil.getString(jsonObject, "ft");
					bmobChatManager.updateMsgStatus(conversionId, msgTime);
					if(list.size()>0){
						for(EventListener listener:list){
							listener.onReaded(conversionId, msgTime);
						}
					}
				}
			}
			
			//聊天类型信息tag=""
			if(TextUtils.isEmpty(tag)){
				String tId = BmobJsonUtil.getString(jsonObject, "tId");
				if(objectId.equals(tId)){
					
					bmobChatManager.createReceiveMsg(json, new OnReceiveListener() {
						
						@Override
						public void onSuccess(BmobMsg msg) {
							if(list.size()>0){
								for(EventListener listener:list){
									listener.onMessage(msg);
								}
							}else{
								if(sp.isAcceptNotify()){
									//tickerText会根据聊天类型的不同而有不同的显示内容
									String tickerText = "";
									//聊天类型分为四种：1 文本 2图像 3位置 4语音
									int type = msg.getMsgType();
									switch (type) {
									case 1:
										//可能会修改
										tickerText = msg.getContent();
										break;

									case 2:
										tickerText = "[图片]";
										break;
									case 3:
										//TODO 可能会修改
										tickerText = "[位置]";
										break;
									case 4:
										tickerText = "[语音]";
										break;
									}
									bmobNotifyManager.showNotify(
											sp.isAllowSound(), 
											sp.isAllowVibrate(), 
											R.drawable.ic_notification, 
											tickerText,
											msg.getBelongUsername(),
											tickerText,
											MainActivity.class);
								}
							}
							
						}
						
						@Override
						public void onFailure(int code, String arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 判断fId所代表的所用是否已经是我的好友
	 * @param fId
	 * @return false 不是好友 
	 *         true 是好友
	 */
	private boolean isFriend(String fId) {

		List<BmobChatUser> contacts = bmobDB.getAllContactList();

		for(BmobChatUser b:contacts){
			if(b.getObjectId().equals(fId)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 注册成为订阅者
	 * @param listener
	 */
	public static void regist(EventListener listener){
		list.add(listener);
	}
	/**
	 * 注销订阅者
	 * @param listener
	 */
	public static void unregist(EventListener listener){
		list.remove(listener);
	}
}
