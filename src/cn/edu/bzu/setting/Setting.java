package cn.edu.bzu.setting;


import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.UpdateStatus;
import cn.edu.bzu.util.TestNetWork;
import cn.edu.bzu.walker.MainActivity;
import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 软件设置界面
 * @author monster
 *  @date:2015-07-22
 */
public class Setting extends Fragment implements OnClickListener{
    public Activity context;
    
   private LinearLayout setting_help	,	setting_team	,	setting_version	,	setting_update	,	setting_advice;
  
    private Button loginout;
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.activity_setting, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init(view);
		
	}

		private void init(View view) {
			setting_help=(LinearLayout) view.findViewById(R.id.soft_help);
			setting_team=(LinearLayout) view.findViewById(R.id.soft_team);
			setting_version=(LinearLayout) view.findViewById(R.id.soft_version);
			setting_update=(LinearLayout) view.findViewById(R.id.soft_update);
			setting_advice=(LinearLayout) view.findViewById(R.id.soft_advice);
			loginout=(Button) view.findViewById(R.id.loginout);
			
			setting_help.setOnClickListener(this);
			setting_team.setOnClickListener(this);
			setting_version.setOnClickListener(this);
			setting_update.setOnClickListener(this);
			setting_advice.setOnClickListener(this);
			
			loginout.setOnClickListener(this);
		}

		/**
		 * 得到上下文
		 */
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			context=activity;
		}

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {	
			switch (v.getId()) {
			case R.id.soft_help:
				Intent help=new Intent(context,SettingIntroduce.class);
				startActivity(help);
				break;
			case R.id.soft_team:
				Intent team=new Intent(context,SettingTeam.class);
				startActivity(team);
				break;
			case R.id.soft_version:
				Intent version=new Intent(context,SettingVersion.class);
				startActivity(version);
				break;
			case R.id.soft_update:
				Boolean isNet=new TestNetWork().isNetworkAvailable(context);
				if(isNet){
					 UmengUpdateAgent.forceUpdate(context);  //手动检查更新
					 UmengUpdateAgent.setUpdateAutoPopup(false);
					 UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					
					@Override
					public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
						switch (updateStatus) {
						case UpdateStatus.Yes: //存在更新
							 UmengUpdateAgent.showUpdateDialog(context, updateInfo);
							break;
						case UpdateStatus.No: // 没有更新
				            Toast.makeText(context, "已是最新版", Toast.LENGTH_SHORT).show();
				            break;
				        case UpdateStatus.TimeOut:
				            Toast.makeText(context, "超时", Toast.LENGTH_SHORT).show();
				            break;
						}
					}
				});
				 UmengUpdateAgent.update(context);
				}else{
					Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.soft_advice:
				Intent advice=new Intent(context,SettingAdvice.class);
				startActivity(advice);
				break;
			case R.id.loginout:
				BmobUser bmobUser=BmobUser.getCurrentUser(context);
				bmobUser.logOut(context);
				
				MainActivity mainActivity=(MainActivity) getActivity();
				mainActivity.firstEnter();
				mainActivity.showLogin();
				mainActivity.clearData();
				break;
			}
		}
}
