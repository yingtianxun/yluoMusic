package com.yluo.yluomusic.ui.widget;

import com.yluo.utils.DpTranToPx;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class PictureRecycle extends ViewPager {
	private static final String TAG = "PictureRecycle";
	private Paint paint;
	private float radius = 0;
	private float indicatorContainerLen;
	private float circleY;
	private int divideSpan = 0;
	private int drawRadius;
	private float mLastX;
	private float mLastY;
	

	public PictureRecycle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PictureRecycle(Context context) {
		super(context);
		init();
	}

	private void init() {

		radius = DpTranToPx.dp2px(getContext(), 10);
		divideSpan = DpTranToPx.dp2px(getContext(), 18);

		drawRadius = DpTranToPx.dp2px(getContext(), 5);
		paint = new Paint();
		paint.setStrokeWidth(5);

		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE: {

			float disY = event.getY() - mLastY;
			float disX = event.getX() - mLastX;

			float distance = calcMoveDistance(disX, disY);

			if (distance == 0) {
				break;
			}

			if (getCurrentItem() == 0 && disX > 0 ) {

				getParent().requestDisallowInterceptTouchEvent(false);

				break;
			}

			if (getCurrentItem() == getAdapter().getCount() - 1 && disX < 0) {

				getParent().requestDisallowInterceptTouchEvent(false);

				break;
			}

		}
			break;

		default:
			break;
		}

		mLastX = event.getX();
		mLastY = event.getY();
		return super.onTouchEvent(event);

	}

	private float calcMoveDistance(float x, float y) {

		float distance = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

		return distance;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		paint.setColor(0xffffffff);
		paint.setAlpha(255);

		indicatorContainerLen = (getAdapter().getCount() - 1) * divideSpan
				+ radius * 2;

		circleY = getHeight() - DpTranToPx.dp2px(getContext(), drawRadius);

		// »­ÔÚÖÐ¼ä
		float drawCircleLeft = getScrollX() + getWidth() / 2
				- indicatorContainerLen / 2;

		for (int i = 0; i < getAdapter().getCount(); i++) {
			canvas.drawCircle(drawCircleLeft + divideSpan * i, circleY,
					drawRadius, paint);
		}

		paint.setColor(0xfff67353);

		float ratio = (getScrollX() - (getCurrentItem() * getWidth()))
				/ (getWidth() * 1.0f);

		canvas.drawCircle(drawCircleLeft + divideSpan
				* (getCurrentItem() + ratio), circleY, drawRadius, paint);
	}

}
