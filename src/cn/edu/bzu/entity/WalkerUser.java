package cn.edu.bzu.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 用户信息实体类
 * @author monster
 * @date:2015-07-23
 *  *说明：本实体类继承的BmobUser，由于这张表作为Bmob的系统表，所以内部的字段
 * 已经定义好，所以不需要重复定义，如果重复定义，将导致程序出错
 * 附录：已含字段：
 *   		objectId,username,password,email,authData........
 *   所以：用户可以在添加实体类的时候只需要添加除了这几个字段就ok啦
 */
@SuppressWarnings("serial")
public class WalkerUser extends BmobUser implements Serializable {
	
	/**基本信息**/
	private String motto; //座右铭
	private BmobFile userPhoto;//用户头像
	private String city;   //用户的城市
	private String nick; //用户的昵称
	
	/**必需存在的变量**/
	private String stepLength;//步长
	private String userHeight; //身高
	private String userWeight; //体重
	private String userFeatherSport; //用户预期运动量(步数)
	
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
	}
	public BmobFile getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(BmobFile userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStepLength() {
		return stepLength;
	}
	public void setStepLength(String stepLength) {
		this.stepLength = stepLength;
	}
	public String getUserHeight() {
		return userHeight;
	}
	public void setUserHeight(String userHeight) {
		this.userHeight = userHeight;
	}
	public String getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(String userWeight) {
		this.userWeight = userWeight;
	}
	public String getUserFeatherSport() {
		return userFeatherSport;
	}
	public void setUserFeatherSport(String userFeatherSport) {
		this.userFeatherSport = userFeatherSport;
	}
	
}
