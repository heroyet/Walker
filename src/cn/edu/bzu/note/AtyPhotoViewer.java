package cn.edu.bzu.note;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import cn.edu.bzu.walker.R;

/**
 * 显示照片
 * @author lb
 * @date 2015-07-30
 *
 */
public class AtyPhotoViewer extends Activity {
	public static final String EXTRA_PATH = "path";
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.photo_viewer);
		iv = (ImageView) findViewById(R.id.iv);
		String path = getIntent().getStringExtra(EXTRA_PATH);
		Bitmap b = residePhoto(path);
		if (path!=null) {
			iv.setImageBitmap(b);
			Log.e("---->>>><<<<----", path);//可以得到路径
		}else{
			finish();
		}
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
