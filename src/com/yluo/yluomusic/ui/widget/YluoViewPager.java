package com.yluo.yluomusic.ui.widget;

import com.yluo.yluomusic.utils.MotionEventUtil;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class YluoViewPager extends ViewPager{
	
	private static final String TAG = "YluoViewPager";

	private MotionEventUtil eventUtil;
	
	private float mCurItemOffset = 0;
	
	private int mCurItem = 0;
	
	public YluoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public YluoViewPager(Context context) {
		super(context);
		init();
			
	}
	private void init() {
		eventUtil = new MotionEventUtil(getContext());
		
		this.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
			}
			
			@Override
			public void onPageScrolled(int curItem, float curItemOffset, int arg2) {
				mCurItem = curItem;
				mCurItemOffset = curItemOffset;
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:{
			
			if(isMeetLeftPosition(event)) {
				
				getParent().requestDisallowInterceptTouchEvent(false);
				
			} else if( isMeetRightPosition(event)){
				
				getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
			break;
		default:
			break;
		}
		
		eventUtil.recordEventXY(event);
		return super.onTouchEvent(event);
	}
	private boolean isMeetLeftPosition(MotionEvent event) {
		return mCurItem == 0 && eventUtil.isMoveRight(event) && mCurItemOffset == 0.0f;
	}
	private boolean isMeetRightPosition(MotionEvent event) {
		return mCurItem == (getAdapter().getCount() - 1) &&!eventUtil.isMoveRight(event) && mCurItemOffset == 0.0f;
	}

}
