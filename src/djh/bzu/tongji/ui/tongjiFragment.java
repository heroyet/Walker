package djh.bzu.tongji.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.entity.WalkerUser;
import cn.edu.bzu.entity.sportData;
import cn.edu.bzu.walker.R;
import djh.bzu.tongji.ui.proggress.CircleProgressBar;

public class tongjiFragment extends Fragment {

	private View layout;
	private ListView list1 = null;
	private ImageView circlePointImg;

	private CircleProgressBar mBar;

	private int Progress;// �ƻ��˶�·�̵�����

	private int stepTotal;// �ܲ���
	private float distanceTotal;// ��·��
	private long timeTotal;// ��ʱ��

	private Activity context;
	
	
	private final static int MSG_SUCCESS = 0; //�ɹ���ȡ���ݵı�ʶ
	private final static int MSG_FAILURE = 1; //ʧ�ܻ�ȡ���ݵı�ʶ
	
	private Thread mThread;
	
	
	//TODO �õ����ݺ���ִ�еĲ���
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
	       public void handleMessage(Message msg){  //�˷�����UI�߳�������
	           switch(msg.what){
	               case MSG_SUCCESS:
	                  WalkerUser walkuser=(WalkerUser) msg.obj;
	                  Progress= Integer.parseInt(walkuser.getUserFeatherSport());
	                //  Log.e("======>>>>>>>>>>>>>>>>>>>", ""+Progress);
	                  if(Progress!=0){
	          			//int temp = (int) (stepTotal / Progress * 100);
//	          			mBar.setProgress(temp, circlePointImg);
	          			list1 = (ListView) layout.findViewById(R.id.tv_list);

	          			BmobQuery<sportData> query1 = new BmobQuery<sportData>();
	          			String username1 = BmobUser.getCurrentUser(getActivity()).getUsername();
	          			query1.addWhereEqualTo("userName", username1);
	          			// ����50�����ݣ����������������䣬Ĭ�Ϸ���10������
	          			query1.setLimit(200);
	          			// ִ�в�ѯ����
	          			query1.findObjects(getActivity(), new FindListener<sportData>() {

	          				@Override
	          				public void onSuccess(List<sportData> arg0) {
	          					setData(arg0);

	          				}

	          				@Override
	          				public void onError(int arg0, String arg1) {
	          				}
	          			});

	          		}else {
	          			mBar.setProgress(0, circlePointImg);
	          		}
	                  
	                  
	                   break;   
	               case MSG_FAILURE:
	            	  // Log.e("_+___________<<<<>>>>____", "ʧ��");
	                   break;
	           }
	       }
	        
	   };
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.djh_tongji, container, false);
		return layout;
	}

	 @Override
	 public void onViewCreated(View view, Bundle savedInstanceState) {
	 super.onViewCreated(view, savedInstanceState);
 
	 if(mThread==null){
         mThread=new Thread(runnable);
         mThread.start();
     }
	 
	 }
	 
	 /**
	  * �������߳�
	  */
	 Runnable runnable=new Runnable() {
         
         @Override
         public void run() {
              //run()���µ��߳�������
        	//TODO   ----->>
        	 String objectId=BmobUser.getCurrentUser(context).getObjectId();
        	 BmobQuery<WalkerUser> query=new BmobQuery<WalkerUser>();
        	query.getObject(context, objectId, new GetListener<WalkerUser>() {
				
				@Override
				public void onFailure(int arg0, String arg1) {
					 mHandler.obtainMessage(MSG_FAILURE).sendToTarget();//��ȡ����ʧ��
				}
				
				@Override
				public void onSuccess(WalkerUser walkuser) {
					 mHandler.obtainMessage(MSG_SUCCESS,walkuser).sendToTarget();//��ȡ���ݳɹ�
				}
			});
        	
         }
     };

	 
	 
	 
	private void setData(List<sportData> arg0) {

		// TODO Auto-generated method stub
		List<String> data1 = new ArrayList<String>();
		for (sportData data : arg0) {
			// ���sportData����Ϣ

			// 1.��ȡ��ǰʱ��

			Date createdDate = new Date(System.currentTimeMillis());

			// 2.��ȡ���ݿ�ʱ��

			Date bmobCreatedate = data.getCreateTime();

			// 3.�ѵ�ǰʱ������ݿ���ȡ��ʱ�����Ա�
			boolean isSame = isSameDay(createdDate, bmobCreatedate);
			@SuppressWarnings("unused")
			String username = BmobUser.getCurrentUser(getActivity())
					.getUsername();

			if (isSame) {
				timeTotal += Integer.parseInt(data.getTime());

				stepTotal += Integer.parseInt(data.getStep());// �ܲ���
				// distanceTotal +=Integer.parseInt(data.getDistance());;//��·��
				distanceTotal += Float.parseFloat(data.getDistance());
			}

		}

		data1.add("��ʱ��Ϊ" + (timeTotal / 60) + "����");
		data1.add("�ܲ���Ϊ" + stepTotal + "��");
		data1.add("��·��Ϊ" + (distanceTotal) + "��");

		list1.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, data1));

		circlePointImg = (ImageView) layout.findViewById(R.id.circle_point_img);
		mBar = (CircleProgressBar) layout.findViewById(R.id.myProgress);

		if(Progress!=0){
			int temp = (int) ((float)stepTotal / Progress * 100);
			mBar.setProgress(temp, circlePointImg);

		}else {
			//mBar.setProgress(0, circlePointImg);
		}
		
	}

	/**
	 * �ж����������Ƿ�Ϊͬһ��
	 * */
	@SuppressLint("SimpleDateFormat")
	public boolean isSameDay(Date day1, Date day2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ds1 = sdf.format(day1);
		String ds2 = sdf.format(day2);
		if (ds1.equals(ds2)) {
			return true;
		} else {
			return false;
		}
	}
	
	//���������Ķ���
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	
	}

}
