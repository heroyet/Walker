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
	
	private SlideMenu slideMenu; // ����SlideMenu����
	public BmobUser bmobUser;
	public Boolean isNet; //�����������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//UmengUpdateAgent.setUpdateOnlyWifi(false);   --->���κ�����¶����и���
		isNet=new TestNetWork().isNetworkAvailable(this);
		if(isNet){
			UmengUpdateAgent.update(this);  //WIFI�³����Զ�����
			requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
			setContentView(R.layout.activity_main);
			ButterKnife.inject(this);// ��װע����
			initView();
			firstEnter();
		}else {
			new TestNetWork().showNetDialog(this);
			requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
			setContentView(R.layout.activity_main);
			ButterKnife.inject(this);// ��װע����
			initView();
			firstEnter();
		}
	
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView() {
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menu = (ImageView) findViewById(R.id.title_bar_menu_btn);
		ImageView map = (ImageView) findViewById(R.id.title_bar_menu_map);
		menu.setOnClickListener(this);
		map.setOnClickListener(this);
		//Ĭ�Ͻ���
		Fragment sport=new Sports(); //����Fragment
		FragmentManager fm=getFragmentManager();//�õ�Fragment�Ĺ���
		fm.beginTransaction().replace(R.id.content_frame, sport).commit();//�ύ����
	}

	/** ��view */
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
	 * ��д�ؼ��ĵ���¼�
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

	// @OnClick(R.id.ly_column_health)//�΂� view�����@��������
   
	@OnClick({ R.id.ly_column_health, R.id.ly_column_history,
			R.id.ly_column_inform, R.id.ly_column_setting,
			R.id.ly_column_sport, R.id.ly_column_weather ,R.id.ly_column_login,R.id.img_user_photo})//���view�������ַ�������¼�

	//�ر�ע�⣺ �������� ���������ָ�ʽ��   public ����Ȩ�ޣ� void ����ֵ�� ���� ����Ϊ�գ� ������ View������Ϊ���������������Զ���
	public void onMenuClick(View menuItem) {
		switch (menuItem.getId()) {
		case R.id.ly_column_health:
			//TODO  ����ģ��
			isNet=new TestNetWork().isNetworkAvailable(this);
			if(isNet){
				Intent healthy=new Intent(MainActivity.this, HealthyActivity.class);
				startActivity(healthy);
				slideMenu.closeMenu();
			}else {
				Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.img_user_photo:
			//TODO  ����ͷ��
			showUserInform();
			break;
		case R.id.ly_column_history:
			//TODO  �г̼�¼ģ��
			isNet=new TestNetWork().isNetworkAvailable(this);
			if(isNet){
				if(bmobUser!=null){
					Fragment history=new tongjiFragment();
					FragmentManager fm_history=getFragmentManager();
					fm_history.beginTransaction().replace(R.id.content_frame, history).commit();
					slideMenu.closeMenu();
				}else {
					Toast.makeText(this, "�ף�����û�е�¼", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.ly_column_inform:
			//TODO  ������Ϣ��ʾ
				showUserInform();
//			Bundle bundle=new Bundle();
//			//bundle.putSerializable("Info", walkerUser);  //�Ѷ�ȡ���û���ϢЯ����Fragment��
//			bundle.putString("nick", walkerUser.getNick());
//			inform.setArguments(bundle);
		
			break;
		case R.id.ly_column_setting:
			//TODO  �������
			Fragment setting=new Setting(); //����Fragment
			FragmentManager fm_setting=getFragmentManager();//�õ�Fragment�Ĺ���
			fm_setting.beginTransaction().replace(R.id.content_frame, setting).commit();//�ύ����
			slideMenu.closeMenu();
			break;
		case R.id.ly_column_sport:
			//TODO  �˶�����
				Fragment sport=new Sports(); //����Fragment
				FragmentManager fm_sport=getFragmentManager();//�õ�Fragment�Ĺ���
				fm_sport.beginTransaction().replace(R.id.content_frame, sport).commit();//�ύ����
				slideMenu.closeMenu();
			break;
		case R.id.ly_column_weather:
			//TODO  ��ѯ����
			Fragment weather=new Search_Weather(); //����Fragment
			FragmentManager fm_weather=getFragmentManager();//�õ�Fragment�Ĺ���
			fm_weather.beginTransaction().replace(R.id.content_frame, weather).commit();//�ύ����
			slideMenu.closeMenu();
			break;
		case R.id.ly_column_login:
			//TODO  ��½ģ��
			isNet=new TestNetWork().isNetworkAvailable(this);
			if(isNet){
				showLogin();
			}else {
				Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
	/**
	 * ��ʾ�û���Ϣ
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
							String url=object.getUserPhoto().getFileUrl(MainActivity.this);  //�õ�ͼƬ��url ����Ҫͨ��ImageLoader����ͼƬ�Ĵ���
							//walkerUser.setUserPhoto(object.getUserPhoto());
							//Log.e("nick", walkerUser.getNick());  
							
							Fragment inform=new ShowInform();//����Fragment
							
							Bundle bundle=new Bundle();
							bundle.putSerializable("Info", walkerUser);
							bundle.putString("imageURL", url);
							inform.setArguments(bundle);
							
							FragmentManager fm_inform=getFragmentManager();//�õ�Fragment�Ĺ���
							fm_inform.beginTransaction().replace(R.id.content_frame, inform).commit();//�ύ����
							slideMenu.closeMenu();
					}

					@Override
					public void onFailure(int code, String error) {
						Toast.makeText(MainActivity.this, "��ѯʧ��"+error,Toast.LENGTH_SHORT).show();
					}
				});
		
		}else{
			//�û�δ��¼
			//showTipsDialog();
			Toast.makeText(this, "�ף�����û�е�¼����ȥ��¼��(��������)��", Toast.LENGTH_SHORT).show();
		}
		}else {
			Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
		}
		
	}

//	/**
//	 * ��ʾ�Զ���Ի���
//	 */
//	private void showTipsDialog() {
//			LayoutInflater inflater=LayoutInflater.from(this);
//			View view=inflater.inflate(R.layout.user_alert_dialog, null);
//			AlertDialog.Builder builder=new AlertDialog.Builder(this);
//			
//			builder.setView(view);
//			
//			AlertDialog dialog=builder.create(); //����dialog
//			dialog.show();
//	}
	@Override
	protected void onResume() {
		super.onResume();
		bmobUser=BmobUser.getCurrentUser(this);
	}
	
	/**
	 *ǿ��ˢ�½��� 
	 */
	public void refresh(){
		showUserInform(); //TODO ˢ��
	}
	
	/**
	 *TODO  ��½
	 */
	public void showLogin() {
		if(bmobUser==null){
			Fragment login=new Login();//����Fragment
			FragmentManager fm_login=getFragmentManager();//�õ�Fragment�Ĺ���
			fm_login.beginTransaction().replace(R.id.content_frame, login).commit();//�ύ����
			slideMenu.closeMenu();
		}else {
			//�û��Ѿ���¼
			Toast.makeText(this, "�ף����Ѿ���¼���벻Ҫ�ظ���¼��_��", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * �����һ�ε�½/��������
	 */
	public void firstEnter() {
		bmobUser=BmobUser.getCurrentUser(this);
		if(bmobUser!=null){
			
			/**
			 * ��ȡ��ǰ��¼���û������������˵�������Ϣ������ʾ
			 */
			String objectId=bmobUser.getObjectId();
			BmobQuery<WalkerUser>query=new BmobQuery<WalkerUser>();
			query.getObject(this, objectId, new GetListener<WalkerUser>() {
	
				@Override
				public void onSuccess(WalkerUser object) {
					user.setText(object.getNick());
					motto.setText(object.getMotto());
					String url=object.getUserPhoto().getFileUrl(MainActivity.this);  //�õ�ͼƬ��url ����Ҫͨ��ImageLoader����ͼƬ�Ĵ���
					
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
	 * �ָ���Ĭ������
	 */
	public void clearData(){
		userPhoto.setImageResource(R.drawable.test_photo);
		user.setText(R.string.left_menu_user_test_text);
		motto.setText(R.string.left_menu_motto_test_text);
	}
}
