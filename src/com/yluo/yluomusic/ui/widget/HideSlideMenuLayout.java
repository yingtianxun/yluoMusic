package com.yluo.yluomusic.ui.widget;

import com.yluo.yluomusic.utils.MotionEventUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class HideSlideMenuLayout extends ViewGroup {
	private static final String TAG = "HideSlideMenuLayout";

	private boolean isFinishInflate = false;
	private View mLeftViewMenu;
	private View mRightViewMenu;
	private View mViewContent;
	private float mMenuWidthFactor = 1.0f;
	private Point windowSize;

	private static final int LEFT_MENU_OPEN = 1;

	private static final int RIGHT_MENU_OPEN = -1;

	private static final int MENU_CLOSE = 0;

	private int mMenuOpenStatus = MENU_CLOSE;

	private static final int INTERCEP_YES = 1; // 拦截

	private static final int INTERCEP_NO = -1; // 不拦截

	private static final int INTERCEP_NO_DECIDE = 0; // 拦截状态

	private int mInterceptStatus = INTERCEP_NO_DECIDE;

	private int mRightMenuClosePosition = 0;

	private int mRightMenuOpenPosition = 0;

	private int mLeftMenuClosePosition = 0;

	private int mLeftMenuOpenPosition = 0;

	private MotionEventUtil eventUtil;

	private static final int LEFT_MENU = 1;

	private static final int RIGHT_MENU = -1;

	private static final int CONTENT = 0;

	private int mCurMoveView = CONTENT;

	private Scroller mScroller;

	private int mCurRightMenuLayoutLeft = 0;

	private int mCurLeftMenuLayoutLeft = 0;

	private Paint mPaint;

	// private int mShaderWidth = dp2px(10);
	//
	// private LinearGradient mLeftMenuShader;
	//
	// private LinearGradient mRightMenuShader;

	private float mContentScaleFactor = 0.95f;

	private boolean notMove =  true;

	public HideSlideMenuLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public HideSlideMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HideSlideMenuLayout(Context context) {
		super(context);
		init();
	}

	protected void init() {
		windowSize = getWindowSize();

		eventUtil = new MotionEventUtil(getContext());

		mScroller = new Scroller(getContext());

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(1);
		mPaint.setAlpha(100);
		mPaint.setStyle(Style.FILL);
	}

	private int dp2px(float dp) {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		return (int) (outMetrics.density * dp + 0.5f);

	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {

			View view = getCurMoveView();
			if (view != null) {

				// if(mScroller.isFinished()) {
				// mCurMoveView = CONTENT;
				// }
				view.setTranslationX(mScroller.getCurrX());

				invalidate();

				float mContentScaleFactor = getContentScaleFactor();

				mViewContent.setScaleX(mContentScaleFactor);
				mViewContent.setScaleY(mContentScaleFactor);
			}

		}

	}

	public void setLeftMenuView(View leftMenuView) {
		mLeftViewMenu = leftMenuView;
		if (isFinishInflate) {
			addChild(mLeftViewMenu);
		}
	}

	public void setLeftMenuView(int id) {
		setLeftMenuView(inflate(id));
	}

	public void setRightMenuView(View leftMenuView) {
		mRightViewMenu = leftMenuView;
		if (isFinishInflate) {
			addChild(mRightViewMenu);
		}
	}

	public void setRightMenuView(int id) {
		setRightMenuView(inflate(id));
	}

	public void openRightMenu() {
		if (isMenuClose()) {
			float tranX = mRightViewMenu.getTranslationX();

			mScroller.startScroll((int) (tranX - 0.5f), 0,
					(int) (mRightMenuOpenPosition - tranX - 0.5f), 0, 300);

			mMenuOpenStatus = RIGHT_MENU_OPEN;
			mCurMoveView = RIGHT_MENU;
			invalidate();
		}
	}

	public void openLeftMenu() {
		if (isMenuClose()) {

			float tranX = mLeftViewMenu.getTranslationX();

			mScroller.startScroll((int) (tranX + 0.5f), 0,
					(int) (mLeftMenuOpenPosition - tranX + 0.5f), 0, 300);

			mMenuOpenStatus = LEFT_MENU_OPEN;
			mCurMoveView = LEFT_MENU;
			invalidate();

		}
	}

	protected View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	protected void addChild(View view) {
		addChild(view, true);
	}

	protected void addChild(View view, boolean isForce) {
		if (view == null) {
			return;
		}
		if (!isAddToViewGroup(view)) {
			super.addView(view); // 这里单纯添加就好了,触发获取
			if (isForce) {
				forceLayout();
			} else {
				requestLayout();
			}
		}
	}

	protected boolean isAddToViewGroup(View view) {
		if (view == null) {
			return true;
		}
		return view.getParent() != null;

	}

	@Override
	protected void onFinishInflate() {

		if (!isFinishInflate) {
			if (getChildCount() != 1) {
				throw new IllegalArgumentException("SlideMenuLayout的内容只能有一个");
			}
			mViewContent = getChildAt(0);

			addChild(mLeftViewMenu, true);

			addChild(mRightViewMenu, true);

			isFinishInflate = true;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		setLeftMenuWidthAndHeight(heightMeasureSpec);

		setContentWidthAndHeight(heightMeasureSpec); // 设置内容高度

		setRightMenuWidthAndHeight(heightMeasureSpec);

		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutLeftMenu();

		layoutContent();

		layoutRighttMenu();
	}

	private void layoutRighttMenu() {

		if (mRightViewMenu != null) {
			mRightViewMenu
					.layout(mCurRightMenuLayoutLeft, 0, mCurRightMenuLayoutLeft
							+ mRightViewMenu.getMeasuredWidth(),
							mRightViewMenu.getMeasuredHeight());
		}

	}

	private void layoutContent() {
		mViewContent.layout(0, 0, mViewContent.getMeasuredWidth(),
				mViewContent.getMeasuredHeight());
	}

	private void layoutLeftMenu() {

		if (mLeftViewMenu != null) {
			mLeftViewMenu.layout(mCurLeftMenuLayoutLeft, 0,
					mCurLeftMenuLayoutLeft + mLeftViewMenu.getMeasuredWidth(),
					mLeftViewMenu.getMeasuredHeight());
		}
	}

	private void setContentWidthAndHeight(int heightMeasureSpec) {
		setViewWidthAndHeight(mViewContent, false, heightMeasureSpec);
	}

	private void setLeftMenuWidthAndHeight(int heightMeasureSpec) {

		if (mLeftViewMenu == null) {
			return;
		}

		int viewWidth = setViewWidthAndHeight(mLeftViewMenu, true,
				heightMeasureSpec);

		mLeftMenuOpenPosition = viewWidth;

		mLeftMenuClosePosition = 0;

		mCurLeftMenuLayoutLeft = (int) (isLeftMenuOpen() ? 0 : -viewWidth);

	}

	private void setRightMenuWidthAndHeight(int heightMeasureSpec) {

		if (mRightViewMenu == null) {
			return;
		}

		int viewWidth = setViewWidthAndHeight(mRightViewMenu, true,
				heightMeasureSpec);

		mRightMenuOpenPosition = -viewWidth; // 打开的位置

		mRightMenuClosePosition = 0; // 关闭的位置

		mCurRightMenuLayoutLeft = (int) (isRightMenuOpen() ? mViewContent
				.getMeasuredWidth() - viewWidth : viewWidth);

	}

	private int setViewWidthAndHeight(View view, boolean isMenu,
			int heightMeasureSpec) {
		int viewWidth = 0;
		if (view != null) {

			if (isMenu) {
				viewWidth = (int) (windowSize.x * mMenuWidthFactor);
			} else {
				viewWidth = windowSize.x;
			}

			int widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWidth,
					MeasureSpec.EXACTLY);

			view.measure(widthMeasureSpec, heightMeasureSpec);

			return viewWidth;
		}

		return viewWidth;
	}

	private boolean isLeftMenuOpen() {
		if (mLeftViewMenu == null) {
			return false;
		}

		return mMenuOpenStatus == LEFT_MENU_OPEN;
	}

	private boolean isRightMenuOpen() {
		if (mRightViewMenu == null) {
			return false;
		}
		return mMenuOpenStatus == RIGHT_MENU_OPEN;
	}

	private boolean isMenuClose() {
		return mMenuOpenStatus == MENU_CLOSE;
	}

	protected Point getWindowSize() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		Point outSize = new Point();

		wm.getDefaultDisplay().getSize(outSize);

		return outSize;
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}
	
	
	public void setNotMove(boolean notMove) {
		this.notMove = notMove;
	}
	
	public boolean onInterceptTouchEvent(MotionEvent event) {
//		notMove = true;
		if(isMenuClose() && notMove) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!isMenuClose()) {
				Log.d(TAG, "----按下设置拦截---");
				mInterceptStatus = INTERCEP_YES;
				if (isLeftMenuOpen()) {
					mCurMoveView = LEFT_MENU;
				} else {
					mCurMoveView = RIGHT_MENU;
				}
			} else {
				Log.d(TAG, "----按下设置不确定----");
				mInterceptStatus = INTERCEP_NO_DECIDE;
				mCurMoveView = CONTENT;
			}
//			eventUtil.resetMoveStatus();
//			eventUtil.resetMoveDirection();
			eventUtil.resetMoveDirection();
			eventUtil.resetMoveStatus();
			//

			break;

		case MotionEvent.ACTION_MOVE:

			if (mInterceptStatus != INTERCEP_NO_DECIDE) {
				break;
			}
			eventUtil.calcMoveDirection(event); // 在这里做Y方向的判断拦截
			// 上下移动的话就不做和任何处理
			if (eventUtil.isMoveY()) {
				Log.d(TAG, "--------ooooooooo1-------");
				mInterceptStatus = INTERCEP_NO;// 不拦截
				break;
			}
			if (isLeftMenuOpen() && eventUtil.isMoveRight(event)) {
				Log.d(TAG, "----111不拦截");
				mInterceptStatus = INTERCEP_NO;// 不拦截
				break;
			}

			if (isRightMenuOpen() && eventUtil.isMoveLeft(event)) {
				Log.d(TAG, "----222不拦截");
				mInterceptStatus = INTERCEP_NO;// 不拦截
				break;
			}

			mInterceptStatus = INTERCEP_YES;

			break;
		default:
			break;
		}
		eventUtil.recordEventXY(event);
		return isIntercept();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if(isMenuClose() && notMove) {
			return false;
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			
//			
//			
//			eventUtil.resetMoveStatus();
//			eventUtil.resetMoveDirection();
			break;
		case MotionEvent.ACTION_MOVE:
			handleTouchMoveEvent(event);
			break;
		case MotionEvent.ACTION_UP:

			handleTouchUpEvent();

			break;
		default:
			break;
		}
		eventUtil.recordEventXY(event);
		return true;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawMask(canvas);

	}

	private void drawMask(Canvas canvas) {
		if (mCurMoveView != CONTENT) {
			float left = 0;
			float right = 0;
			float alphaFactor;
			if (mCurMoveView == LEFT_MENU) {
				left = mLeftViewMenu.getTranslationX();
				if (left < 0) {
					left = 0;
				}
				right = getMeasuredWidth();
				alphaFactor = (right - left)
						/ (mLeftMenuOpenPosition - mLeftMenuClosePosition);
			} else {
				left = 0;
				right = getMeasuredWidth() + mRightViewMenu.getTranslationX();
				alphaFactor = (right - left)
						/ (mRightMenuClosePosition - mRightMenuOpenPosition);
			}

			mPaint.setAlpha((int) (100 * (1 - alphaFactor)));
			canvas.drawRect(left, 0, right, getMeasuredHeight(), mPaint);
		}
	}

	private float getContentScaleFactor() {

		if (mCurMoveView != CONTENT) {

			float alphaFactor = 1;

			if (mCurMoveView == LEFT_MENU) {
				float left = mLeftViewMenu.getTranslationX();
				if (left < 0) {
					left = 0;
				}
				float right = getMeasuredWidth();
				alphaFactor = (right - left)
						/ (mLeftMenuOpenPosition - mLeftMenuClosePosition);
			} else {
				float left = 0;
				float right = getMeasuredWidth()
						+ mRightViewMenu.getTranslationX();
				alphaFactor = (right - left)
						/ (mRightMenuClosePosition - mRightMenuOpenPosition);
			}
			return 1 - (1 - mContentScaleFactor) * (1 - alphaFactor);
		}
		return 1;
	}

	private void handleTouchUpEvent() {
		if (mCurMoveView == RIGHT_MENU) {
			float tranX = mRightViewMenu.getTranslationX();

			if (tranX != mRightMenuClosePosition
					&& tranX != mRightMenuOpenPosition) {
				if (tranX <= mRightMenuOpenPosition / 2) {
					// 开启
					mScroller.startScroll((int) (tranX - 0.5f), 0,
							(int) (mRightMenuOpenPosition - tranX - 0.5f), 0,
							300);

					mMenuOpenStatus = RIGHT_MENU_OPEN;

				} else {
					mScroller.startScroll((int) (tranX + 0.5f), 0,
							(int) (mRightMenuClosePosition - tranX + 0.5f), 0,
							300);

					mMenuOpenStatus = MENU_CLOSE;
				}
				invalidate();
			} else if (tranX == mRightMenuClosePosition) {
				mMenuOpenStatus = MENU_CLOSE;
			} else if (tranX == mRightMenuOpenPosition) {
				mMenuOpenStatus = RIGHT_MENU_OPEN;
			}

		} else if (mCurMoveView == LEFT_MENU) {
			Log.d(TAG, "11111mCurMoveView:" + mCurMoveView);
			float tranX = mLeftViewMenu.getTranslationX();

			if (tranX != mLeftMenuClosePosition
					&& tranX != mLeftMenuOpenPosition) {
				Log.d(TAG, "kkkkkk:" + mCurMoveView);
				if (tranX >= mLeftMenuOpenPosition / 2) {
					// 开启
					mScroller.startScroll((int) (tranX + 0.5f), 0,
							(int) (mLeftMenuOpenPosition - tranX + 0.5f), 0,
							300);

					mMenuOpenStatus = LEFT_MENU_OPEN;

				} else {
					mScroller.startScroll((int) (tranX - 0.5f), 0,
							(int) (mLeftMenuClosePosition - tranX - 0.5f), 0,
							300);

					mMenuOpenStatus = MENU_CLOSE;
				}
				invalidate();
			} else if (tranX == mLeftMenuClosePosition) {
				mMenuOpenStatus = MENU_CLOSE;
			} else if (tranX == mLeftMenuOpenPosition) {
				mMenuOpenStatus = LEFT_MENU_OPEN;
			}
		} else {
			mMenuOpenStatus = MENU_CLOSE;
		}
	}

	private View getCurMoveView() {
		if (mCurMoveView == RIGHT_MENU) {

			return mRightViewMenu;

		} else if (mCurMoveView == LEFT_MENU) {
			return mLeftViewMenu;
		}

		return null;
	}

	private void handleTouchMoveEvent(MotionEvent event) {

		eventUtil.calcMoveDirection(event);
		// 上下移动的话就不做和任何处理

		if (eventUtil.isMoveY()) {
			Log.d(TAG, "--------1111-------");
			return;
		}
		// 获取补偿的后的滑动距离
		float disX = eventUtil.getRealDisX(event);
		if (disX == 0) { // 如果为0的话那就跳过了
			Log.d(TAG, "--------2222-------");
			return;
		}
		if (isMenuClose()) {
			// 菜单是关闭的
			if (mCurMoveView == CONTENT) {
				if (eventUtil.isMoveLeft(event)) {
					mCurMoveView = RIGHT_MENU; // 右菜单
					bringChildToFront(mRightViewMenu);
				} else {
					mCurMoveView = LEFT_MENU; // 右菜单
					bringChildToFront(mLeftViewMenu);
				}
			}
			if (mCurMoveView == RIGHT_MENU) {
				// 关闭的时候拖动的是右边的菜单
				Log.d(TAG, "--关闭的时候拖动的是右边的菜单--");
				float tranX = adjustRightMenuItem(mRightViewMenu
						.getTranslationX() - disX);

				mRightViewMenu.setTranslationX(tranX);
			} else {
				// 关闭的时候 拖动的是左边的菜单
				Log.d(TAG, "--关闭的时候 拖动的是左边的菜单--");
				float tranX = mLeftViewMenu.getTranslationX() - disX;

				mLeftViewMenu.setTranslationX(adjustLeftMenuItem(tranX));
			}
			invalidate();
		} else if ((isRightMenuOpen() && eventUtil.isMoveRight(event))
				|| mCurMoveView == RIGHT_MENU) {
			mCurMoveView = RIGHT_MENU;
			float tranX = adjustRightMenuItem(mRightViewMenu.getTranslationX()
					- disX);

			mRightViewMenu.setTranslationX(tranX);

			invalidate();

		} else if ((isLeftMenuOpen() && eventUtil.isMoveLeft(event))
				|| mCurMoveView == LEFT_MENU) {
			Log.d(TAG, "--左边菜单打开的拖动--");
			mCurMoveView = LEFT_MENU;

			float tranX = mLeftViewMenu.getTranslationX() - disX;

			mLeftViewMenu.setTranslationX(adjustLeftMenuItem(tranX));

			invalidate();
		}

		float mContentScaleFactor = getContentScaleFactor();

		mViewContent.setScaleX(mContentScaleFactor);
		mViewContent.setScaleY(mContentScaleFactor);
	}

	private float adjustLeftMenuItem(float tranX) {

		if (tranX > mLeftMenuOpenPosition) {

			tranX = mLeftMenuOpenPosition;

		} else if (tranX < mLeftMenuClosePosition) {

			tranX = mLeftMenuClosePosition;
		}
		return tranX;
	}

	private float adjustRightMenuItem(float tranX) {
		if (tranX < mRightMenuOpenPosition) {

			tranX = mRightMenuOpenPosition;

		} else if (tranX > mRightMenuClosePosition) {

			tranX = mRightMenuClosePosition;
		}
		return tranX;
	}

	private boolean isIntercept() {
		if (mInterceptStatus == INTERCEP_YES) {
			return true;
		} else {
			return false;
		}
	}

}
