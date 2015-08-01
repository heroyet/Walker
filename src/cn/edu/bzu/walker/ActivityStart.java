package cn.edu.bzu.walker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;

/**
 * ������������
 * @author monster
 *@date 2015-07-27
 */
public class ActivityStart extends Activity{
	//TODO
	private SharedPreferences sharedPreferences=null;
	private static final String PREFS_NAME="IsFirst";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);
		//TODO
		sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		//�̲߳���
		new Thread(){
			public void run() {
				try {
					sleep(1500);
					if (sharedPreferences.getBoolean("isFirst",true)) {
						Editor editor = sharedPreferences.edit();
						editor.putBoolean("isFirst", false);
						editor.commit();
						//����ҳ��
						Intent i = new Intent(ActivityStart.this, OurViewPager.class);
						startActivity(i);
						finish();
					}else{
						//������
						Intent i = new Intent(ActivityStart.this, MainActivity.class);
						startActivity(i);
						finish();
					}
					finish();
				} catch (InterruptedException e) {
				}
			};
		}.start();
	}
	
}
