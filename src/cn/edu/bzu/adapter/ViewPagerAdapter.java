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
	 * ��ȡViewpagerҳ��������
	 */
	@Override
	public int getCount() {
		return mViewList.size();
	}

	/**
	 * �ж������Ƿ����
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * ʵ����һ��ҳ��
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViewList.get(position));
		return mViewList.get(position);
	}
	
	/**
	 * ����һ��ҳ��
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViewList.get(position));
	}
}
