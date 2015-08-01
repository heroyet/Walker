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

	private int Progress;// 计划运动路程的总数

	private int stepTotal;// 总步数
	private float distanceTotal;// 总路程
	private long timeTotal;// 总时间

	private Activity context;
	
	
	private final static int MSG_SUCCESS = 0; //成功获取数据的标识
	private final static int MSG_FAILURE = 1; //失败获取数据的标识
	
	private Thread mThread;
	
	
	//TODO 得到数据后并且执行的操作
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
	       public void handleMessage(Message msg){  //此方法在UI线程中运行
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
	          			// 返回50条数据，如果不加上这条语句，默认返回10条数据
	          			query1.setLimit(200);
	          			// 执行查询方法
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
	            	  // Log.e("_+___________<<<<>>>>____", "失败");
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
	  * 启动新线程
	  */
	 Runnable runnable=new Runnable() {
         
         @Override
         public void run() {
              //run()在新的线程中运行
        	//TODO   ----->>
        	 String objectId=BmobUser.getCurrentUser(context).getObjectId();
        	 BmobQuery<WalkerUser> query=new BmobQuery<WalkerUser>();
        	query.getObject(context, objectId, new GetListener<WalkerUser>() {
				
				@Override
				public void onFailure(int arg0, String arg1) {
					 mHandler.obtainMessage(MSG_FAILURE).sendToTarget();//获取数据失败
				}
				
				@Override
				public void onSuccess(WalkerUser walkuser) {
					 mHandler.obtainMessage(MSG_SUCCESS,walkuser).sendToTarget();//获取数据成功
				}
			});
        	
         }
     };

	 
	 
	 
	private void setData(List<sportData> arg0) {

		// TODO Auto-generated method stub
		List<String> data1 = new ArrayList<String>();
		for (sportData data : arg0) {
			// 获得sportData的信息

			// 1.获取当前时间

			Date createdDate = new Date(System.currentTimeMillis());

			// 2.获取数据库时间

			Date bmobCreatedate = data.getCreateTime();

			// 3.把当前时间和数据库提取的时间做对比
			boolean isSame = isSameDay(createdDate, bmobCreatedate);
			@SuppressWarnings("unused")
			String username = BmobUser.getCurrentUser(getActivity())
					.getUsername();

			if (isSame) {
				timeTotal += Integer.parseInt(data.getTime());

				stepTotal += Integer.parseInt(data.getStep());// 总步数
				// distanceTotal +=Integer.parseInt(data.getDistance());;//总路程
				distanceTotal += Float.parseFloat(data.getDistance());
			}

		}

		data1.add("总时间为" + (timeTotal / 60) + "分钟");
		data1.add("总步数为" + stepTotal + "步");
		data1.add("总路程为" + (distanceTotal) + "米");

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
	 * 判断两个日期是否为同一天
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
	
	//传入上下文对象
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	
	}

}
