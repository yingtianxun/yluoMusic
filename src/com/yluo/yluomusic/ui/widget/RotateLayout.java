package com.yluo.yluomusic.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

public class RotateLayout extends FrameLayout {
	private static final String TAG = "RotateLineaLayout";

	private int mTouchslop;

	private boolean mIsMoveX = false;
	private boolean mIsMoveY = false;

	private boolean mIsDown = false;

	private boolean mIsMove = false;

	private float mLastX = 0;
	private float mLastY = 0;

	private float mMaxRotateDegree = 45;

	private View mMusicView = null;

	private Scroller mScroller;

	private int mIsWantToOpenOrClose = 0;

	private boolean mDontIntercept = false;

	public RotateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public RotateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RotateLayout(Context context) {
		super(context);
		init();
	}

	public void closeMusicViewByNow() {
		if (mMusicView != null && getRotation() != mMaxRotateDegree) {
			mMusicView.setPivotX(getMeasuredWidth() / 2);

			mMusicView.setPivotY(getMeasuredHeight() * 2);

			mMusicView.setRotation(mMaxRotateDegree);

			invalidate();
		}
	}

	public void opemMusicView() {
		if (mMusicView == null) {
			return;
		}
		mIsWantToOpenOrClose = 1;
		mScroller.startScroll((int) mMusicView.getRotation(), 0,
				(int) (0 - mMusicView.getRotation()), 0, 300);
		invalidate();
	}

	@SuppressLint("NewApi")
	private void init() {
		ViewConfiguration configuration = ViewConfiguration.get(getContext());

		mTouchslop = configuration.getScaledTouchSlop();

		mScroller = new Scroller(getContext());

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (mMusicView != null) {
							mMusicView.setPivotX(getMeasuredWidth() / 2);

							mMusicView.setPivotY(getMeasuredHeight() * 2);
						}
						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});

	}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {

			if (mScroller.isFinished()) {
				if (mIsWantToOpenOrClose == 1) {
					// 打开
					mMusicView.setRotation(0);
				} else {
					mMusicView.setRotation(mMaxRotateDegree);
				}

			} else {
				mMusicView.setRotation(mScroller.getCurrX());
			}
			invalidate();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		
		if (mMusicView == null) {
			recordXY(event);
			return false;
		}
		
		
		if (mMusicView.getRotation() == mMaxRotateDegree) {
			return false;
		}
		
		if (mMusicView.getRotation() != mMaxRotateDegree
				&& mMusicView.getRotation() != 0) {
//			Log.d(TAG, "-----要-----");
			recordXY(event);
			return true;
		}


		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			resetStatus();
			getParent().requestDisallowInterceptTouchEvent(true);
			recordXY(event);
			return false;
		case MotionEvent.ACTION_MOVE:
			
			if (mDontIntercept) {
				recordXY(event);
				return false;
			}
			
			float disX = event.getX() - mLastX;
			float disY = event.getY() - mLastY;
			if (!mIsMove) {
				if (Math.abs(disX) >= mTouchslop) {

				} else if (Math.abs(disY) >= mTouchslop) {
					mIsMoveY = true;
				} else {
					return false;
				}
			}

			if (mIsMoveY) {
//				Log.d(TAG, "----Y方向不拦截----");
				mDontIntercept = true;
				recordXY(event);
				return false;
			}

			if (mMusicView.getRotation() == 0 && disX < 0) {
//				Log.d(TAG, "----向左不拦截----");
				mDontIntercept = true;
				recordXY(event);
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mMusicView.getRotation() == 0
					|| mMusicView.getRotation() == mMaxRotateDegree) {
				recordXY(event);
				return false;
			}
			break;
		default:
		}
		return true;
	}

	public void setPlayMusicView(int Id) {
		setPlayMusicView(View.inflate(getContext(), Id, null));
	}

	public void setPlayMusicView(View view) {
		if (mMusicView == null) {
			mMusicView = view;
			addView(view);
		}
	}

	private void resetStatus() {
		mIsMoveX = false;
		mIsMoveY = false;
		mIsMove = false;
		mDontIntercept = false;
	}

	private void recordXY(MotionEvent event) {
		mLastX = event.getX();
		mLastY = event.getY();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			resetStatus();
			mIsDown = true;
			if (mMusicView == null) {
				return false;
			}
			if (mMusicView.getRotation() == mMaxRotateDegree) {

				return false;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (!mIsDown) {
				resetStatus();
				mIsDown = true;
				break;
			}
			float disX = event.getX() - mLastX;
			float disY = event.getY() - mLastY;

			if (!mIsMove) {
//				Log.d(TAG, "--判断方向-------");
				if (Math.abs(disX) >= mTouchslop) {
//					Log.d(TAG, "---------补偿---------");
//					Toast.makeText(getContext(), "---补偿----", Toast.LENGTH_SHORT).show();
					disX += disX > 0 ? -mTouchslop : mTouchslop;
					mIsMove = true;
					mIsMoveX = true;
				} else if (Math.abs(disY) >= mTouchslop) {
//					Log.d(TAG, "--Y方向不拦截----111---");
					mIsMoveY = true;
					mIsMove = true;
				} else {
					break;
				}
			}
			// 移动的是Y方向,不拦截了
			if (mIsMoveY) {
				Log.d(TAG, "--Y方向不拦截-------");
				break;
			}
			float curRotateDegree = mMusicView.getRotation() + disX
					/ getMeasuredWidth() * mMaxRotateDegree;

			if (curRotateDegree < 0) {
				curRotateDegree = 0;
			} else if (curRotateDegree > mMaxRotateDegree) {
				curRotateDegree = mMaxRotateDegree;
			}
			mMusicView.setRotation(curRotateDegree);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			if (!mIsDown) {
				break;
			}

			if (mMusicView.getRotation() != 0
					&& mMusicView.getRotation() != mMaxRotateDegree) {
				// Log.d(TAG, "---cur:" + mMusicView.getRotation() + ",half:" +
				// mMaxRotateDegree/2);
				if (mMusicView.getRotation() <= mMaxRotateDegree * 0.3) {
					// 开页面
					mIsWantToOpenOrClose = 1;
					mScroller.startScroll((int) mMusicView.getRotation(), 0,
							(int) (0 - mMusicView.getRotation()), 0, 300);
				} else {
					// 关页面
					mIsWantToOpenOrClose = -1;
					mScroller
							.startScroll((int) mMusicView.getRotation(), 0,
									(int) (mMaxRotateDegree - mMusicView
											.getRotation()), 0, 300);
				}
				invalidate();
			}

			break;
		default:
			break;
		}
		recordXY(event);
		return true;
	}

}
