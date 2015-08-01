package cn.edu.bzu.inform;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.edu.bzu.entity.WalkerUser;
import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  更新用户的资料
 *  @author monster
 *  @date 2015-07-26
 */
public class UpdateInform extends Activity {
	
	private TextView title_bar_menu_cancel,title_bar_menu_complete;
	
	private EditText edt_update_nick  ,  edt_update_city   ,   edt_update_motto,
								edt_update_stepLength  ,  edt_update_height  ,  edt_update_weight  ,  edt_update_feather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  //关闭软键盘
		
		initView();
		//通过Bundle对象得到值
		//final WalkerUser walkerUser=(WalkerUser) getIntent().getExtras().getSerializable("userInfo");
		//Log.e("TAG", walkerUser.getNick());
		final String objectId=BmobUser.getCurrentUser(UpdateInform.this).getObjectId();
			//Log.e("OBbjectId", objectId);
		BmobQuery<WalkerUser> query = new BmobQuery<WalkerUser>();
		query.getObject(UpdateInform.this, objectId, new GetListener<WalkerUser>() {
			
			@Override
			public void onFailure(int i, String error) {
				Toast.makeText(UpdateInform.this, "查询数据失败"+error, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(WalkerUser walkerUser) {
				edt_update_nick.setText(walkerUser.getNick());
				edt_update_city.setText(walkerUser.getCity());
				edt_update_motto.setText(walkerUser.getMotto());
				
				edt_update_stepLength.setText(walkerUser.getStepLength());
				edt_update_height.setText(walkerUser.getUserHeight());
				edt_update_weight.setText(walkerUser.getUserWeight());
				edt_update_feather.setText(walkerUser.getUserFeatherSport());
				
			}
		});
		
		/**取消当前的Activity**/
		title_bar_menu_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					finish();
			}
		});
		
		/**完成更新的操作**/
		title_bar_menu_complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WalkerUser user=new WalkerUser();
				user.setNick(edt_update_nick.getText().toString());
				user.setCity(edt_update_city.getText().toString());
				user.setMotto(edt_update_motto.getText().toString());
				
				user.setStepLength(edt_update_stepLength.getText().toString());
				user.setUserHeight(edt_update_height.getText().toString());
				user.setUserWeight(edt_update_weight.getText().toString());
				user.setUserFeatherSport(edt_update_feather.getText().toString());
				
				user.update(UpdateInform.this,objectId, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(UpdateInform.this, "更新成功", Toast.LENGTH_SHORT).show();
						finish();
						
					}
					
					@Override
					public void onFailure(int i, String error) {
						Toast.makeText(UpdateInform.this, "更新失败"+error,Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	
	/**
	 * 初始化控件
	 */
	private void initView() {
		title_bar_menu_cancel=(TextView) findViewById(R.id.title_bar_menu_cancel);
		title_bar_menu_complete=(TextView) findViewById(R.id.title_bar_menu_complete);
		
		edt_update_nick=(EditText) findViewById(R.id.edt_update_nick);
		edt_update_city=(EditText) findViewById(R.id.edt_update_city);
		edt_update_motto=(EditText) findViewById(R.id.edt_update_motto);
		
		edt_update_stepLength=(EditText) findViewById(R.id.edt_update_stepLength);
		edt_update_height=(EditText) findViewById(R.id.edt_update_height);
		edt_update_weight=(EditText) findViewById(R.id.edt_update_weight);
		edt_update_feather=(EditText) findViewById(R.id.edt_update_feather);
	}


}
