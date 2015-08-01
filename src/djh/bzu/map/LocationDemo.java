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
 * ��demo����չʾ��ν�϶�λSDKʵ�ֶ�λ����ʹ��MyLocationOverlay���ƶ�λλ�� ͬʱչʾ���ʹ���Զ���ͼ����Ʋ����ʱ��������
 * 
 */
public class LocationDemo extends Activity {

	private Button huifang;
	private TextView title_bar_menu_cancel;
	// ��λ���
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI���
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ

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
		requestLocButton.setText("��ͨ");
		
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
					requestLocButton.setText("����");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("��ͨ");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("����");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		
	
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
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
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	

	/**
	 * ��ӵ㡢�ߡ�����Ρ�Բ������
	 */
	public void addCustomElementsDemo() {
		
//		// ��Ӷ����
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
