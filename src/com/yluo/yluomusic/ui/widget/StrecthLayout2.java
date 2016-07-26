package com.yluo.yluomusic.ui.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class StrecthLayout2 extends RelativeLayout {
	private static final String TAG = "StrecthLayout2";
	private int headerHeight;
	private int bodyHeight;
	private int maxHeight;
	private ValueAnimator animator;
	private boolean bFlag = true;
	private MarginLayoutParams layoutParams;

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

	@SuppressLint("NewApi")
	private void init() {

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						try {
							headerHeight = getChildAt(0).getMeasuredHeight();
							bodyHeight = getChildAt(1).getMeasuredHeight();
							maxHeight = headerHeight + bodyHeight;
							layoutParams = (MarginLayoutParams) getLayoutParams();
							layoutParams.height = headerHeight;
							setLayoutParams(layoutParams);
							bringChildToFront(getChildAt(0));
						} catch (Exception e) {
							// TODO: handle exception
							e.getStackTrace();
						}
						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});

	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// headerHeight = getChildAt(1).getMeasuredHeight();
	// int bodyHeight = getChildAt(0).getMeasuredHeight();
	// maxHeight = headerHeight + bodyHeight;
	// heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
	// MeasureSpec.EXACTLY);
	//
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// }
	public void strecth() {

		if (bFlag) {
			animator = ValueAnimator.ofInt(headerHeight, maxHeight);
			bFlag = false;
		} else {
			animator = ValueAnimator.ofInt(maxHeight, headerHeight);
			bFlag = true;
		}
		animator.setDuration(5000);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				layoutParams.height = (Integer) animation.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});
		animator.start();
	}

}
