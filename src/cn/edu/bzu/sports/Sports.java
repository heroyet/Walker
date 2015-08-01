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
 * �˶�������(���ж�λ���У��õ���λ���е�����)
 * @author monster
 * @date 2015-08-01
 */
@SuppressWarnings("unused")
public class Sports extends  Fragment implements OnClickListener{
	
	private LocationClient locationClient=null;
	//private static final int UPDATE_TIME=5000;  //����Ƶ��
	
	private Activity context;
	
	private TextView tv_city , tv_temper , tv_week , tv_weather;
	
	private TextView sport_heart  ,  sport_walk  ,  sport_notepad;
	
	private String city;
	
	private MyProgressDialog myProgressDialog;
	/**
	 *�ۺ������������
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
		myProgressDialog.initDialog();  //���ؽ�����
		getCityByLocation();
		locationClient.start();
		locationClient.requestLocation();
	}

	private void queryCityWeather() {
		if(city!=null){
			try {
				cityEncode=URLEncoder.encode(city, "utf-8");//��url�еĺ��ֽ��б���
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}  
			 URLALL=new StringBuffer(url).append("&cityname=").append(cityEncode).append("&dtype=json&key=").append(key).toString();//api�ӿ�
			 new GetWeatherByCity().execute(URLALL,context, tv_temper , tv_week , tv_weather,myProgressDialog);
		}else{
			Toast.makeText(context, "��ѯ����ʧ��", Toast.LENGTH_SHORT).show();
		}
		
	}

	/**
	 * ���ݶ�λ�õ�������Ϣ
	 */
	public void getCityByLocation() {
		locationClient = new LocationClient(context);
		// ���ö�λ����
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ø߾��ȶ�λ��λģʽ
		option.setCoorType("bd09ll");// ���ðٶȾ�γ������ϵ��ʽ
		//option.setScanSpan(UPDATE_TIME);// ���÷���λ����ļ��ʱ��Ϊ1000ms
		option.setIsNeedAddress(true);// �������þ���λ�ã�ֻ�����綨λ�ſ���
		locationClient.setLocOption(option); // ��option�ĳ�ʼ����Ϣ��ӵ��ͻ���
		        
		 //ע��λ�õļ����¼�
        locationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				// ��λ�ص���Ϣ
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
