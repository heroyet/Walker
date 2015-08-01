package cn.edu.bzu.regist;

import java.io.File;

import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.edu.bzu.customercontrol.MyProgressDialog;
import cn.edu.bzu.entity.WalkerUser;
import cn.edu.bzu.util.BaseActivity;
import cn.edu.bzu.util.RoundImage;
import cn.edu.bzu.walker.R;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �û�ע�����
 * @author monster
 *@date:2015-07-23
 */
public class Regist extends BaseActivity implements OnClickListener{
	
	/**��ʼ���ؼ���Ϣ**/
	private EditText edt_userName,edt_pass,edt_email,edt_nick,
							  edt_motto,edt_city,
							  edt_stepLength,edt_height,edt_weight,edt_userFeatherSport;
	
	private ImageView img_user_photo;
	
	private TextView tv_change_photo;
	
	private Button btn_regist;
	
	private String userName,pass,email,nick,
						  motto,city,
						  stepLength,height,weight,userFeatherSport;
	
	private static final int PICK_CODE = 1; //������
	
	private String mCurrentPhotoStr;  //��ǰͼƬ��·��
	
	private Bitmap mPhotoImg;  
	
	private MyProgressDialog myProgressDialog; //���ȿ�
	
	private Form form;//����ʵ�ֱ�����֤
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		initView();
		form=new Form(); //����Form��
		
		/**
		 * ����ȫ����Ϊ��
		 */
		NotEmptyValidator notEmptyValidator=new NotEmptyValidator(this);  //��Ϊ�յ���������
		Validate userName=new Validate(edt_userName);
		Validate pass=new Validate(edt_pass);
		Validate nick=new Validate(edt_nick);
		Validate motto=new Validate(edt_motto);
		Validate city=new Validate(edt_city);
		
		Validate stepLength=new Validate(edt_stepLength);
		Validate height=new Validate(edt_height);
		Validate weight=new Validate(edt_weight);
		Validate userFeatherSport=new Validate(edt_userFeatherSport);
		
		userName.addValidator(notEmptyValidator);
		pass.addValidator(notEmptyValidator);
		nick.addValidator(notEmptyValidator);
		motto.addValidator(notEmptyValidator);
		city.addValidator(notEmptyValidator);
		
		stepLength.addValidator(notEmptyValidator);
		height.addValidator(notEmptyValidator);
		weight.addValidator(notEmptyValidator);
		userFeatherSport.addValidator(notEmptyValidator);
		
		
		/**
		 * ������֤
		 */
		
		EmailValidator emailValidator=new EmailValidator(this);
		Validate email=new Validate(edt_email);
		email.addValidator(emailValidator);
		
		
	/**
	 * ��ӵ�form����
	 */
		
	form.addValidates(userName);
	form.addValidates(pass);
	form.addValidates(nick);
	form.addValidates(city);
	form.addValidates(motto);
	
	form.addValidates(stepLength);
	form.addValidates(height);
	form.addValidates(weight);
	form.addValidates(userFeatherSport);
	
	form.addValidates(email);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_change_photo:
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");  //������������
            startActivityForResult(intent, PICK_CODE);
			break;
		case R.id.btn_regist:
			Boolean isOK=form.validate(); //�õ���֤���
			if(isOK){
				//��֤�ɹ�
				myProgressDialog.initDialog(); //���ؽ��ȿ�
				
				userName=edt_userName.getText().toString();
				pass=edt_pass.getText().toString();
				email=edt_email.getText().toString();
				
				nick=edt_nick.getText().toString();
				motto=edt_motto.getText().toString();
				city=edt_city.getText().toString();
				
				stepLength=edt_stepLength.getText().toString();
				height=edt_height.getText().toString();
				weight=edt_weight.getText().toString();
				userFeatherSport=edt_userFeatherSport.getText().toString();
				
				
				if(mCurrentPhotoStr==null){
					Toast.makeText(this, "Ϊ�˱�֤�������������,�뽫Ĭ��ͼƬ����Ϊ����ͷ��", Toast.LENGTH_SHORT).show();
					myProgressDialog.colseDialog();
				}else {
					final BmobFile file=new BmobFile(new File(mCurrentPhotoStr));//��ͼƬ·��תΪBmobFile
					
					/**ͼƬ�ϴ�**/
					file.uploadblock(this, new UploadFileListener() {
						
						@Override
						public void onSuccess() {
							//�ϴ��ɹ�
							//Log.e("TAG", "�ϴ��ļ��ɹ�");
							
							/**�ɹ����ϴ�����**/
							upData(file);
						}
						
						@Override
						public void onFailure(int i, String error) {
							Log.e("****--->>>","�ļ��ϴ�ʧ��"+error);
						}
					});
				}
				
				
			}else {
				//��֤ʧ��
				Toast.makeText(this, "�ף�����ȷ��д�û���Ϣ��ôô��", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
	
	/**
	 * �ϴ�����
	 */
	protected void upData(BmobFile file) {
		//TODO �ϴ�����(��ͼƬ)
		final WalkerUser walkerUser=new WalkerUser();
		walkerUser.setUsername(userName);
		walkerUser.setPassword(pass);
		walkerUser.setEmail(email);
		
		walkerUser.setMotto(motto);
		walkerUser.setCity(city);
		walkerUser.setNick(nick);
		
		walkerUser.setUserHeight(height);
		walkerUser.setUserWeight(weight);
		walkerUser.setStepLength(stepLength);
		walkerUser.setUserFeatherSport(userFeatherSport);
		walkerUser.setUserPhoto(file);
		walkerUser.signUp(Regist.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				//�����ϴ��ɹ�
				//Log.e("--->", "�����ϴ��ɹ�");
				myProgressDialog.colseDialog(); //��ֹ������
				Toast.makeText(Regist.this, "��ӭ����Walker�Ĵ��ͥ",Toast.LENGTH_SHORT).show();
				finish(); //������ǰactivity
			}
			
			@Override
			public void onFailure(int i, String s) {
				//�����ϴ�ʧ��
				//Log.e("--->", "�����ϴ�ʧ��"+s);
				Toast.makeText(Regist.this, "ע��ʧ��",Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(requestCode==PICK_CODE){
            if(intent!=null){
                Uri uri=intent.getData();
                Cursor cursor= getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst(); //���α��Ƶ�First

                int idx=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mCurrentPhotoStr=cursor.getString(idx); //�õ�ͼƬ��·��

                cursor.close(); //�α�ر�

                residePhoto();  //ѹ����Ƭ
                
                RoundImage roundImage=new RoundImage();
              //  img_user_photo.setImageBitmap(bitmap);
                img_user_photo.setImageBitmap(roundImage.toRoundBitmap(mPhotoImg));
                
            }
        }
		super.onActivityResult(requestCode, resultCode, intent);
	}
	

	
	/**
	 * �˷�������ѹ��ͼƬ
	 */
	private void residePhoto() {
        BitmapFactory.Options options=new BitmapFactory.Options();
        //�������Ϊtrue��������������null��û��λͼ���������������Խ����ã���������߲�ѯλͼ���������ط����ڴ档
        options.inJustDecodeBounds=true;

        BitmapFactory.decodeFile(mCurrentPhotoStr,options); //�������ļ�ת����λͼ
        double radio=Math.max(options.outWidth * 1.0d/1024f,options.outHeight *1.0d/1024f);
        //������õ�ֵ����1��Ҫ�������������ԭʼͼ�񣬷���һ����С��ͼ�񱣴���䡣 -->>���Թ��������ĵ��Ľ���
        options.inSampleSize= (int) Math.ceil(radio);
        options.inJustDecodeBounds=false;
        mPhotoImg=BitmapFactory.decodeFile(mCurrentPhotoStr,options); //ѹ�������λͼ
    }
	
	//��ʼ���ؼ�
	private void initView() {
		edt_userName=(EditText) findViewById(R.id.edt_userName);
		edt_pass=(EditText) findViewById(R.id.edt_pass);
		edt_email=(EditText) findViewById(R.id.edt_email);
		edt_nick=(EditText) findViewById(R.id.edt_nick);
		
		edt_motto=(EditText) findViewById(R.id.edt_motto);
		edt_city=(EditText) findViewById(R.id.edt_city);
		
		edt_stepLength=(EditText) findViewById(R.id.edt_stepLength);
		edt_height=(EditText) findViewById(R.id.edt_height);
		edt_weight=(EditText) findViewById(R.id.edt_weight);
		edt_userFeatherSport=(EditText) findViewById(R.id.edt_userFeatherSport);
		
		img_user_photo=(ImageView) findViewById(R.id.img_user_photo);
		tv_change_photo=(TextView) findViewById(R.id.tv_change_photo);
		btn_regist=(Button) findViewById(R.id.btn_regist);
		
		myProgressDialog=new MyProgressDialog(Regist.this);
		tv_change_photo.setOnClickListener(this);
		btn_regist.setOnClickListener(this);
	}
	
	
}
