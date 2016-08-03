package com.Dinggrn.weiliao.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager{
	
	private boolean isScroll = true;
	
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		//如果return true的时候，就代表着ViewPager要消费事件
		return isScroll && super.onTouchEvent(arg0);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		//如果return true的时候，就代表着ViewPager把事件截获了
		return isScroll && super.onInterceptTouchEvent(arg0);
	}
	
	public void setScrollEnable(boolean flag){
		this.isScroll = flag;
	}

}
