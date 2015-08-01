package cn.edu.bzu.util;

import cn.bmob.v3.Bmob;
import android.app.Activity;
import android.os.Bundle;

/**
 * 继承Activity ，实现每个界面中初始化的appid
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
