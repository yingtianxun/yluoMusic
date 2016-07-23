package com.yluo.yluomusic.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class YluoViewPager extends ViewPager{
	
	
	private float mLastX = 0;
	private float mLastY;
	private int mTouchSlop;
	private boolean mIsMove;
	
	public YluoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public YluoViewPager(Context context) {
		super(context);
		init();
			
	}
	private void init() {
		ViewConfiguration viewConfiguration = ViewConfiguration
				.get(getContext());

		mTouchSlop = viewConfiguration.getScaledTouchSlop();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			getParent().requestDisallowInterceptTouchEvent(true);
		} else if(event.getAction() == MotionEvent.ACTION_MOVE){
						
			if(getCurrentItem() == 0 && isMoveRight(event)) {
				getParent().requestDisallowInterceptTouchEvent(false);
			} else if(getCurrentItem() == (getAdapter().getCount() - 1) && !isMoveRight(event)){
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
		}
		
		mLastX = event.getX();
		mLastY = event.getY();
		return super.onTouchEvent(event);
	}
	private boolean isMoveRight(MotionEvent event) {
		
		return getDisX(event) < 0 ;
	}
	
	private boolean isCanMove(MotionEvent event) {
		return  !mIsMove || calcMoveDistance(event) > mTouchSlop;
	}
	
	private float calcMoveDistance(MotionEvent event) {
		float distance = (float) Math.sqrt(Math.pow(getDisX(event), 2)
				+ Math.pow(getDisY(event), 2));
		return distance;
	}
	private float getDisX(MotionEvent event) {
		return mLastX - event.getX();
	}

	private float getDisY(MotionEvent event) {
		return mLastY - event.getY();
	}
}
