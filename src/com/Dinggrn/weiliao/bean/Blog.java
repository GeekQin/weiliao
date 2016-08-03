package com.Dinggrn.weiliao.bean;

import cn.bmob.v3.BmobObject;

public class Blog extends BmobObject {
	
	User user;//blog的作者
	String images;//这篇blog配图的地址
	String content;//这篇blog的文本内容
	int love;//这篇blog获得的“赞”的数量
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLove() {
		return love;
	}
	public void setLove(int love) {
		this.love = love;
	}
	
}
