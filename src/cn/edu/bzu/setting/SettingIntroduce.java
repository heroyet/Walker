package cn.edu.bzu.setting;

import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

/**
 * 软件使用说明
 * @author monster
 * @date 2015-07-28
 */
public class SettingIntroduce extends Activity{
	private TextView title_bar_menu_cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_introduction);
		title_bar_menu_cancel=(TextView) findViewById(R.id.title_bar_menu_cancel);
		title_bar_menu_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
}
