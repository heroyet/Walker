package cn.edu.bzu.adapter;

import java.util.List;

import cn.edu.bzu.entity.WeatherInfo;
import cn.edu.bzu.walker.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 用于显示天气的适配器
 * @author monster
 * @date 2015-06-26
 */
public class WeatherAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private Context context;
	private List<WeatherInfo> list;
	private LayoutInflater mInflater;
	
	public WeatherAdapter(Context context,List<WeatherInfo> list) {
		this.context=context;
		this.list=list;
		mInflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int positon, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.list_item_weather, null); //布局转化为视图
			viewHolder.tv_week=(TextView) convertView.findViewById(R.id.tv_week);
			viewHolder.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
			viewHolder.tv_temperature=(TextView) convertView.findViewById(R.id.tv_temperature);
			viewHolder.tv_weather=(TextView) convertView.findViewById(R.id.tv_weather);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.tv_weather.setText(list.get(positon).weather);
		viewHolder.tv_week.setText(list.get(positon).week);
		viewHolder.tv_date.setText(list.get(positon).date);
		viewHolder.tv_temperature.setText(list.get(positon).temperature);
		return convertView;
	}

	class ViewHolder{
		public TextView tv_week,tv_date,tv_temperature,tv_weather;
	}
}
