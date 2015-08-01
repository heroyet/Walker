package cn.edu.bzu.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 *	�����TextView
 *@author monster
 *@date 2015-02
 */
public class MarqueeText extends TextView {
	public MarqueeText(Context context) {
		super(context);
	}
	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public boolean isFocused() {
		return true;  //Ĭ��Ϊfalse������ΪtrueΪ����ÿ��TextView����ȡ����
	}
}
