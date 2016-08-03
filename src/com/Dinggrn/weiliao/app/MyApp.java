package com.Dinggrn.weiliao.app;

import java.io.File;

import android.app.Application;
import android.media.MediaPlayer;
import cn.bmob.im.BmobChat;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.constant.Constant;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApp extends Application{

	//声明一些你认为非常重要，且全局可用的一些属性（变量）
	//该属性用来记录用户在使用软件时的位置
	public static BmobGeoPoint lastPoint;
	//当有新的消息/新的好友添加申请被服务器推送过来时，播放提示音
	public static MediaPlayer mediaPlayer;
	//图片加载用类库，需要进行一次初始化才能使用
	public static ImageLoader imageLoader;
	//提供一个全局上下文对象（使用的时候请慎重！并不是所有场合都可以用！能不用就别瞎用！）
	public static MyApp context;
	

	@Override
	public void onCreate() {
		super.onCreate();
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
		//注意该方法要再setContentView方法之前实现  
		SDKInitializer.initialize(getApplicationContext());  
		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		// Bmob.initialize(this, Constant.BmobKey);
		// 可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试，正式发布应注释此句。
		BmobChat.DEBUG_MODE = true;
		//BmobIM SDK初始化--只需要这一段代码即可完成初始化
		BmobChat.getInstance(this).init(Constant.BmobKey);

		//属性的初始化
		context = this;
		mediaPlayer = MediaPlayer.create(context, R.raw.notify);
		initImageLoader();
	}

	/**
	 * 初始化ImageLoader
	 * 初始化参数网上copy的
	 * http://blog.csdn.net/liu1164316159/article/details/38728259
	 * 
	 */
	private void initImageLoader() {
		File cacheDir =StorageUtils.getOwnCacheDirectory(this, "imageloader/Cache");  
		ImageLoaderConfiguration  configuration= new ImageLoaderConfiguration   
				.Builder(this)   
		.memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽   
		.threadPoolSize(3)//线程池内加载的数量   
		.threadPriority(Thread.NORM_PRIORITY -2)   
		.denyCacheImageMultipleSizesInMemory()   
		.memoryCache(new UsingFreqLimitedMemoryCache(2* 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现   
		.memoryCacheSize(5 * 1024 * 1024)     
		.discCacheSize(50 * 1024 * 1024)     
		.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密   
		.tasksProcessingOrder(QueueProcessingType.LIFO)   
		.discCacheFileCount(200) //缓存的文件数量   
		.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径   
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())   
		.imageDownloader(new BaseImageDownloader(this,5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间   
		.writeDebugLogs() // Remove for releaseapp   
		.build();//开始构建 
		ImageLoader.getInstance().init(configuration);
	}
}
