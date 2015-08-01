package cn.edu.bzu.entity;

/**
 * 运动首页天气实体类
 * @author monster
 * @date 2015-07-29
 */
public class SportWeather {
	private String temper;  //温度
	private String week;  //星期
	private String weath;  //天气情况
	public String getTemper() {
		return temper;
	}
	public void setTemper(String temper) {
		this.temper = temper;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getWeath() {
		return weath;
	}
	public void setWeath(String weath) {
		this.weath = weath;
	}
	public SportWeather(String temper, String week, String weath) {
		super();
		this.temper = temper;
		this.week = week;
		this.weath = weath;
	}
	public SportWeather() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
