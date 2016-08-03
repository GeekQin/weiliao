package com.Dinggrn.weiliao.bean;

import cn.bmob.v3.BmobObject;

public class Favo extends BmobObject{
	
	String userId;//谁点的赞
	String blogId;//给哪个blog点赞
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBlogId() {
		return blogId;
	}
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	
	

}
