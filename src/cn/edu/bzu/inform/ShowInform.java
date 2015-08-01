package cn.edu.bzu.inform;



import cn.edu.bzu.entity.WalkerUser;
import cn.edu.bzu.util.ImageLoader;
import cn.edu.bzu.walker.MainActivity;
import cn.edu.bzu.walker.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 显示个人信息
 * @author monster
 * @date 2015-07-25
 */
public class ShowInform extends Fragment{
	
	private Activity context;
	   
	private Button edt_updateInform;
	
	private ImageView img_user_getPhoto;
	
	private TextView tv_user_getNickName   ,  tv_user_getCity;
	
	private TextView tv_user_getStepLength  ,  tv_user_getheight   ,  tv_user_getWeight  ,  tv_user_getFeather;
	
	private TextView tv_user_getUserName  ,  tv_user_getMotto  ,  tv_user_getEmail ;
	
 
	public static final int REQUEST_CODE = 10; //TODO 请求码  
	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.activity_inform, container, false);
			return view;
		}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init(view);
		final WalkerUser walkerUser=(WalkerUser) getArguments().getSerializable("Info");
		String imageURL=getArguments().getString("imageURL");
		//Log.e("url", imageURL);
		tv_user_getNickName.setText(walkerUser.getNick());
		tv_user_getCity.setText(walkerUser.getCity());
		
		
		tv_user_getStepLength.setText(walkerUser.getStepLength());
		tv_user_getheight.setText(walkerUser.getUserHeight());
		tv_user_getWeight.setText(walkerUser.getUserWeight());
		tv_user_getFeather.setText(walkerUser.getUserFeatherSport());
		
		tv_user_getUserName.setText(walkerUser.getUsername());
		tv_user_getMotto.setText(walkerUser.getMotto());
		tv_user_getEmail.setText(walkerUser.getEmail());

		img_user_getPhoto.setTag(imageURL);
		new ImageLoader().showImageByThread(img_user_getPhoto, imageURL);
		
		
		edt_updateInform.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,UpdateInform.class);
//				Bundle bundle=new Bundle();
//				bundle.putSerializable("userInfo", walkerUser);
//				intent.putExtras(bundle);
				//startActivity(intent);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
			MainActivity mainActivity=(MainActivity) getActivity();
			mainActivity.refresh();
			mainActivity.firstEnter();
		}
	}
	@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			context=activity;
		}

		private void init(View view) {
			edt_updateInform=(Button) view.findViewById(R.id.edt_updateInform);
			
			img_user_getPhoto=(ImageView) view.findViewById(R.id.img_user_getPhoto);
			tv_user_getNickName=(TextView) view.findViewById(R.id.tv_user_getNickName);
			tv_user_getCity=(TextView) view.findViewById(R.id.tv_user_getCity);
			
			tv_user_getStepLength=(TextView) view.findViewById(R.id.tv_user_getStepLength);
			tv_user_getheight=(TextView) view.findViewById(R.id.tv_user_getheight);
			tv_user_getWeight=(TextView) view.findViewById(R.id.tv_user_getWeight);
			tv_user_getFeather=(TextView) view.findViewById(R.id.tv_user_getFeather);
			
			
			tv_user_getUserName=(TextView) view.findViewById(R.id.tv_user_getUserName);
			tv_user_getMotto=(TextView) view.findViewById(R.id.tv_user_getMotto);
			tv_user_getEmail=(TextView) view.findViewById(R.id.tv_user_getEmail);
		}


}
