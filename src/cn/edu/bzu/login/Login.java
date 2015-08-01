package cn.edu.bzu.login;


import com.geetest.sdk.GeetestLib;
import com.geetest.sdk.GtDialog;
import com.geetest.sdk.GtDialog.GtListener;
import com.geetest.sdk.SdkInit;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.bzu.entity.WalkerUser;
import cn.edu.bzu.regist.Regist;
import cn.edu.bzu.walker.MainActivity;
import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��½�Ĳ�����
 * @author monster
 * @date 2015-07-23
 */
public class Login extends Fragment implements OnClickListener,SwichLayoutInterFace{
	
	private Activity context;
	private EditText edt_user;
	private EditText edt_pass;
	private Button btn_login;
	private Button btn_regist;
	private static final String appKey="ec474218c0f9d695c62f5084fb532e00";  //Bmob��appKey
	private TextView forget_pass;
	
	/**������֤**/
	private String captcha_id = "e740b07afb3aee425cd88afa824ccabe";
	private GeetestLib geetestLib = new GeetestLib();
	private SdkInit sdkInitData = new SdkInit();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.activity_login, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bmob.initialize(context, appKey); //��ʼ��Bmob
		init(view);
		
	}
	
	/**
	 * ��ʼ����ͼ
	 */
	private void init(View view) {
		edt_user=(EditText) view.findViewById(R.id.edt_user);
		edt_pass=(EditText) view.findViewById(R.id.edt_pass);
		btn_login=(Button) view.findViewById(R.id.btn_login);
		btn_regist=(Button) view.findViewById(R.id.btn_regist);
		forget_pass=(TextView) view.findViewById(R.id.forget_pass);
		
		/**���ü����¼�**/
		btn_login.setOnClickListener(this);
		btn_regist.setOnClickListener(this);
		forget_pass.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			geetestLib.setCaptchaId(captcha_id);
			new GtAppDlgTask().execute(); //����������֤���첽�߳�
			break;
		case R.id.btn_regist:
				//�˴���дע��� ��ز���
			Intent intent=new Intent(context, Regist.class);
			 startActivity(intent);
			break;
		case R.id.forget_pass:
			//��������ҳ��
			Intent i=new Intent(context,ForgetPass.class);
			startActivity(i);
			break;
		}
	}
	
	
	/**
	 * ������֤�첽��̨����
	 * @author monster
	 *@date 2015-07-24
	 */
	class GtAppDlgTask extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			return geetestLib.preProcess();
		}

		@Override
		protected void onPostExecute(Integer serverStatusCode) {

			if (serverStatusCode == 1) {

				sdkInitData.setCaptcha_id(captcha_id);
				sdkInitData.setChallenge_id(geetestLib.getChallengeId());
				sdkInitData.setContext(context);
				openGtTest(sdkInitData);

			} else {
			}
		}
	}

	public void openGtTest(SdkInit initData) {
		GtDialog dialog = GtDialog.newInstance(initData);


		dialog.setGtListener(new GtListener() {

			@Override
			public void gtResult(boolean success, String result) {
				if (success) {

					String user=edt_user.getText().toString();
					String pass=edt_pass.getText().toString();
					
					if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass)){
						Toast.makeText(context, "�˺Ż�����δ��д=_=", Toast.LENGTH_SHORT).show();
					}else{
					 	final WalkerUser walkerUser=new WalkerUser();
					 	walkerUser.setUsername(user);
					 	walkerUser.setPassword(pass);
						 walkerUser.login(context, new SaveListener() {
							
							@Override
							public void onSuccess() {
								//ע�⣺������֤
							
								if(walkerUser.getEmailVerified()){
									Toast.makeText(context,"��¼�ɹ�",Toast.LENGTH_SHORT).show();
									 
									MainActivity  mainActivity=(MainActivity) getActivity();
									mainActivity.firstEnter();
									mainActivity.showUserInform();
								}else{
									Toast.makeText(context, "�ף���ȥ�������������֤��", Toast.LENGTH_SHORT).show();
								}
							}
							
							@Override
							public void onFailure(int i, String error) {
								 Toast.makeText(context,"��¼ʧ��"+error,Toast.LENGTH_SHORT).show();
							}
						});
					}

				} else {
					// TODO ��֤ʧ��
					Toast.makeText(context, "��֤ʧ��", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void closeGt() {
			}
		});
		dialog.show();
	}

	@Override
	public void setEnterSwichLayout() {
		SwitchLayout.getFadingIn(context);
	
	}

	@Override
	public void setExitSwichLayout() {
		
	}


}
