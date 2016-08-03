package com.Dinggrn.weiliao.bean;
/**
 * 实体类，用来描述表情
 * 每一个表情就是一个Emo对象
 * @author pjy
 *
 */
public class Emo {
	//R.drawable.ue057 数字 0x7fXXXXXX 不太妥当
	//因为做正则表达式判断的时候，不太容易区分
	//哪些是用户聊天中的数字，哪些代表了表情id
	//如果用ue057做id，在某些场合下，也会不好判定
	//哪些是聊天中的信息，哪些是表情id
	String id;//[emo]ue057

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Emo(String id) {
		super();
		this.id = id;
	}

	public Emo() {
		super();
	}
	
}
