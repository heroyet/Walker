package djh.bzu.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.edu.bzu.walker.R;

import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * ��demo����չʾ����ڵ�ͼ����GraphicsOverlay��ӵ㡢�ߡ�����Ρ�Բ ͬʱչʾ����ڵ�ͼ����TextOverlay�������
 * 
 */
public class GeometryDemo extends Activity {

	// ��ͼ���
	MapView mMapView;
	BaiduMap mBaiduMap;
	// UI���
	Button resetBtn;
	Button clearBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geometry);
		// ��ʼ����ͼ
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// UI��ʼ��
		clearBtn = (Button) findViewById(R.id.button1);
		resetBtn = (Button) findViewById(R.id.button2);

		OnClickListener clearListener = new OnClickListener() {
			public void onClick(View v) {
				clearClick();
			}
		};
		OnClickListener restListener = new OnClickListener() {
			public void onClick(View v) {
				resetClick();
			}
		};

		clearBtn.setOnClickListener(clearListener);
		resetBtn.setOnClickListener(restListener);

		// �������ʱ��ӻ���ͼ��
		addCustomElementsDemo();
	}

	/**
	 * ��ӵ㡢�ߡ�����Ρ�Բ������
	 */
	public void addCustomElementsDemo() {
		// �������
		LatLng p1 = new LatLng(39.97923, 116.357428);
		LatLng p2 = new LatLng(39.94923, 116.397428);
		LatLng p3 = new LatLng(39.97923, 116.437428);
		List<LatLng> points = new ArrayList<LatLng>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(0xAAFF0000).points(points);
		mBaiduMap.addOverlay(ooPolyline);
		// ��ӻ���
		OverlayOptions ooArc = new ArcOptions().color(0xAA00FF00).width(4)
				.points(p1, p2, p3);
		mBaiduMap.addOverlay(ooArc);
		// ���Բ
		LatLng llCircle = new LatLng(39.90923, 116.447428);
		OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
				.center(llCircle).stroke(new Stroke(5, 0xAA000000))
				.radius(1400);
		mBaiduMap.addOverlay(ooCircle);

		LatLng llDot = new LatLng(39.98923, 116.397428);
		OverlayOptions ooDot = new DotOptions().center(llDot).radius(6)
				.color(0xFF0000FF);
		mBaiduMap.addOverlay(ooDot);
		// ��Ӷ����
		LatLng pt1 = new LatLng(39.93923, 116.357428);
		LatLng pt2 = new LatLng(39.91923, 116.327428);
		LatLng pt3 = new LatLng(39.89923, 116.347428);
		LatLng pt4 = new LatLng(39.89923, 116.367428);
		LatLng pt5 = new LatLng(39.91923, 116.387428);
		List<LatLng> pts = new ArrayList<LatLng>();
		pts.add(pt1);
		pts.add(pt2);
		pts.add(pt3);
		pts.add(pt4);
		pts.add(pt5);
		OverlayOptions ooPolygon = new PolygonOptions().points(pts)
				.stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
		mBaiduMap.addOverlay(ooPolygon);
		// �������
		LatLng llText = new LatLng(39.86923, 116.397428);
		OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
				.fontSize(24).fontColor(0xFFFF00FF).text("�ٶȵ�ͼSDK").rotate(-30)
				.position(llText);
		mBaiduMap.addOverlay(ooText);
	}

	public void resetClick() {
		// ��ӻ���Ԫ��
		addCustomElementsDemo();
	}

	public void clearClick() {
		// �������ͼ��
		mMapView.getMap().clear();
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
		mMapView.onDestroy();
		super.onDestroy();
	}

}
