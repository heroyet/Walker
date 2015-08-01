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
 * ʹ���첽����ķ�ʽ��ȡ�����е�����
 * @author monster
 * date��2015-06-26
 */
public class AsyncTaskGetWeather extends AsyncTask<Object, Void, List<WeatherInfo>>{
	
	private ListView mList;
	private Context context;
	private MyProgressDialog myProgressDialog;
	
	private LayoutAnimationController lac; //��������
	private ScaleAnimation sa;  //����Ч��
	
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
			Toast.makeText(context, "�ף���������ĳ��в�����", Toast.LENGTH_SHORT).show();
		}
		
	}
	/**
	 * �õ������е�json����
	 * @param object
	 * @return
	 */
	private List<WeatherInfo> getWeatherInfo(Object params) {
		List<WeatherInfo> list=new ArrayList<WeatherInfo>();
		try {
			String jsonString=readStream(new URL((String) params).openStream());
			
			
			//---->>>�жϵ�ǰ��json�����Ƿ��
			
			//Log.e("TAG", jsonString);
			 JsonParser parse=new JsonParser();//json������
			 JsonObject json=(JsonObject) parse.parse(jsonString);
			 int resultcode=json.get("resultcode").getAsInt();
			Log.e("RE------>>", ""+resultcode);
			 /**
			  * ͨ������������жϳ����Ƿ���ڣ�������ڣ��������������������ڣ��򲻽�������
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
	 * ����aoi���ص�����
	 * @param is
	 * @return result
	 */
	private String readStream(InputStream is){
		 InputStreamReader isr;  
		 String result="";
		 try {
			 String line=""; //ÿ�е�����
			isr=new InputStreamReader(is,"utf-8");  //�ֽ���ת��Ϊ�ַ���
			BufferedReader br=new BufferedReader(isr);  //���ַ�����buffer����ʽ��ȡ����
			while((line=br.readLine())!=null){
				result+=line;  //ƴ�ӵ�result��
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		 return result;
	}
		                                                         
	}




