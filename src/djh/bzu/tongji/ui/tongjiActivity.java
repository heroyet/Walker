package djh.bzu.tongji.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.edu.bzu.entity.sportData;
import cn.edu.bzu.walker.R;
import djh.bzu.tongji.ui.proggress.CircleProgressBar;

public class tongjiActivity extends Activity {

	private ListView list1 = null;
	private ImageView circlePointImg;

	private CircleProgressBar mBar;

	private int Progress;// 计划运动路程的总数

	private int stepTotal;// 总步数
	private float distanceTotal;// 总路程
	private long timeTotal;// 总时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.djh_tongji);

		Progress = 10;
		list1 = (ListView) super.findViewById(R.id.tv_list);

		BmobQuery<sportData> query = new BmobQuery<sportData>();
		
		query.addWhereEqualTo("userName", "zhangsan");
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 执行查询方法
		query.findObjects(this, new FindListener<sportData>() {

			@Override
			public void onSuccess(List<sportData> arg0) {
				// TODO Auto-generated method stub

				setData(arg0);

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void setData(List<sportData> arg0) {

		List<String> data1 = new ArrayList<String>();
		for (sportData data : arg0) {
			// 获得sportData的信息

			// 1.获取当前时间
			
			Date createdDate = new Date(System.currentTimeMillis());

			// 2.获取数据库时间

			Date bmobCreatedate = data.getCreateTime();

			// 3.把当前时间和数据库提取的时间做对比
			boolean isSame = isSameDay(createdDate, bmobCreatedate);
			String username=BmobUser.getCurrentUser(tongjiActivity.this).getUsername();
			if (isSame && username == data.getUserName()) {
			    timeTotal +=Integer.parseInt(data.getTime());
				
				stepTotal += Integer.parseInt(data.getStep());// 总步数
				// distanceTotal +=Integer.parseInt(data.getDistance());;//总路程
				distanceTotal += Float.parseFloat(data.getDistance());
			}

		}
		
		 
		 
		
		
		data1.add("总时间为" + (timeTotal ) + "分钟");
		data1.add("总步数为" + stepTotal + "步");
		data1.add("总路程为" + (distanceTotal) + "米");

		list1.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, data1));

		circlePointImg = (ImageView) findViewById(R.id.circle_point_img);
		mBar = (CircleProgressBar) findViewById(R.id.myProgress);

		int temp = (int) (distanceTotal/Progress*100);
		mBar.setProgress(temp, circlePointImg);

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

}