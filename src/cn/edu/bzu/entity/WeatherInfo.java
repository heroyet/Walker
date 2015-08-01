package cn.edu.bzu.entity;

/**
 * 未来天气信息的实体类
 * @author monster
 * @date：2015-06-26
 */
public class WeatherInfo {
	public String week;  
	public String date;
	public String temperature;
	public String weather;
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public WeatherInfo(String week, String date, String temperature,
			String weather) {
		super();
		this.week = week;
		this.date = date;
		this.temperature = temperature;
		this.weather = weather;
	}
	public WeatherInfo() {
		super();
	}
	
	
}
