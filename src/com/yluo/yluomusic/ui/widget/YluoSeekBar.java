package com.yluo.yluomusic.ui.widget;

import com.yluo.yluomusic.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
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

	private float mThumbMinRadius = 4; // 绘制的小的半径

	private float mThumbMaxRadius = 7; // 绘制大的半径
	private float mThumbRadius; // 当前使用的半径

	private int mThumbColor = 0xff51CCFF; // 拖动圆形的颜色

	private int mDeterminProgressColor = 0xff51CCFF; // 跑过的颜色

	private int mIndeterminProgressBKColor = 0xffD8D8D8; // 没跑过的颜色

	private float mProgressBarHeight = 3; // 进度条的高度

	private float mDrawThumbMaxSpan = 0; // 拖动条可以绘制的最大范围

	private int mIsdrawBiger = 0; // 当前是不是在画大的thumb

	private Paint mPaint;

	private boolean isFirstMove = false; // 是不是第一次移动用来判断画半圆的

	private int lastProces = 0; // 上一次的进度

	private boolean mIsWithTime = false;

//	private String mCurProcessTime = "0:00";
//
//	private String mTotalTime = "4:02";

	private OnSeekBarChangeListener mOnSeekBarChangeListener;

	private boolean mIsFirstCalc = false; // 用来判断减去两边字体宽度的

//	private boolean mIsTouch;

//	private int originPaddingLeft = 0;
//
//	private int originPaddingRight = 0;
//
//	private Rect mCurTimetextBound;
//
//	private Rect mTotalTimetextBound;

	public YluoSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(attrs);
	}

	public YluoSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(attrs);
	}

	public YluoSeekBar(Context context) {
		super(context);
		initAttrs(null);
	}

	private void initAttrs(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.YluoSeekBar);

			for (int i = 0; i < a.length(); i++) {
				switch (a.getIndex(i)) {
				case R.styleable.YluoSeekBar_show_time_indicator:
					mIsWithTime = a.getBoolean(
							R.styleable.YluoSeekBar_show_time_indicator, false);
					break;
				case R.styleable.YluoSeekBar_thumb_color:
					mThumbColor = a.getColor(
							R.styleable.YluoSeekBar_thumb_color, 0xff51CCFF);
					break;
				case R.styleable.YluoSeekBar_determin_color:

					mDeterminProgressColor = a.getColor(
							R.styleable.YluoSeekBar_indetermin_color,
							0xff51CCFF);
					break;
				case R.styleable.YluoSeekBar_indetermin_color:

					mIndeterminProgressBKColor = a.getColor(
							R.styleable.YluoSeekBar_indetermin_color,
							0xffD8D8D8);
					break;
				default:
					break;
				}
			}

			a.recycle();
		}

		init();
	}

	private void init() {

		mThumbMinRadius = dp2px(mThumbMinRadius);

		mThumbMaxRadius = dp2px(mThumbMaxRadius);

		mThumbRadius = mThumbMinRadius;

		super.setOnSeekBarChangeListener(this);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		mPaint.setColor(Color.RED);

		mPaint.setStrokeWidth(1);

		mPaint.setStyle(Style.FILL);

		mPaint.setTextSize(dp2px(12));

		if (mIsWithTime) {
			mThumbRadius = mThumbMaxRadius;//
		}
		// 设置新的padding
//		if (mIsWithTime) {
//			originPaddingLeft = getPaddingLeft();
//			originPaddingRight = getPaddingRight();
//
//			mCurTimetextBound = new Rect();
//
//			mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
//					mCurTimetextBound);
//
//			mTotalTimetextBound = new Rect();
//
//			mPaint.getTextBounds(mTotalTime, 0, mTotalTime.length(), mTotalTimetextBound);
//			
//			setPadding(originPaddingLeft*2 + mCurTimetextBound.width()
//					, top
//					, right
//					, bottom);
//
//		}

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

		// Rect textBound = new Rect();
		//
		// mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
		// textBound);
		//
		// Rect textBound = new Rect();
		//
		// mPaint.getTextBounds(mTotalTime, 0, mTotalTime.length(), textBound);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mDrawThumbMaxSpan = MeasureSpec.getSize(widthMeasureSpec)
				- getPaddingLeft() - getPaddingRight();
		mIsFirstCalc = false;

		// 这个是背景条单位

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mIsWithTime) {
				getParent().requestDisallowInterceptTouchEvent(true);
				return true;
//				if (isTouchDownOnTimeIndicatorThumb(event)) {
//					
//				} else {
//					
//				}
			} else {

				if (isTouchDownOnThumb(event)) {
					getParent().requestDisallowInterceptTouchEvent(true);
					handleFirstTouch();

				} else {
					return false;
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private void handleFirstTouch() {
		mThumbRadius = mThumbMaxRadius;
		lastProces = getProgress();
		mIsdrawBiger = -1;
		isFirstMove = true;
		invalidate();
	}

//	private boolean isTouchDownOnTimeIndicatorThumb(MotionEvent event) {
//
//		Rect textBound = new Rect();
//
//		mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
//				textBound);
//
//		int leftMinPosition = getPaddingLeft() * 2 + textBound.width();
//
//		if (event.getX() < leftMinPosition) {
//
//			setProgress(0);
//			invalidate();
//			return false;
//		}
//
//		textBound = new Rect();
//		mPaint.getTextBounds(mTotalTime, 0, mTotalTime.length(), textBound);
//
//		int rightMinPosition = getMeasuredWidth() - getPaddingRight() * 2
//				- textBound.width();
//
//		if (event.getX() > rightMinPosition) {
//			setProgress(getMax());
//			invalidate();
//			return false;
//		}
//		return true;
//	}

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
		if (mIsWithTime) {
//			drawProcessingTime(canvas);
//			drawToatalTime(canvas);
//			drawThumb(canvas);
			drawThumb(canvas);
		} else {
			if (mIsdrawBiger == -1) {
				drawSmallThumb(canvas);
				drawHalfBigThumb(canvas);
			} else {
				drawThumb(canvas);
			}
		}
	}

//	private void drawProcessingTime(Canvas canvas) {
//
//		Rect textBound = new Rect();
//
//		mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
//				textBound);
//
//		mPaint.setColor(0xbbffffff);
//
//		canvas.drawText(mCurProcessTime, getPaddingLeft(),
//				(getMeasuredHeight() + textBound.height()) / 2, mPaint);
//	}

//	private void drawToatalTime(Canvas canvas) {
//		Rect textBound = new Rect();
//
//		mPaint.getTextBounds(mTotalTime, 0, mTotalTime.length(), textBound);
//
//		mPaint.setColor(0xbbffffff);
//
//		canvas.drawText(mTotalTime, getMeasuredWidth() - getPaddingRight()
//				- textBound.width(),
//				(getMeasuredHeight() + textBound.height()) / 2, mPaint);
//	}

	private void drawHalfBigThumb(Canvas canvas) {
		mPaint.setColor(mThumbColor);

		float left = getThumDrawPosition() - mThumbMaxRadius;
		float top = getMeasuredHeight() / 2 - mThumbMaxRadius;

		RectF rect = new RectF(left, top, left + mThumbMaxRadius * 2, top
				+ mThumbMaxRadius * 2);

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

//		float thumbPos = 0;

//		if (mIsWithTime && !mIsFirstCalc) {
//
//			int timeIndicatorWidth = 0;
//
//			Rect textBound = new Rect();
//
//			mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
//					textBound);
//
//			timeIndicatorWidth += (textBound.width() + getPaddingLeft());
//
//			textBound = new Rect();
//			mPaint.getTextBounds(mTotalTime, 0, mTotalTime.length(), textBound);
//
//			timeIndicatorWidth += (textBound.width() + getPaddingRight());
//
//			mDrawThumbMaxSpan -= timeIndicatorWidth;
//
//			mIsFirstCalc = true;
//
//		}
//		if (mIsWithTime) {
//
//			int leftPos = 0;
//			Rect textBound = new Rect();
//
//			mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
//					textBound);
//
//			leftPos = (textBound.width() + getPaddingLeft() + getPaddingLeft());
//
//			thumbPos = leftPos + (getProgress() * 1.0f / getMax())
//					* mDrawThumbMaxSpan;
//
//			Log.d(TAG, "---mDrawThumbMaxSpan:" + mDrawThumbMaxSpan
//					+ ",thumbPos:" + thumbPos + ",getProgress:" + getProgress());
//
//		} else {
//			thumbPos = 
//		}

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

		int leftPos = getPaddingLeft();

//		// Log.d(TAG, "--getPaddingLeft:" + getPaddingLeft());
//		if (mIsWithTime) {
//			Rect textBound = new Rect();
//
//			mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
//					textBound);
//			leftPos += (textBound.width() + getPaddingLeft());
//		}

		return leftPos;
	}

	private float getIndeterEndDrawPosition() {

		int rightPos = getMeasuredWidth() - getPaddingRight();

		// Log.d(TAG, "--getPaddingRight:" + getPaddingRight());

//		if (mIsWithTime) {
//			Rect textBound = new Rect();
//
//			mPaint.getTextBounds(mTotalTime, 0, mTotalTime.length(), textBound);
//			rightPos -= (textBound.width() + getPaddingRight());
//		}

		return rightPos;
	}

	private float PositionToProgress(float touchDownX) {
		final int width = getWidth();

		int available = width - getPaddingLeft() - getPaddingRight();

		int mCurProcessTimeWidth = 0;

		int mTotalTimeWidth = 0;

//		if (mIsWithTime) {
//
//			Rect textBound = new Rect();
//			mPaint.getTextBounds(mCurProcessTime, 0, mCurProcessTime.length(),
//					textBound);
//
//			mCurProcessTimeWidth += (textBound.width() + getPaddingLeft());
//
//			available -= mCurProcessTimeWidth;
//
//			textBound = new Rect();
//			mPaint.getTextBounds(mTotalTime, 0, mTotalTime.length(), textBound);
//
//			mTotalTimeWidth += (textBound.width() + getPaddingRight());
//
//			available -= mTotalTimeWidth;
//
//		}

		int leftMaxPosition = getPaddingLeft() + mCurProcessTimeWidth;

		int rightMaxPosition = width - getPaddingLeft() - mTotalTimeWidth;

		final int x = (int) touchDownX;
		float scale;
		if (x < leftMaxPosition) {
			scale = 0.0f;
		} else if (x > rightMaxPosition) {
			scale = 1.0f;
		} else {
			scale = (float) (x - leftMaxPosition) / (float) available;
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

//		if (!mIsWithTime) {
			if (Math.abs(lastProces - getProgress()) < (getMax() * 0.05f)
					&& isFirstMove) {
				mIsdrawBiger = -1;
				isFirstMove = false;
			} else {
				mIsdrawBiger = 1;
			}
//		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (!mIsWithTime) {
			mThumbRadius = mThumbMinRadius;
			mIsdrawBiger = 0;
		}
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onProgressChanged(seekBar, getProgress(),
					false);
		}
	}

	public int dp2px(float dp) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dp, getContext().getResources().getDisplayMetrics()) + 0.5f);
	}
}
