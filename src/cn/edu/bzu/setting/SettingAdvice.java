package cn.edu.bzu.setting;

import cn.bmob.v3.listener.SaveListener;
import cn.edu.bzu.entity.Advice;
import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 意见反馈界面
 * @author monster
 * @date 2015-07-28
 */
public class SettingAdvice extends Activity implements OnClickListener{
	private Button btn_submit;
	private EditText edt_advice;
	private TextView cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_advice);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  //关闭软键盘
		
		btn_submit=(Button) findViewById(R.id.submit_advice);
		edt_advice=(EditText) findViewById(R.id.advice);
		cancel=(TextView) findViewById(R.id.title_bar_menu_cancel);
		
		btn_submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_menu_cancel:
			finish();
			break;
		case R.id.submit_advice:
			String userAdvice=edt_advice.getText().toString();
			if(TextUtils.isEmpty(userAdvice)){
				Toast.makeText(this, "请填写您的宝贵意见", Toast.LENGTH_SHORT).show();
			}else{
				Advice advice=new Advice();
				advice.setUserAdvice(userAdvice);
				
				advice.save(this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(SettingAdvice.this, "我们已收到您的宝贵意见，谢谢您的合作", Toast.LENGTH_SHORT).show();
						finish();
					}
					
					@Override
					public void onFailure(int i, String e) {
						Toast.makeText(SettingAdvice.this, "提交建议失败: "+e, Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
		}
		
	}
}
