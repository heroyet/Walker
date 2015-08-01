package cn.edu.bzu.setting;

import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * ∞Ê±æΩÈ…‹
 * @author monster
 * @date 2015-07-28
 */
public class SettingVersion extends Activity{
	private TextView title_bar_menu_cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_version);
		title_bar_menu_cancel=(TextView) findViewById(R.id.title_bar_menu_cancel);
		title_bar_menu_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
}
