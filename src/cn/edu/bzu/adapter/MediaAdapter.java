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
 * 显示照片的列表
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
	 * 此方法用于压缩图片
	 */
	private Bitmap residePhoto(String path) {
		BitmapFactory.Options options=new BitmapFactory.Options();
		//如果设置为true，解码器将返回null（没有位图），但出…领域仍将设置，允许调用者查询位图不用其像素分配内存。
		options.inJustDecodeBounds=true;

		BitmapFactory.decodeFile(path,options); //将解码文件转换成位图
		double radio=Math.max(options.outWidth * 1.0d/1024f,options.outHeight *1.0d/1024f);
		//如果设置的值大于1，要求解码器子样的原始图像，返回一个较小的图像保存记忆。 -->>来自官网帮助文档的解析
		options.inSampleSize= (int) Math.ceil(radio);
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(path,options); //压缩过后的位图
	}

}