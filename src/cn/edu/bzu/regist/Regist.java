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
 * 用户注册界面
 * @author monster
 *@date:2015-07-23
 */
public class Regist extends BaseActivity implements OnClickListener{
	
	/**初始化控件信息**/
	private EditText edt_userName,edt_pass,edt_email,edt_nick,
							  edt_motto,edt_city,
							  edt_stepLength,edt_height,edt_weight,edt_userFeatherSport;
	
	private ImageView img_user_photo;
	
	private TextView tv_change_photo;
	
	private Button btn_regist;
	
	private String userName,pass,email,nick,
						  motto,city,
						  stepLength,height,weight,userFeatherSport;
	
	private static final int PICK_CODE = 1; //请求码
	
	private String mCurrentPhotoStr;  //当前图片的路径
	
	private Bitmap mPhotoImg;  
	
	private MyProgressDialog myProgressDialog; //进度框
	
	private Form form;//用于实现表单的验证
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		initView();
		form=new Form(); //创建Form表单
		
		/**
		 * 以下全部不为空
		 */
		NotEmptyValidator notEmptyValidator=new NotEmptyValidator(this);  //不为空的限制条件
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
		 * 邮箱验证
		 */
		
		EmailValidator emailValidator=new EmailValidator(this);
		Validate email=new Validate(edt_email);
		email.addValidator(emailValidator);
		
		
	/**
	 * 添加到form表单中
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
            intent.setType("image/*");  //设置数据类型
            startActivityForResult(intent, PICK_CODE);
			break;
		case R.id.btn_regist:
			Boolean isOK=form.validate(); //得到验证结果
			if(isOK){
				//验证成功
				myProgressDialog.initDialog(); //加载进度框
				
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
					Toast.makeText(this, "为了保证程序的正常运行,请将默认图片更换为您的头像", Toast.LENGTH_SHORT).show();
					myProgressDialog.colseDialog();
				}else {
					final BmobFile file=new BmobFile(new File(mCurrentPhotoStr));//将图片路径转为BmobFile
					
					/**图片上传**/
					file.uploadblock(this, new UploadFileListener() {
						
						@Override
						public void onSuccess() {
							//上传成功
							//Log.e("TAG", "上传文件成功");
							
							/**成功后上传数据**/
							upData(file);
						}
						
						@Override
						public void onFailure(int i, String error) {
							Log.e("****--->>>","文件上传失败"+error);
						}
					});
				}
				
				
			}else {
				//验证失败
				Toast.makeText(this, "亲，请正确填写用户信息，么么哒", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
	
	/**
	 * 上传数据
	 */
	protected void upData(BmobFile file) {
		//TODO 上传数据(含图片)
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
				//数据上传成功
				//Log.e("--->", "数据上传成功");
				myProgressDialog.colseDialog(); //终止进度条
				Toast.makeText(Regist.this, "欢迎加入Walker的大家庭",Toast.LENGTH_SHORT).show();
				finish(); //结束当前activity
			}
			
			@Override
			public void onFailure(int i, String s) {
				//数据上传失败
				//Log.e("--->", "数据上传失败"+s);
				Toast.makeText(Regist.this, "注册失败",Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(requestCode==PICK_CODE){
            if(intent!=null){
                Uri uri=intent.getData();
                Cursor cursor= getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst(); //将游标移到First

                int idx=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mCurrentPhotoStr=cursor.getString(idx); //得到图片的路径

                cursor.close(); //游标关闭

                residePhoto();  //压缩照片
                
                RoundImage roundImage=new RoundImage();
              //  img_user_photo.setImageBitmap(bitmap);
                img_user_photo.setImageBitmap(roundImage.toRoundBitmap(mPhotoImg));
                
            }
        }
		super.onActivityResult(requestCode, resultCode, intent);
	}
	

	
	/**
	 * 此方法用于压缩图片
	 */
	private void residePhoto() {
        BitmapFactory.Options options=new BitmapFactory.Options();
        //如果设置为true，解码器将返回null（没有位图），但出…领域仍将设置，允许调用者查询位图不用其像素分配内存。
        options.inJustDecodeBounds=true;

        BitmapFactory.decodeFile(mCurrentPhotoStr,options); //将解码文件转换成位图
        double radio=Math.max(options.outWidth * 1.0d/1024f,options.outHeight *1.0d/1024f);
        //如果设置的值大于1，要求解码器子样的原始图像，返回一个较小的图像保存记忆。 -->>来自官网帮助文档的解析
        options.inSampleSize= (int) Math.ceil(radio);
        options.inJustDecodeBounds=false;
        mPhotoImg=BitmapFactory.decodeFile(mCurrentPhotoStr,options); //压缩过后的位图
    }
	
	//初始化控件
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
