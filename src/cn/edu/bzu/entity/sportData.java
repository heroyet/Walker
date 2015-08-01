package cn.edu.bzu.entity;

import java.util.Date;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class sportData extends BmobObject{
    private String userName;//用户名
    private String step;//步数
    private String distance;//路程
    private String time;
    public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	private Date createTime;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}   

    
    
}
