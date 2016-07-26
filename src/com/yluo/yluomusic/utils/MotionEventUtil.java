package com.yluo.yluomusic.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class MotionEventUtil {

	private float mLastX;
	private float mLastY;
	private Context mContext;
	private int mTouchSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;
	private boolean mIsMove = false;
	
	private float mMinScrollSpan = 0;
	private float mMaxScrollSpan = 0;
	
	private static final int DIRECTION_X = 1; // X方向滚动
	private static final int DIRECTION_Y = -1; // Y方向滚动
	private static final int NODIRECTION = 0; // 还没有判断
	private int curMoveDirection = NODIRECTION; // 0 表示没移动,-1表示上下移动,1表示左右移动
	private float mMinTranslateSpan;
	private float mMaxTranslateSpan;
	
	public MotionEventUtil(Context context) {
		mContext = context;
		init();
	}
	
	private void init() {
		ViewConfiguration viewConfiguration = ViewConfiguration
				.get(mContext);

		mTouchSlop = viewConfiguration.getScaledTouchSlop();

		mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

		mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
	}
	
	public void recordEventXY(MotionEvent event) {
		mLastX = event.getX();
		mLastY = event.getY();
	}
	
	public float getDisX(MotionEvent event) {
		return mLastX - event.getX();
	}

	public float getDisY(MotionEvent event) {
		return mLastY - event.getY();
	}
	public boolean isMoveRight(MotionEvent event) {

		return getDisX(event) < 0;
	}
	
	public boolean isMoveLeft(MotionEvent event) {

		return getDisX(event) > 0;
	}
	
	public float calcMoveDistance(MotionEvent event) {
		float distance = (float) Math.sqrt(Math.pow(getDisX(event), 2)
				+ Math.pow(getDisY(event), 2));
		return distance;
	}
	
	public boolean isMeetMinFlingVelocity(float curVelocity){
		 return Math.abs(curVelocity) > mMinFlingVelocity;
	}
	
	public boolean isCanMove(MotionEvent event) {
		return !mIsMove  || calcMoveDistance(event) > mTouchSlop;
	}
	
	public boolean getMoveStatus() {
		return mIsMove;
	}
	
	public void setMoveStatus() {
		 mIsMove = true;
	}
	
	public void resetMoveStatus() {
		 mIsMove = false;
	}
	
	public float compensateFirstMoveDistance(float disX) {
		if (disX > 0) {
			disX -= mTouchSlop;
		} else {
			disX += mTouchSlop;
		}
		return disX;
	}
	
	public float getCurXVelocity(VelocityTracker velocityTracker) {
		velocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);

		return velocityTracker.getXVelocity();
	}
	
	public void calcMoveDirection(MotionEvent event) {
		if (curMoveDirection != NODIRECTION || !isCanMove(event)) {
			return;
		}
		float disX = getDisX(event);
		float disY = getDisY(event);

		if (disX == 0 && disY == 0) {
			return;
		}
		if (disX == 0 && disY != 0) {
			curMoveDirection = DIRECTION_Y;
			return;
		}
		float radian = Math.abs(disY) / Math.abs(disX);

		if (radian > Math.tan(30 * Math.PI / 180)) {
			curMoveDirection = DIRECTION_Y;
		} else {
			curMoveDirection  = DIRECTION_X;
		}
	}
	
	public void resetMoveDirection() {
		curMoveDirection = NODIRECTION;
	}
	
	public boolean isMoveY() {
		return curMoveDirection == DIRECTION_Y;
	}
	
	public float getRealDisX(MotionEvent event) {
		
		float disX = getDisX(event);//
		
		if (!getMoveStatus()) {
			
			if (!isCanMove(event)) {
				return 0;
			}
			disX = compensateFirstMoveDistance(disX);
			
			setMoveStatus();
		}
		return disX;
	}
	
	public void setMinTranslateSpan(float minTranslateSpan) {
		mMinTranslateSpan = minTranslateSpan;
	}
	public void setMaxTranslateSpan(float maxTranslateSpan) {
		mMaxTranslateSpan = maxTranslateSpan;
	}
	
	public int adjustTranslatePosition(float translatePosition) {

		if (translatePosition > mMinTranslateSpan) {
			translatePosition = mMinTranslateSpan;

		} else if (translatePosition < mMaxTranslateSpan) {
			translatePosition = mMaxTranslateSpan;
		}
		return (int) translatePosition;
	}
	
//	private float mMinScrollSpan = 0;
//	private float mMaxScrollSpan = 0;
}


















































