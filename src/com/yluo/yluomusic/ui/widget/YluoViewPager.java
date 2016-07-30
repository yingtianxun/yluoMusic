package com.yluo.yluomusic.ui.widget;

import com.yluo.yluomusic.utils.MotionEventUtil;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class YluoViewPager extends ViewPager {

	private static final String TAG = "YluoViewPager";

	private MotionEventUtil eventUtil;

	private float mCurItemOffset = 0;

	private int mCurItem = 0;
	
	private boolean mIsHandle = false;

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
			public void onPageScrolled(int curItem, float curItemOffset,
					int arg2) {
				mCurItem = curItem;
				mCurItemOffset = curItemOffset;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			mIsHandle = false;
			eventUtil.resetMoveDirection();
			eventUtil.recordEventXY(event);
			eventUtil.resetMoveStatus();
			break;
		case MotionEvent.ACTION_MOVE:
			eventUtil.calcMoveDirection(event);
			// 上下移动的话就不做和任何处理
			if (eventUtil.isMoveY()) {
				getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
			break;
		default:
			break;
		}
		
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			eventUtil.resetMoveStatus();
			eventUtil.resetMoveDirection();
//			Log.d(TAG, "--------222-------");
			getParent().requestDisallowInterceptTouchEvent(true);
			
			break;
		case MotionEvent.ACTION_MOVE: {
			
			eventUtil.calcMoveDirection(event);
			// 上下移动的话就不做和任何处理
				
			
			if (eventUtil.isMoveY()) {
//				Log.d(TAG, "----------上下不拦截-----");
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}
			
			 eventUtil.getRealDisX(event);
			
//			float disX = eventUtil.getRealDisX(event);
			
//			Log.d(TAG, "ViewPager---disX:" + disX + ",rawX:" + eventUtil.getRealDisX(event));
			
//			 ((disX == eventUtil.getRealDisX(event)) &&
			
			if ( !mIsHandle && (isMeetLeftPosition(event) || isMeetRightPosition(event))) {
//				Log.d(TAG, "---------------------------水平不拦截的");
				getParent().requestDisallowInterceptTouchEvent(false);

			} else {
				mIsHandle = true;
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
		
		return  mCurItem == 0 && eventUtil.isMoveRight(event)
				&& mCurItemOffset == 0.0f;
		
//		Log.d(TAG, "左边移动-----mCurItem:" + mCurItem 
//				+ ",isRight:" + eventUtil.isMoveRight(event) 
//				+ ",mCurItemOffset:" + mCurItemOffset + ",result:" + result);
		
//		return result;
	}
	private boolean isMeetRightPosition(MotionEvent event) {
		
		return  mCurItem == (getAdapter().getCount() - 1)
				&& eventUtil.isMoveLeft(event) && mCurItemOffset == 0.0f;
		
//		Log.d(TAG, "移动-----mCurItem:" + mCurItem 
//				+ ",isRight:" + eventUtil.isMoveLeft(event) 
//				+ ",mCurItemOffset:" + mCurItemOffset + ",result:" + result);
//		
//		return result;
		
	}

}
