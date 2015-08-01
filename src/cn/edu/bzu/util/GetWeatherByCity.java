package cn.edu.bzu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.edu.bzu.customercontrol.MyProgressDialog;
import cn.edu.bzu.entity.SportWeather;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 通过城市得到天气信息工具类
 * @author monster
 * @date 2015-07-29 
 * URLALL	,	context	,	 tv_temper , tv_week , tv_weather
 */
@SuppressWarnings("unused")
public class GetWeatherByCity extends AsyncTask<Object, Void, SportWeather>{

	private Context context;
	private TextView 	  tv_temper , tv_week , tv_weather;
	private MyProgressDialog myProgressDialog;
	@Override
	protected SportWeather doInBackground(Object... params) {
		context=(Context) params[1];
		tv_temper=(TextView) params[2];
		tv_week=(TextView) params[3];
		tv_weather=(TextView) params[4];
		myProgressDialog=(MyProgressDialog) params[5];
		return getWeatherInfo(params[0]);
	}

	@Override
	protected void onPostExecute(SportWeather result) {
		super.onPostExecute(result);
		if(result!=null){
			tv_week.setText(result.getWeek());
			tv_weather.setText(result.getWeath());
			tv_temper.setText(result.getTemper());
			myProgressDialog.colseDialog();
		}else{
			myProgressDialog.colseDialog();
			Toast.makeText(context, "查询天气失败", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 得到网络中的json数据
	 * @param object
	 * @return
	 */
	private SportWeather getWeatherInfo(Object params) {
		SportWeather sportWeather=new SportWeather();
		try {
			String jsonString=readStream(new URL((String) params).openStream());
			
			 JsonParser parse=new JsonParser();//json解析器
			 JsonObject json=(JsonObject) parse.parse(jsonString);
			 int resultcode=json.get("resultcode").getAsInt();
			// Log.e("resultcode", ""+resultcode);
			 if(resultcode==200){
				 
				 JsonObject result=json.get("result").getAsJsonObject();
				JsonObject today=result.get("today").getAsJsonObject();
				//Log.e("Today", ""+today);
				
				String temperature=today.get("temperature").getAsString();
				String week=today.get("week").getAsString();
				String weath=today.get("weather").getAsString();
				
				sportWeather.setTemper(temperature);
				sportWeather.setWeath(weath);
				sportWeather.setWeek(week);
			 }else{
				 sportWeather=null;
			 }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sportWeather;
	}

	/**
	 * 接收aoi返回的数据
	 * @param is
	 * @return result
	 */
	private String readStream(InputStream is){
		 InputStreamReader isr;  
		 String result="";
		 try {
			 String line=""; //每行的数据
			isr=new InputStreamReader(is,"utf-8");  //字节流转换为字符流
			BufferedReader br=new BufferedReader(isr);  //将字符流以buffer的形式读取出来
			while((line=br.readLine())!=null){
				result+=line;  //拼接到result中
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		 return result;
	}
}
