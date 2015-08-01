package cn.edu.bzu.entity;

import cn.bmob.v3.BmobObject;

/**
 * 用户建议实体类
 * @author monster
 * @date 2015-07-28
 */
@SuppressWarnings("serial")
public class Advice extends BmobObject{
	private String userAdvice;

	public String getUserAdvice() {
		return userAdvice;
	}

	public void setUserAdvice(String userAdvice) {
		this.userAdvice = userAdvice;
	}
	
	
}
