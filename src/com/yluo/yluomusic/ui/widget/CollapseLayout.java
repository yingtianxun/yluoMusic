package com.yluo.yluomusic.ui.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

public class CollapseLayout extends LinearLayout {
	private View changeSizeView;
	private int height;
	private LayoutParams layoutParams;
	private boolean bFlag = true;
	private ValueAnimator animator;
	
	public CollapseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CollapseLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}

	public CollapseLayout(Context context) {
		super(context);
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		
		setOrientation(VERTICAL);
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				changeSizeView = getChildAt(1);
				height = changeSizeView.getMeasuredHeight();
				layoutParams = (LayoutParams)changeSizeView.getLayoutParams();
				layoutParams.height = 0;
				changeSizeView.setLayoutParams(layoutParams);
				getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});
	}
	public void strecth() {
		if (bFlag) {
			animator = ValueAnimator.ofInt(0,height);
			bFlag = false;
		}else {
			animator = ValueAnimator.ofInt(height,0);
			bFlag = true;
		}
		animator.setDuration(200);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				layoutParams.height = (Integer)animation.getAnimatedValue();
				changeSizeView.setLayoutParams(layoutParams);
			}
		});
		animator.start();
	}
}
