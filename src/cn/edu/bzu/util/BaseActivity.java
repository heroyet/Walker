package cn.edu.bzu.util;

import cn.bmob.v3.Bmob;
import android.app.Activity;
import android.os.Bundle;

/**
 * �̳�Activity ��ʵ��ÿ�������г�ʼ����appid
 * @author monster
 *@date:2015-06-25
 */
public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, "ec474218c0f9d695c62f5084fb532e00");//APP ID
	}
}
