package cn.edu.bzu.application;

import com.tandong.swichlayout.SwitchLayout;

import android.app.Application;

/**
 * Application���������
 * ���У��л������ĳ���ʱ��
 * @author monster
 * @date:2015-07-20
 */
public class MyApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		//����������ʱ��
		SwitchLayout.animDuration=1000;
	}
}
