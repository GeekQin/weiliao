package com.Dinggrn.weiliao.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject{
	
	User user;//该评论的作者
	String username;//该评论作者的用户名
	String content;//该评论的正文
	String blogId;//该评论是针对哪一条blog发布的
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBlogId() {
		return blogId;
	}
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	
	
}
