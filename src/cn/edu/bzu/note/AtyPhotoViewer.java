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
 * ��ʾ��Ƭ
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.photo_viewer);
		iv = (ImageView) findViewById(R.id.iv);
		String path = getIntent().getStringExtra(EXTRA_PATH);
		Bitmap b = residePhoto(path);
		if (path!=null) {
			iv.setImageBitmap(b);
			Log.e("---->>>><<<<----", path);//���Եõ�·��
		}else{
			finish();
		}
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
