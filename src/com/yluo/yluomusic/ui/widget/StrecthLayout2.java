package com.yluo.yluomusic.ui.widget;

import android.R.integer;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class StrecthLayout2 extends RelativeLayout {
	private static final String TAG = "StrecthLayout2";
	private int mHeaderHeight;
	private int maxHeight;
	private ValueAnimator animator;
	private boolean bFlag = true;
	
	private int mCurHeight = 0;
	
	private View mHeader = null;
	
	public StrecthLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public StrecthLayout2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public StrecthLayout2(Context context) {
		super(context);
		init();
	}
//	public int getHeaderHeight() {
//		return mHeaderHeight;
//	}

	@SuppressLint("NewApi")
	private void init() {

//		getViewTreeObserver().addOnGlobalLayoutListener(
//				new OnGlobalLayoutListener() {
//
//					@Override
//					public void onGlobalLayout() {
//
//						mCurHeight = mHeaderHeight = getChildAt(0).getMeasuredHeight();
//
//						bringChildToFront(getChildAt(0));
//
//						getViewTreeObserver().removeOnGlobalLayoutListener(this);
//					}
//				});

	}
	public void shrink() {
//		if(mHeaderHeight != 0) {
			mCurHeight = mHeaderHeight;
			requestLayout();
//		}
	}
	public void open() {
		if(maxHeight != 0) {
			mCurHeight = maxHeight;
			requestLayout();
		}
	}
	
	// @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		if(mCurHeight != 0) {
			// 测量之后
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCurHeight,
					MeasureSpec.EXACTLY);
			bringChildToFront(mHeader);
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else {
			// 刚刚创建item时候测量
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			mHeader = getChildAt(0);
			mCurHeight = mHeaderHeight = getChildAt(0).getMeasuredHeight();
			maxHeight = mHeaderHeight + getChildAt(1).getMeasuredHeight();
			bringChildToFront(mHeader);
			
//			heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCurHeight,
//					MeasureSpec.EXACTLY);
			
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// headerHeight = getChildAt(0).getMeasuredHeight();
		// bodyHeight = getChildAt(1).getMeasuredHeight();
		// // Log.d(TAG, "headerHeight:" + headerHeight +",bodyHeight:" +
		// bodyHeight);
		// maxHeight = headerHeight + bodyHeight;

		// StrecthLayout2.this.layout(StrecthLayout2.this.getLeft(),
		// StrecthLayout2.this.getTop(),StrecthLayout2.this.getRight(),
		// StrecthLayout2.this.getTop() + maxHeight);

		// StrecthLayout2.this.setMinimumHeight(maxHeight);

		// layoutParams = (MarginLayoutParams) getLayoutParams();
		// layoutParams.height = headerHeight;
		// setLayoutParams(layoutParams);
		// bringChildToFront(getChildAt(0));

//		maxHeight = getChildAt(1).getMeasuredHeight()
//				+ getChildAt(0).getMeasuredHeight();
		
		

	}

	public void strecth(int gridViewHeigh) {
		
		maxHeight = mHeaderHeight + gridViewHeigh;
		Log.d(TAG, "strecth:flag="+bFlag);
		if (bFlag) {
			animator = ValueAnimator.ofInt(mHeaderHeight, maxHeight);
			bFlag = false;
		} else {
			animator = ValueAnimator.ofInt(maxHeight, mHeaderHeight);
			bFlag = true;
		}
		animator.setDuration(300);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mCurHeight = (Integer) animation.getAnimatedValue();
				requestLayout();
			}
		});
		animator.start();
	}

}
