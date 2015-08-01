package cn.edu.bzu.weather;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.edu.bzu.adapter.WeatherAdapter;
import cn.edu.bzu.customercontrol.MyProgressDialog;
import cn.edu.bzu.entity.WeatherInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 使用异步任务的方式获取网络中的数据
 * @author monster
 * date：2015-06-26
 */
public class AsyncTaskGetWeather extends AsyncTask<Object, Void, List<WeatherInfo>>{
	
	private ListView mList;
	private Context context;
	private MyProgressDialog myProgressDialog;
	
	private LayoutAnimationController lac; //动画控制
	private ScaleAnimation sa;  //动画效果
	
	@Override
	protected List<WeatherInfo> doInBackground(Object... params) {
		mList=(ListView) params[1];
		context=(Context) params[2];
		myProgressDialog=(MyProgressDialog) params[3];
		return getWeatherInfo(params[0]);
	}
	@Override
	protected void onPostExecute(List<WeatherInfo> result) {
		super.onPostExecute(result);
		
		if(result!=null){
			
			myProgressDialog.colseDialog();
			WeatherAdapter adapter=new WeatherAdapter(context, result);
			//new ScaleAnimation(fromX, toX, fromY, toY)
			sa=new ScaleAnimation(0, 1, 0, 1);
			sa.setDuration(1000);
			lac=new LayoutAnimationController(sa, 0.6f);
		
			mList.setAdapter(adapter);
			mList.setLayoutAnimation(lac);
		}else{
			myProgressDialog.colseDialog();
			Toast.makeText(context, "亲，您所输入的城市不存在", Toast.LENGTH_SHORT).show();
		}
		
	}
	/**
	 * 得到网络中的json数据
	 * @param object
	 * @return
	 */
	private List<WeatherInfo> getWeatherInfo(Object params) {
		List<WeatherInfo> list=new ArrayList<WeatherInfo>();
		try {
			String jsonString=readStream(new URL((String) params).openStream());
			
			
			//---->>>判断当前的json数据是否空
			
			//Log.e("TAG", jsonString);
			 JsonParser parse=new JsonParser();//json解析器
			 JsonObject json=(JsonObject) parse.parse(jsonString);
			 int resultcode=json.get("resultcode").getAsInt();
			Log.e("RE------>>", ""+resultcode);
			 /**
			  * 通过返回码进行判断城市是否存在，如果存在，则继续解析，如果不存在，则不解析数据
			  */
			 if(resultcode==200){
				 JsonObject result=json.get("result").getAsJsonObject();
				 JsonArray array=result.get("future").getAsJsonArray();
				// Log.e("resultcode",""+ resultcode);
				 for(int i=0;i<array.size();i++){
					 WeatherInfo info = new WeatherInfo();
					 JsonObject subObject=array.get(i).getAsJsonObject();
					 info.setWeek(subObject.get("week").getAsString());
					 info.setDate(subObject.get("date").getAsString());
					 info.setTemperature(subObject.get("temperature").getAsString());
					 info.setWeather(subObject.get("weather").getAsString());
					 list.add(info);
				 }
			 }else {
				 list=null;
			}
			// Log.e("Array",""+ array);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
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




