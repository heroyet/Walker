package cn.edu.bzu.walker;

import com.umeng.update.UmengUpdateAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.entity.WalkerUser;
import cn.edu.bzu.healthy.HealthyActivity;
import cn.edu.bzu.inform.ShowInform;
import cn.edu.bzu.login.Login;
import cn.edu.bzu.setting.Setting;
import cn.edu.bzu.sports.Sports;
import cn.edu.bzu.util.ImageLoader;
import cn.edu.bzu.util.TestNetWork;
import cn.edu.bzu.weather.Search_Weather;
import djh.bzu.map.LocationDemo;
import djh.bzu.tongji.ui.tongjiFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * @author monster
 * @date 2015-07-20
 */
public class MainActivity extends Activity implements OnClickListener {
	
	private SlideMenu slideMenu; // 定义SlideMenu布局
	public BmobUser bmobUser;
	public Boolean isNet; //检测网络连接
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//UmengUpdateAgent.setUpdateOnlyWifi(false);   --->在任何情况下都进行更新
		isNet=new TestNetWork().isNetworkAvailable(this);
		if(isNet){
			UmengUpdateAgent.update(this);  //WIFI下程序自动更新
			requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
			setContentView(R.layout.activity_main);
			ButterKnife.inject(this);// 安装注解框架
			initView();
			firstEnter();
		}else {
			new TestNetWork().showNetDialog(this);
			requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
			setContentView(R.layout.activity_main);
			ButterKnife.inject(this);// 安装注解框架
			initView();
			firstEnter();
		}
	
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menu = (ImageView) findViewById(R.id.title_bar_menu_btn);
		ImageView map = (ImageView) findViewById(R.id.title_bar_menu_map);
		menu.setOnClickListener(this);
		map.setOnClickListener(this);
		//默认界面
		Fragment sport=new Sports(); //创建Fragment
		FragmentManager fm=getFragmentManager();//得到Fragment的管理
		fm.beginTransaction().replace(R.id.content_frame, sport).commit();//提交事务
	}

	/** 绑定view */
	@InjectView(R.id.ly_menuview)
	View menuView;

	@InjectView (R.id.img_user_photo)
	ImageView userPhoto;
	@InjectView (R.id.tv_user_text)
	TextView user;
	@InjectView (R.id.tv_user_motto)
	TextView motto;
	
	@InjectView (R.id.ly_column_login)
	View itemLogin;
	@InjectView(R.id.ly_column_sport)
	View itemSport;
	@InjectView(R.id.ly_column_inform)
	View itemInform;
	@InjectView(R.id.ly_column_history)
	View itemlHistory;
	@InjectView(R.id.ly_column_weather)
	View itemlWeather;
	@InjectView(R.id.ly_column_health)
	View itemlHealth;
	@InjectView(R.id.ly_column_setting)
	View itemlSetting;

	/**
	 * 重写控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_menu_btn:
			if (slideMenu.isMainScreenShowing()) {
				slideMenu.openMenu();
			} else {
				slideMenu.closeMenu();
			}
			break;
		case R.id.title_bar_menu_map:
			Intent intent = new Intent(MainActivity.this,LocationDemo.class);
			startActivity(intent);
		}

	}

	// @OnClick(R.id.ly_column_health)//單個 view，用這個方法；
   
	@OnClick({ R.id.ly_column_health, R.id.ly_column_history,
			R.id.ly_column_inform, R.id.ly_column_setting,
			R.id.ly_column_sport, R.id.ly_column_weather ,R.id.ly_column_login,R.id.img_user_photo})//多个view，用这种方法添加事件

	//特别注意： 函数声明 必须是这种格式；   public 访问权限， void 返回值， 参数 可以为空， 建议用 View对象最为参数；函数名，自定义
	public void onMenuClick(View menuItem) {
		switch (menuItem.getId()) {
		case R.id.ly_column_health:
			//TODO  健康模块
			isNet=new TestNetWork().isNetworkAvailable(this);
			if(isNet){
				Intent healthy=new Intent(MainActivity.this, HealthyActivity.class);
				startActivity(healthy);
				slideMenu.closeMenu();
			}else {
				Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.img_user_photo:
			//TODO  个人头像
			showUserInform();
			break;
		case R.id.ly_column_history:
			//TODO  行程记录模块
			isNet=new TestNetWork().isNetworkAvailable(this);
			if(isNet){
				if(bmobUser!=null){
					Fragment history=new tongjiFragment();
					FragmentManager fm_history=getFragmentManager();
					fm_history.beginTransaction().replace(R.id.content_frame, history).commit();
					slideMenu.closeMenu();
				}else {
					Toast.makeText(this, "亲，您还没有登录", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.ly_column_inform:
			//TODO  个人信息显示
				showUserInform();
//			Bundle bundle=new Bundle();
//			//bundle.putSerializable("Info", walkerUser);  //把读取的用户信息携带到Fragment中
//			bundle.putString("nick", walkerUser.getNick());
//			inform.setArguments(bundle);
		
			break;
		case R.id.ly_column_setting:
			//TODO  软件设置
			Fragment setting=new Setting(); //创建Fragment
			FragmentManager fm_setting=getFragmentManager();//得到Fragment的管理
			fm_setting.beginTransaction().replace(R.id.content_frame, setting).commit();//提交事务
			slideMenu.closeMenu();
			break;
		case R.id.ly_column_sport:
			//TODO  运动测试
				Fragment sport=new Sports(); //创建Fragment
				FragmentManager fm_sport=getFragmentManager();//得到Fragment的管理
				fm_sport.beginTransaction().replace(R.id.content_frame, sport).commit();//提交事务
				slideMenu.closeMenu();
			break;
		case R.id.ly_column_weather:
			//TODO  查询天气
			Fragment weather=new Search_Weather(); //创建Fragment
			FragmentManager fm_weather=getFragmentManager();//得到Fragment的管理
			fm_weather.beginTransaction().replace(R.id.content_frame, weather).commit();//提交事务
			slideMenu.closeMenu();
			break;
		case R.id.ly_column_login:
			//TODO  登陆模块
			isNet=new TestNetWork().isNetworkAvailable(this);
			if(isNet){
				showLogin();
			}else {
				Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
	/**
	 * 显示用户信息
	 */
	public void showUserInform() {
		Boolean isNet=new TestNetWork().isNetworkAvailable(this);
		if(isNet){
			if(bmobUser!=null){
				String objectId=bmobUser.getObjectId();
				//Log.e("ID",objectId);
				BmobQuery<WalkerUser> query = new BmobQuery<WalkerUser>();
				query.getObject(this, objectId, new GetListener<WalkerUser>() {

					@Override
					public void onSuccess(WalkerUser object) {
						   WalkerUser walkerUser=new WalkerUser();
							
							walkerUser.setNick(object.getNick());
							walkerUser.setCity(object.getCity());
							
							walkerUser.setStepLength(object.getStepLength());
							walkerUser.setUserHeight(object.getUserHeight());
							walkerUser.setUserWeight(object.getUserWeight());
							walkerUser.setUserFeatherSport(object.getUserFeatherSport());
							
							walkerUser.setUsername(object.getUsername());
							walkerUser.setMotto(object.getMotto());
							walkerUser.setEmail(object.getEmail());
							String url=object.getUserPhoto().getFileUrl(MainActivity.this);  //得到图片的url ，需要通过ImageLoader进行图片的处理
							//walkerUser.setUserPhoto(object.getUserPhoto());
							//Log.e("nick", walkerUser.getNick());  
							
							Fragment inform=new ShowInform();//创建Fragment
							
							Bundle bundle=new Bundle();
							bundle.putSerializable("Info", walkerUser);
							bundle.putString("imageURL", url);
							inform.setArguments(bundle);
							
							FragmentManager fm_inform=getFragmentManager();//得到Fragment的管理
							fm_inform.beginTransaction().replace(R.id.content_frame, inform).commit();//提交事务
							slideMenu.closeMenu();
					}

					@Override
					public void onFailure(int code, String error) {
						Toast.makeText(MainActivity.this, "查询失败"+error,Toast.LENGTH_SHORT).show();
					}
				});
		
		}else{
			//用户未登录
			//showTipsDialog();
			Toast.makeText(this, "亲，您还没有登录，快去登录吧(～￣▽￣)～", Toast.LENGTH_SHORT).show();
		}
		}else {
			Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
		}
		
	}

//	/**
//	 * 显示自定义对话框
//	 */
//	private void showTipsDialog() {
//			LayoutInflater inflater=LayoutInflater.from(this);
//			View view=inflater.inflate(R.layout.user_alert_dialog, null);
//			AlertDialog.Builder builder=new AlertDialog.Builder(this);
//			
//			builder.setView(view);
//			
//			AlertDialog dialog=builder.create(); //创建dialog
//			dialog.show();
//	}
	@Override
	protected void onResume() {
		super.onResume();
		bmobUser=BmobUser.getCurrentUser(this);
	}
	
	/**
	 *强制刷新界面 
	 */
	public void refresh(){
		showUserInform(); //TODO 刷新
	}
	
	/**
	 *TODO  登陆
	 */
	public void showLogin() {
		if(bmobUser==null){
			Fragment login=new Login();//创建Fragment
			FragmentManager fm_login=getFragmentManager();//得到Fragment的管理
			fm_login.beginTransaction().replace(R.id.content_frame, login).commit();//提交事务
			slideMenu.closeMenu();
		}else {
			//用户已经登录
			Toast.makeText(this, "亲，您已经登录，请不要重复登录∩_∩", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 程序第一次登陆/更改资料
	 */
	public void firstEnter() {
		bmobUser=BmobUser.getCurrentUser(this);
		if(bmobUser!=null){
			
			/**
			 * 获取当前登录的用户，并且在左侧菜单栏的信息区域显示
			 */
			String objectId=bmobUser.getObjectId();
			BmobQuery<WalkerUser>query=new BmobQuery<WalkerUser>();
			query.getObject(this, objectId, new GetListener<WalkerUser>() {
	
				@Override
				public void onSuccess(WalkerUser object) {
					user.setText(object.getNick());
					motto.setText(object.getMotto());
					String url=object.getUserPhoto().getFileUrl(MainActivity.this);  //得到图片的url ，需要通过ImageLoader进行图片的处理
					
					userPhoto.setTag(url);
					new ImageLoader().showImageByThread(userPhoto, url);
				}
	
				@Override
				public void onFailure(int i, String error) {
					
				}
			});
		}
	}
	
	/**
	 * 恢复至默认数据
	 */
	public void clearData(){
		userPhoto.setImageResource(R.drawable.test_photo);
		user.setText(R.string.left_menu_user_test_text);
		motto.setText(R.string.left_menu_motto_test_text);
	}
}
