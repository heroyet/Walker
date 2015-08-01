package cn.edu.bzu.healthy;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.edu.bzu.adapter.MyFragmentPagerAdapter;
import cn.edu.bzu.walker.R;

public class HealthyActivity extends FragmentActivity{
	 private ViewPager mPager;  
	    private ArrayList<Fragment> fragmentList;  
	    private TextView barText;  
	    private TextView view1, view2;  
	    private int currIndex;//当前页卡编号  
	    WebView webView;

		ImageView title_bar_menu_back;
	    
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉标题栏
	        setContentView(R.layout.healthy);  
	          
	        InitTextView();  
	        InitTextBar();  
	        InitViewPager();  
	        
	        webView = (WebView)findViewById(R.id.webView); 
	        title_bar_menu_back = (ImageView)findViewById(R.id.title_bar_menu_back);
	        title_bar_menu_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
	        
	    }  
	      
	      
	    /* 
	     * 初始化标签名 
	     */  
	    public void InitTextView(){  
	        view1 = (TextView)findViewById(R.id.tv_guid1);  
	        view2 = (TextView)findViewById(R.id.tv_guid2); 
	          
	        view1.setOnClickListener(new txListener(0));  
	        view2.setOnClickListener(new txListener(1));  
	    }  
	      
	      
	    public class txListener implements View.OnClickListener{  
	        private int index=0;  
	          
	        public txListener(int i) {  
	            index =i;  
	        }  
	        @Override  
	        public void onClick(View v) {  
	            mPager.setCurrentItem(index);  
	        }  
	    }  
	      
	      
	    /* 
	     * 初始化图片的位移像素 
	     */  
	    public void InitTextBar(){  
	    	barText = (TextView) super.findViewById(R.id.cursor);
	    	Display display = getWindow().getWindowManager().getDefaultDisplay();
	    	          // 得到显示屏宽度
	        DisplayMetrics metrics = new DisplayMetrics();
	    	display.getMetrics(metrics);
	    	          // 1/3屏幕宽度
	    	int  tabLineLength = metrics.widthPixels / 2;
	    	 LayoutParams lp = (LayoutParams) barText.getLayoutParams();
	    	 lp.width = tabLineLength;
	    	 barText.setLayoutParams(lp);
	    	        
	    }  
	      
	    /* 
	     * 初始化ViewPager 
	     */  
	    public void InitViewPager(){  
	        mPager = (ViewPager)findViewById(R.id.viewpager);  
	        fragmentList = new ArrayList<Fragment>();  
	        Fragment btFragment= new WebView_One();
	        Fragment web2Fragment = new WebView_Two();
	        fragmentList.add(btFragment); 
	        fragmentList.add(web2Fragment);
	        //给ViewPager设置适配器  
	        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
	        mPager.setCurrentItem(0);//设置当前显示标签页为第一页  
	        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器  
	    }  
	  
	      
	    public class MyOnPageChangeListener implements OnPageChangeListener{  
	          
	        @Override  
	        public void onPageScrolled(int arg0, float arg1, int arg2) {  
	        	  // 取得该控件的实例
                LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) barText
                        .getLayoutParams();
                
                if(currIndex == arg0){
                	 ll.leftMargin = (int) (currIndex * barText.getWidth() + arg1
                             * barText.getWidth());
                }else if(currIndex > arg0){
                	 ll.leftMargin = (int) (currIndex * barText.getWidth() - (1 - arg1)* barText.getWidth());
                }
                barText.setLayoutParams(ll);
	        }  
	          
	        @Override  
	        public void onPageScrollStateChanged(int arg0) {  
	              
	        }  
	          
	        @Override  
	        public void onPageSelected(int arg0) {  
	            currIndex = arg0;  
	        }  
	    }  
	  
	  
}
