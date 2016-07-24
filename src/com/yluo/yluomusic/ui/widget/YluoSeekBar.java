package com.yluo.yluomusic.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
/**
 * 
 * @author 樱天寻
 *
 */
public class YluoSeekBar extends SeekBar implements OnSeekBarChangeListener {
	private static final String TAG = "YluoSeekBar";

	private float mThumbMinRadius = 4;
	
	private float mThumbMaxRadius = 7;

	private float mThumbRadius;

	private int mThumbColor = 0xff51CCFF;

	private int mDeterminProgressColor = 0xff51CCFF;

	private int mIndeterminProgressBKColor = 0xffD8D8D8;

	private float mProgressBarHeight = 3;

	private float mDrawProgressMaxSpan = 0;

	private float mDrawThumbMaxSpan = 0;
	private int mIsdrawBiger = 0;

	private Paint mPaint;
	
	private boolean isFirstMove = true;
	
	private int lastProces = 0;
	
	private OnSeekBarChangeListener mOnSeekBarChangeListener;

	public YluoSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public YluoSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public YluoSeekBar(Context context) {
		super(context);
		init();
	}

	private void init() {

		mThumbMinRadius = dp2px(mThumbMinRadius);

		mThumbMaxRadius = dp2px( mThumbMaxRadius);

		mThumbRadius = mThumbMinRadius;

		super.setOnSeekBarChangeListener(this);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		mPaint.setColor(Color.RED);
		
		mPaint.setStrokeWidth(1);
		
		mPaint.setStyle(Style.FILL);
	}

	public void setmDeterminProgressColor(int mDeterminProgressColor) {

		this.mDeterminProgressColor = mDeterminProgressColor;
	}

	public void setmIndeterminProgressColor(int mIndeterminProgressColor) {

		this.mIndeterminProgressBKColor = mIndeterminProgressColor;
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mDrawThumbMaxSpan = MeasureSpec.getSize(widthMeasureSpec)
				- getPaddingLeft() - getPaddingRight();

		// 这个是背景条单位

		mDrawProgressMaxSpan = mDrawThumbMaxSpan - mThumbRadius * 2;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!isTouchDownOnThumb(event)) {
				return false;
			}
		}
		return super.onTouchEvent(event);
	}

	private boolean isTouchDownOnThumb(MotionEvent event) {

		float curMaxTouchProgress = PositionToProgress(event.getX() + 2
				* mThumbRadius);

		float curMinTouchProgress = PositionToProgress(event.getX() - 2
				* mThumbRadius);

		if (getProgress() >= curMinTouchProgress
				&& getProgress() <= curMaxTouchProgress) {
			return true;
		}

		return false;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		drawProgress(canvas);
		if(mIsdrawBiger == -1) {
			drawSmallThumb(canvas);
			drawHalfBigThumb(canvas);
		} else {
			drawThumb(canvas);
		}		
	}

	private void drawHalfBigThumb(Canvas canvas) {
		mPaint.setColor(mThumbColor);

		float left = getThumDrawPosition() - mThumbMaxRadius;
		float top = getMeasuredHeight() / 2 - mThumbMaxRadius;
		
		RectF rect = new RectF(left, top, left + mThumbMaxRadius * 2, top+ mThumbMaxRadius * 2);
		
		canvas.drawArc(rect, -90, 180, true, mPaint);
	}

	private void drawSmallThumb(Canvas canvas) {
		mPaint.setColor(mThumbColor);

		canvas.drawCircle(getThumDrawPosition(), getMeasuredHeight() / 2,
				mThumbMinRadius, mPaint);
	}

	private void drawThumb(Canvas canvas) {

		mPaint.setColor(mThumbColor);

		canvas.drawCircle(getThumDrawPosition(), getMeasuredHeight() / 2,
				mThumbRadius, mPaint);
	}
	private float getThumDrawPosition() {
		return getPaddingLeft() + (getProgress() * 1.0f / getMax())
				* mDrawThumbMaxSpan;
	}
	private void drawProgress(Canvas canvas) {
		drawDeterminProgress(canvas);
		drawIndeterminProgress(canvas);
	}
	private void drawDeterminProgress(Canvas canvas) {

		mPaint.setColor(mDeterminProgressColor);

		canvas.drawRect(getDeterStartDrawPosition(), getDrawPregrossStartY(),
				getThumDrawPosition(), getDrawPregrossStartY()
						+ mProgressBarHeight, mPaint);
	}
	private void drawIndeterminProgress(Canvas canvas) {
		mPaint.setColor(mIndeterminProgressBKColor);

		canvas.drawRect(getThumDrawPosition(), getDrawPregrossStartY(),
				getIndeterEndDrawPosition(), getDrawPregrossStartY()
						+ mProgressBarHeight, mPaint);
	}

	private float getDrawPregrossStartY() {
		return (getMeasuredHeight() - mProgressBarHeight) / 2;
	}

	private float getDeterStartDrawPosition() {

		return getPaddingLeft();
	}

	private float getIndeterEndDrawPosition() {

		return getPaddingLeft() + mThumbRadius * 2 + mDrawProgressMaxSpan;
	}
	private float PositionToProgress(float touchDownX) {
		final int width = getWidth();
		final int available = width - getPaddingLeft() - getPaddingRight();
		final int x = (int) touchDownX;
		float scale;
		if (x < getPaddingLeft()) {
			scale = 0.0f;
		} else if (x > width - getPaddingRight()) {
			scale = 1.0f;
		} else {
			scale = (float) (x - getPaddingLeft()) / (float) available;
		}
		final int max = getMax();
		return scale * max;
	}

	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		mOnSeekBarChangeListener = l;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(Math.abs(lastProces - getProgress()) < (getMax() * 0.05f) && isFirstMove) {
			mIsdrawBiger = -1;
			isFirstMove = false;
		} else {
			mIsdrawBiger = 1;
		}
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mThumbRadius = mThumbMaxRadius;
		lastProces = getProgress();
		mIsdrawBiger = -1;
		isFirstMove = true;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mThumbRadius = mThumbMinRadius;
		mIsdrawBiger = 0;
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onProgressChanged(seekBar, getProgress(),
					false);
		}
	}
	public int dp2px(float dp) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics()) + 0.5f);
	}
}
