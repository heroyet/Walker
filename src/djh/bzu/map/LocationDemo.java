package djh.bzu.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.edu.bzu.walker.R;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class LocationDemo extends Activity {

	private Button huifang;
	private TextView title_bar_menu_cancel;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位

	List<LatLng> pts = new ArrayList<LatLng>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_location);
		requestLocButton = (Button) findViewById(R.id.button1);
		title_bar_menu_cancel=(TextView) findViewById(R.id.title_bar_menu_cancel);
		title_bar_menu_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText("普通");
		
		huifang= (Button) super.findViewById(R.id.huifang);
		huifang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				addCustomElementsDemo();
				
			}
		});
		
		//bdLocction.getLongitude();
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		
	
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				
				pts.add(ll);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	

	/**
	 * 添加点、线、多边形、圆、文字
	 */
	public void addCustomElementsDemo() {
		
//		// 添加多边形
//		LatLng pt1 = new LatLng(39.93923, 116.357428);
//		LatLng pt2 = new LatLng(39.91923, 116.327428);
//		LatLng pt3 = new LatLng(39.89923, 116.347428);
//		LatLng pt4 = new LatLng(39.89923, 116.367428);
//		LatLng pt5 = new LatLng(39.91923, 116.387428);
//		List<LatLng> pts1 = new ArrayList<LatLng>();
//		pts.add(pt1);
//		pts.add(pt2);
//		pts.add(pt3);
//		pts.add(pt4);
//		pts.add(pt5);
//		OverlayOptions ooPolygon = new PolygonOptions().points(pts1)
//				.stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
//		mBaiduMap.addOverlay(ooPolygon);
		
		
		
		
//		pts.add(new LatLng(39.97923, 116.357428));
//		pts.add(new LatLng(39.94923, 116.397428));
		
		if(pts.size()<2){
			
		}else {
			OverlayOptions ooPolyline = new PolylineOptions().width(10)
					.color(0xAAFF0000).points(pts);
			mBaiduMap.addOverlay(ooPolyline);
		}
		
		
		
	}
	
	
	
}
