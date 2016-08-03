package com.Dinggrn.weiliao.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class User extends BmobChatUser{
	String pyname;//username的大写拼音格式
	char sortLetter;//pyname的首字母的大写
	BmobGeoPoint point;//用户最近一次登录时的坐标位置
	Boolean gender;//性别 true 男  false 女
	
	public String getPyname() {
		return pyname;
	}
	public void setPyname(String pyname) {
		this.pyname = pyname;
	}
	public char getSortLetter() {
		return sortLetter;
	}
	public void setSortLetter(char sortLetter) {
		this.sortLetter = sortLetter;
	}
	public BmobGeoPoint getPoint() {
		return point;
	}
	public void setPoint(BmobGeoPoint point) {
		this.point = point;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	
	
	
}
