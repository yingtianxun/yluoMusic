package com.yluo.yluomusic.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class MusicProgressBar extends LinearLayout implements NestedScrollingParent{
	private static final String TAG = "MusicProgressBar";
	private Point mWindowSize;

	private Paint mPaint;

	private Rect shaderRect;

	private LinearGradient linearGradient;

	private Rect bkRect;
	private int totalWidth;
	private int maxScrollSpan;
	private NestedScrollingParentHelper mParentHelper;
	
	private Scroller mScroller;

	public MusicProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MusicProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MusicProgressBar(Context context) {
		super(context);
		init();
	}

	private void init() {
		mParentHelper = new NestedScrollingParentHelper(this);
		
		mScroller = new Scroller(getContext());
		
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						CreateShader();

						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});

	}

	private int getWindowWidth() {

		if (mWindowSize == null) {
			WindowManager wm = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);

			mWindowSize = new Point();

			wm.getDefaultDisplay().getSize(mWindowSize);
		}

		return mWindowSize.x;
	}

	private void CreateShader() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		linearGradient = new LinearGradient(0, dp2px(5), 0, 0, 0xcccccccc,
				Color.TRANSPARENT, TileMode.CLAMP);
		mPaint.setShader(linearGradient);

		shaderRect = new Rect(0, 0, getMeasuredWidth(), dp2px(5));
		bkRect = new Rect(0, dp2px(5), getMeasuredWidth(), getMeasuredHeight());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (totalWidth == 0) {
			totalWidth = changeChildWidth();
		}
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(totalWidth,
				MeasureSpec.EXACTLY);
		
		scrollToClose();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	private int changeChildWidth() {
		int totalWidth = getWindowWidth() * 2 - dp2px(70);

		maxScrollSpan = getWindowWidth() - dp2px(70);

		// // 设置左边的宽度
		MarginLayoutParams layoutParams = (MarginLayoutParams) getChildAt(0)
				.getLayoutParams();
		layoutParams.topMargin = dp2px(8);
		layoutParams.rightMargin = dp2px(20);
		layoutParams.leftMargin = dp2px(10);
		layoutParams.width = maxScrollSpan - dp2px(20) - dp2px(10);

		layoutParams = (MarginLayoutParams) getChildAt(2).getLayoutParams();
		layoutParams.topMargin = dp2px(8);
		layoutParams.leftMargin = dp2px(20);
		layoutParams.width = maxScrollSpan - dp2px(20);

		return totalWidth;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		super.onDraw(canvas);
	}

	private void drawBackground(Canvas canvas) {
		mPaint.setShader(linearGradient);
		canvas.drawRect(shaderRect, mPaint);
		mPaint.setShader(null);
		canvas.drawRect(bkRect, mPaint);

	}

	private int dp2px(float dp) {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		
		return (int) (outMetrics.density * dp + 0.5f);

	}
	public boolean onTouchEvent(MotionEvent event) { 
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		
		return true;
	}

	@Override
	public boolean onStartNestedScroll(View child, View target,
			int nestedScrollAxes) {
		return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_HORIZONTAL) != 0;
	}

	@Override
	public void onNestedScrollAccepted(View child, View target,
			int nestedScrollAxes) {

		mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
	}
	
	private void scrollToClose() {
		scrollTo(maxScrollSpan, 0);
	}
	
	@Override
	public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
		
		int scrollToX = getScrollX() + dx;
		if(scrollToX >  maxScrollSpan ) {
			scrollToX = maxScrollSpan;
			dx = 0;
		} else if(scrollToX < 0) {
			scrollToX = 0;
			dx = 0;
		}
		
		scrollTo(scrollToX, 0);
		consumed[0] = dx;
		consumed[1] = 0; // 把消费的距离放进去
	}

	public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
			int dxUnconsumed, int dyUnconsumed) {
		
	}
	public void computeScroll() {
		if(mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			invalidate();
		}
    }

	@Override
	public void onStopNestedScroll(View target) {
		
		if(getScrollX() != maxScrollSpan || getScrollX() != 0) {
			
			if(getScrollX() > maxScrollSpan /2) {
				mScroller.startScroll(getScrollX(), 0, maxScrollSpan - getScrollX(), 0,300);
			} else {
				mScroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0,300);
			}
			invalidate();
		}
		
		mParentHelper.onStopNestedScroll(target);
	}
}








