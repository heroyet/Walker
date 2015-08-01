package cn.edu.bzu.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 *	跑马灯TextView
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
		return true;  //默认为false，设置为true为了让每个TextView都获取焦点
	}
}
