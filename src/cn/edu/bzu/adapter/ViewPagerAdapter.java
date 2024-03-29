package cn.edu.bzu.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

	private List<View> mViewList;
	
	public ViewPagerAdapter(List<View> viewList) {
		mViewList = viewList;
	}
	/**
	 * 获取Viewpager页卡的数量
	 */
	@Override
	public int getCount() {
		return mViewList.size();
	}

	/**
	 * 判断类型是否符合
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * 实例化一个页卡
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViewList.get(position));
		return mViewList.get(position);
	}
	
	/**
	 * 销毁一个页卡
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViewList.get(position));
	}
}
