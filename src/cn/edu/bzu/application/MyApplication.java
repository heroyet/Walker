package cn.edu.bzu.application;

import com.tandong.swichlayout.SwitchLayout;

import android.app.Application;

/**
 * Application的相关配置
 * 含有：切换动画的持续时间
 * @author monster
 * @date:2015-07-20
 */
public class MyApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		//动画持续的时间
		SwitchLayout.animDuration=1000;
	}
}
