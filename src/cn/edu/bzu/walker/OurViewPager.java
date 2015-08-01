package cn.edu.bzu.walker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import cn.edu.bzu.adapter.ViewPagerAdapter;

/**
 * 引导页
 * @author lb
 * @date 2015-7-30
 *
 */
public class OurViewPager extends Activity implements OnPageChangeListener  {

	private ViewPager viewPager;
	private List<View> viewList;
	private View view1,view2,view3,view4;
	private Button btnFinish;
	
	private ViewPagerAdapter adapter;
	
	//TODO 新建
	private ImageView[] dots;
	private int[] ids = { R.id.iv0,R.id.iv1, R.id.iv2, R.id.iv3 };//4个导航点
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.our_view_pager);
		
		initialize();
		initDots();
		adapter = new ViewPagerAdapter(viewList);
		viewPager.setAdapter(adapter);
	
	}
	
	/**
	 * 初始化控件
	 */
	private void initialize() {
		viewPager =(ViewPager) findViewById(R.id.viewPager);
		
		view1 = View.inflate(this, R.layout.view1_of_pager, null);
		view2 = View.inflate(this, R.layout.view2_of_pager, null);
		view3 = View.inflate(this, R.layout.view3_of_pager, null);
		view4 = View.inflate(this, R.layout.view4_of_pager, null);
		btnFinish = (Button) view4.findViewById(R.id.btnFinish);
		
		btnFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(OurViewPager.this, MainActivity.class);
				startActivity(i);
			}
		});
		
		viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		
		//TODO
		viewPager.setOnPageChangeListener(this);
	}
	
	
	//TODO 
	/**
	 * 初始化导航点
	 */
	private void initDots() {
		dots = new ImageView[viewList.size()];
		for (int i = 0; i < viewList.size(); i++) {
			dots[i] = (ImageView) findViewById(ids[i]);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	/**
	 * 当Viewpager被选择,即正在显示
	 * @param   position 当前显示的是第几个
	 */
	@Override
	public void onPageSelected(int position) {
		for (int i = 0; i < ids.length; i++) {
			if (position == i) {
				dots[i].setImageResource(R.drawable.bg_point_selected);
			} else {
				dots[i].setImageResource(R.drawable.bg_point);
			}
		}
	}

	

}
