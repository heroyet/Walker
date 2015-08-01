package cn.edu.bzu.weather;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.edu.bzu.customercontrol.MyProgressDialog;
import cn.edu.bzu.util.TestNetWork;
import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 *  异步加载天气
 * @author monster
 * @date 2015-07-22
 */
public class Search_Weather extends Fragment{
	private Activity context;
	private EditText edt_city;
	private Button btn_query;
	private ListView list_weather;
	private String city;
	private String cityEncode;
	private String url="http://v.juhe.cn/weather/index?format=2";
	private String key="1f91c740b64a163423ff0369903b2b6f";
	private String URLALL;
	private MyProgressDialog myProgressDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.activity_weather, container, false);
			return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Boolean isNet=new TestNetWork().isNetworkAvailable(context);
		if(isNet){
			edt_city=(EditText) view.findViewById(R.id.edt_city);
			btn_query=(Button) view.findViewById(R.id.btn_search);
			list_weather=(ListView) view.findViewById(R.id.list_weather);
			myProgressDialog=new MyProgressDialog(context);
		
			
			
			btn_query.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					city=edt_city.getText().toString(); //得到城市
					if(TextUtils.isEmpty(city)){
						//isEmpty：空
						Toast.makeText(context, "亲，您的城市还没有输入呀^_^#", Toast.LENGTH_SHORT).show();
					}else {
						myProgressDialog.initDialog();
					   try {
						cityEncode=URLEncoder.encode(city, "utf-8");  //对url中的汉字进行编码
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					   URLALL=new StringBuffer(url).append("&cityname=").append(cityEncode).append("&dtype=json&key=").append(key).toString();//api接口
					  // Log.e("TAG", URLALL);
					   new AsyncTaskGetWeather().execute(URLALL,list_weather,context,myProgressDialog);
				}
				}
			});
		}else{
			Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 得到上下文
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	}
}
