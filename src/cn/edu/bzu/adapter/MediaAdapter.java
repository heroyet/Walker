package cn.edu.bzu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.bzu.entity.MediaListCellData;
import cn.edu.bzu.walker.R;

/**
 * ��ʾ��Ƭ���б�
 * @author lb
 * @date 2015-07-30
 */
public class MediaAdapter extends BaseAdapter {

	private Context context;
	private List<MediaListCellData> list = new ArrayList<MediaListCellData>();


	public MediaAdapter(Context context) {
		this.context = context;
	}

	public void add(MediaListCellData data){
		list.add(data);
	}
	
	public void delete(int position){
		list.remove(position);
	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MediaListCellData getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView==null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.media_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
			viewHolder.tvPath = (TextView) convertView.findViewById(R.id.tvPath);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MediaListCellData data = getItem(position);

		;
		viewHolder.ivIcon.setImageBitmap(residePhoto(data.getPath()));
		viewHolder.tvPath.setText(data.getPath());
		return convertView;
	}

	private class ViewHolder{
		private ImageView ivIcon;
		private TextView tvPath;
	}

	/**
	 * �˷�������ѹ��ͼƬ
	 */
	private Bitmap residePhoto(String path) {
		BitmapFactory.Options options=new BitmapFactory.Options();
		//�������Ϊtrue��������������null��û��λͼ���������������Խ����ã���������߲�ѯλͼ���������ط����ڴ档
		options.inJustDecodeBounds=true;

		BitmapFactory.decodeFile(path,options); //�������ļ�ת����λͼ
		double radio=Math.max(options.outWidth * 1.0d/1024f,options.outHeight *1.0d/1024f);
		//������õ�ֵ����1��Ҫ�������������ԭʼͼ�񣬷���һ����С��ͼ�񱣴���䡣 -->>���Թ��������ĵ��Ľ���
		options.inSampleSize= (int) Math.ceil(radio);
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(path,options); //ѹ�������λͼ
	}

}