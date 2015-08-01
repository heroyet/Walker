package cn.edu.bzu.sports;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import cn.edu.bzu.customercontrol.MyProgressDialog;
import cn.edu.bzu.note.NoteMain;
import cn.edu.bzu.util.GetWeatherByCity;
import cn.edu.bzu.walker.R;
import djh.bzu.sport.pedometer.Pedometer;
import djh.bzu.xinlv.xinlvActivity;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 运动主界面(含有定位城市，得到定位城市的天气)
 * @author monster
 * @date 2015-08-01
 */
@SuppressWarnings("unused")
public class Sports extends  Fragment implements OnClickListener{
	
	private LocationClient locationClient=null;
	//private static final int UPDATE_TIME=5000;  //更新频率
	
	private Activity context;
	
	private TextView tv_city , tv_temper , tv_week , tv_weather;
	
	private TextView sport_heart  ,  sport_walk  ,  sport_notepad;
	
	private String city;
	
	private MyProgressDialog myProgressDialog;
	/**
	 *聚合天气相关数据
	 */
	private String cityEncode;
	private String url="http://v.juhe.cn/weather/index?format=2";
	private String key="1f91c740b64a163423ff0369903b2b6f";
	private String URLALL;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.sport_main, container,false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init(view);
		myProgressDialog.initDialog();  //加载进度条
		getCityByLocation();
		locationClient.start();
		locationClient.requestLocation();
	}

	private void queryCityWeather() {
		if(city!=null){
			try {
				cityEncode=URLEncoder.encode(city, "utf-8");//对url中的汉字进行编码
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}  
			 URLALL=new StringBuffer(url).append("&cityname=").append(cityEncode).append("&dtype=json&key=").append(key).toString();//api接口
			 new GetWeatherByCity().execute(URLALL,context, tv_temper , tv_week , tv_weather,myProgressDialog);
		}else{
			Toast.makeText(context, "查询城市失败", Toast.LENGTH_SHORT).show();
		}
		
	}

	/**
	 * 根据定位得到城市信息
	 */
	public void getCityByLocation() {
		locationClient = new LocationClient(context);
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置高精度定位定位模式
		option.setCoorType("bd09ll");// 设置百度经纬度坐标系格式
		//option.setScanSpan(UPDATE_TIME);// 设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(true);// 反编译获得具体位置，只有网络定位才可以
		locationClient.setLocOption(option); // 讲option的初始化信息添加到客户端
		        
		 //注册位置的监听事件
        locationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				// 定位回调信息
				if(location==null){
					return;
				}      
				 city=location.getCity();
				 //Log.e("city",city);
				 tv_city.setText(city);
				 queryCityWeather();
			}
		});
	}

	private void init(View view) {
		tv_city=(TextView) view.findViewById(R.id.tv_city);
		tv_temper=(TextView) view.findViewById(R.id.tv_temper);
		tv_week=(TextView) view.findViewById(R.id.tv_week);
		tv_weather=(TextView) view.findViewById(R.id.tv_weather);
		
		sport_heart=(TextView) view.findViewById(R.id.sport_heart);
		sport_walk=(TextView) view.findViewById(R.id.sport_walk);
		sport_notepad=(TextView) view.findViewById(R.id.sport_notepad);
		myProgressDialog=new MyProgressDialog(context);
		
		sport_heart.setOnClickListener(this);
		sport_walk.setOnClickListener(this);
		sport_notepad.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(locationClient!=null&&locationClient.isStarted())
		{
			locationClient.stop();
			locationClient=null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sport_heart:
			Intent xinlv=new Intent(context, xinlvActivity.class);
			startActivity(xinlv);
			break;
		case R.id.sport_walk:
			Intent walk=new Intent(context, Pedometer.class);
			startActivity(walk);
			break;
		case R.id.sport_notepad:
			Intent notepad=new Intent(context, NoteMain.class);
			startActivity(notepad);
			break;
		}
	}
}
