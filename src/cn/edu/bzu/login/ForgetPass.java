package cn.edu.bzu.login;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 忘记密码的操作
 * @author monster
 *@date 2015-07-28
 */
public class ForgetPass extends Activity{
	
	private EditText edt_email;
	private Button btn_forget;
	private TextView cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgetpass);
		
		edt_email=(EditText) findViewById(R.id.edt_forget_email);
		btn_forget=(Button) findViewById(R.id.btn_forget);
		cancel=(TextView) findViewById(R.id.title_bar_menu_cancel);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					finish();
			}
		});
		btn_forget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String email=edt_email.getText().toString();
				
				if(TextUtils.isEmpty(email)){
					Toast.makeText(ForgetPass.this, "亲，您的邮箱不能为空", Toast.LENGTH_SHORT).show();
				}else{
					BmobUser.resetPasswordByEmail(ForgetPass.this, email, new ResetPasswordByEmailListener() {
						
						@Override
						public void onSuccess() {
							Toast.makeText(ForgetPass.this,"修改密码请求发送成功，请到"+ email+"修改",Toast.LENGTH_SHORT).show();
							finish();
						}
						
						@Override
						public void onFailure(int i, String e) {
							Toast.makeText(ForgetPass.this,"重置密码失败"+e,Toast.LENGTH_SHORT).show();
						}
					});
				}
				
			}
		});
	}
}
