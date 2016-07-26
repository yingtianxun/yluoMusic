package com.yluo.yluomusic.ui.widget;

/**
 * by 樱天寻
 */
import com.yluo.yluomusic.utils.MotionEventUtil;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

public abstract class AbstractSlideMenuLayout extends ViewGroup {

	private static final String TAG = "AbstractSlideMenuLayout";

	private static final int INTERCE = 1; // 拦截
	private static final int DONINTERCE = -1; // 不拦截
	private static final int NOTINTERCE = 0; // 还没有判断

	protected float mMenuWidthFactor = 1.0f; // 显示页面的百分比

	private final int mScrollDuration = 300;

	private Scroller mScroller;

	private VelocityTracker velocityTracker;

	private boolean isWaitingCallStatusListener = false;

	private int mInterceptFlag = NOTINTERCE; // 0表示没拦截,-1表示不拦截,1表示拦截

	protected Point windowSize;

	private boolean canOpenLeft = true;

	private boolean canOpenRight = true;

	protected int curMinScrollSpan = 0;

	protected int curMaxScrollSpan = 0;

	protected MotionEventUtil eventUtil;

	public static enum MenuSize {
		LEFTSIZE, RIGHTSIZE, BOTHSIZE
	}

	protected int mMaxScrollSpan = 0; // 右边最大滚动范围

	protected int mContentWidth = 0; // 内容的宽度

	public AbstractSlideMenuLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public AbstractSlideMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AbstractSlideMenuLayout(Context context) {
		super(context);
		init();
	}

	public abstract boolean isMenuClose();

	public abstract boolean isMenuLeftOpen();

	public abstract boolean isMenuRightOpen();

	protected View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	public void setMenuWidthFactor(float menuWidthFactor) {
		this.mMenuWidthFactor = menuWidthFactor;
	}

	protected boolean isAddToViewGroup(View view) {
		if (view == null) {
			return true;
		}
		return view.getParent() != null;

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

	protected void init() {

		eventUtil = new MotionEventUtil(getContext());

		mScroller = new Scroller(getContext());

		velocityTracker = VelocityTracker.obtain();

		windowSize = getWindowSize();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			onScroll(mScroller.getCurrX());
			invalidate();
		}
		if (isWaitingCallStatusListener && mScroller.isFinished()) {

			onScroll(mScroller.getCurrX());
			judgeOpenOrClose();
			isWaitingCallStatusListener = false;
			invalidate();

		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		layOutChildren(changed, left, top, right, bottom);
	}

	public abstract void layOutChildren(boolean changed, int left, int top,
			int right, int bottom);

	private void resetStatus() {
		eventUtil.resetMoveDirection();
		velocityTracker.clear();
		canOpenLeft = true;
		canOpenRight = true;
		mScroller.abortAnimation();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// 没有左右菜单的时候 什么都不拦截
		if (!hasLeftMenu() && !hasRightMenu()) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isForeceIntercept(event)) {
				mInterceptFlag = INTERCE;
			} else {
				mInterceptFlag = NOTINTERCE;
				curMinScrollSpan = 0;
				curMaxScrollSpan = mMaxScrollSpan;
			}
			resetStatus();
			velocityTracker.addMovement(event);
			break;
		case MotionEvent.ACTION_MOVE:

			velocityTracker.addMovement(event);
			if (mInterceptFlag != NOTINTERCE) {
				break;
			}
			eventUtil.calcMoveDirection(event); // 在这里做Y方向的判断拦截
			// 上下移动的话就不做和任何处理
			if (eventUtil.isMoveY()) {
				mInterceptFlag = DONINTERCE;// 不拦截
				break;
			}
			if (isMenuLeftOpen() && eventUtil.isMoveRight(event)) {
				mInterceptFlag = DONINTERCE;// 不拦截
				break;
			}

			if (isMenuRightOpen() && !eventUtil.isMoveRight(event)) {
				mInterceptFlag = DONINTERCE;// 不拦截
				break;
			}

			mInterceptFlag = INTERCE;

			break;
		default:
			break;
		}
		eventUtil.recordEventXY(event);
		return isIntercept();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			handleTouchDownEvent(event);
			break;
		case MotionEvent.ACTION_MOVE:
			handleTouchMoveEvent(event);
			break;
		case MotionEvent.ACTION_UP:
			handleTouchUpEvent(event);
			break;
		case MotionEvent.ACTION_CANCEL:
			mInterceptFlag = NOTINTERCE; // 恢复拦截状态
			break;
		default:
			break;
		}
		eventUtil.recordEventXY(event);
		return true;
	}

	private void forceScrollerStop() {
		if (isWaitingCallStatusListener) {
			mScroller.abortAnimation();
			onScroll(mScroller.getFinalX());
			judgeOpenOrClose();
			isWaitingCallStatusListener = false;
			invalidate();
		}
	}

	private boolean isForeceIntercept(MotionEvent event) {
		forceScrollerStop(); // 每次判断都要强制scroller停止

		if (isMenuLeftOpen() && event.getX() >= getMenuWidth()) {
			curMaxScrollSpan = (int) getMenuWidth();
			return true;
			// 右菜单打开点击右边部分不显示
		} else if (isMenuRightOpen()
				&& event.getX() <= (mContentWidth - getMenuWidth())) {
			curMinScrollSpan = 0;
			return true;
		}
		return false;
	}

	protected float getMenuWidth() {
		return mMenuWidthFactor * mContentWidth;
	}

	private boolean isIntercept() {
		if (mInterceptFlag == INTERCE) {
			return true;
		} else {
			return false;
		}
	}

	protected abstract void onTouchDown(MotionEvent event);

	protected abstract void onTouchMove(MotionEvent event, float disX,
			float disY);

	protected abstract void onScroll(int curXPosition);

	protected abstract void onTouchUp(MotionEvent event, float curVelocitX);

	protected abstract void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec);

	private void handleTouchDownEvent(MotionEvent event) {
		eventUtil.resetMoveStatus();
		eventUtil.resetMoveDirection();
		canOpenLeft = true;
		canOpenRight = true;
		onTouchDown(event);
	}

	private void handleTouchMoveEvent(MotionEvent event) {

		eventUtil.calcMoveDirection(event);
		// 上下移动的话就不做和任何处理

		if (eventUtil.isMoveY()) {
			return;
		}
		// 获取补偿的后的滑动距离
		float disX = eventUtil.getRealDisX(event);

		if (disX == 0) { // 如果为0的话那就跳过了
			return;
		}
		velocityTracker.addMovement(event);

		judgeLeftAndRightCanOpen(disX);

		onTouchMove(event, disX, eventUtil.getDisY(event));
	}

	// 判断是否可以打开菜单,用来禁止跨页滑屏的
	private void judgeLeftAndRightCanOpen(float disX) {
		if (isMenuClose()) {
			if (canOpenLeft && canOpenRight) {
				if ((getScrollX() + disX) < getCloseMenuPosition()) {
					
					canOpenRight = false;
					curMaxScrollSpan = (int) getMenuWidth();
					
				} else if ((getScrollX() + disX) > getCloseMenuPosition()) {
					canOpenLeft = false;
					curMinScrollSpan = 0;
				}
			}
		}
	}

	private boolean closeOrOpenMenuByVelocity(boolean velocityDirection) {

		boolean isClose = false;
		if (isMenuLeftOpen()) {
			isClose = !velocityDirection;
		} else if (isMenuRightOpen()) {
			isClose = velocityDirection;
		} else {
			isClose = velocityDirection ? canOpenRight : canOpenLeft;
		}

		return isClose;
	}

	private void handleTouchUpEvent(MotionEvent event) {

		float curVelocity = eventUtil.getCurXVelocity(velocityTracker);

		if (eventUtil.isMeetMinFlingVelocity(curVelocity)) {
			
			Log.d(TAG, "-------滑动速度达到最大值");
			
			closeOrOpenMenu(closeOrOpenMenuByVelocity(curVelocity > 0),
					curVelocity);

		} else if (getScrollX() != mMaxScrollSpan && getScrollX() != 0) {

			closeOrOpenMenu(!isMeetOpentMenu(), 0);

		} else {
			// 立即关闭的
			judgeOpenOrClose();
		}
		mInterceptFlag = NOTINTERCE; // 恢复拦截状态
		onTouchUp(event, curVelocity);
	}

	protected abstract boolean isMeetOpentMenu();

	protected abstract boolean isMeetOpenLeftMenu();

	protected abstract boolean isMeetOpenRightMenu();

	// 计算移动方向,XY方向的滑动方向的

	//
	/**
	 * @param isClose
	 *            判断菜单是否要关闭
	 * @param curVelocity
	 *            速度方向 1,向右,-1向 左,0没有速度
	 */
	private void closeOrOpenMenu(boolean isClose, float curVelocity) {
		if (isClose) {
			closeMenu();
		} else {
			openMenu(curVelocity);
		}
	}

	protected abstract void openMenu(float curVelocity);

	protected abstract boolean hasLeftMenu();

	protected abstract boolean hasRightMenu();

	protected void setContentWidth(int contentWidth) {
		mContentWidth = contentWidth;
	}

	protected void addScrollSpan(int scrollSpan) {
		mMaxScrollSpan += scrollSpan;

		curMaxScrollSpan = mMaxScrollSpan;
		
		
	}

	protected abstract void judgeOpenOrClose();

	protected int getLeftOpenMenuPosition() {
		
		Log.d(TAG, "---curMinScrollSpan:" + curMinScrollSpan);
		return curMinScrollSpan;
	}

	protected int getRightOpenMenuPosition() {
		
		Log.d(TAG, "---curMaxScrollSpan:" + curMaxScrollSpan);
		
		return curMaxScrollSpan;
	}

	protected int getCloseMenuPosition() {
		if (hasLeftMenu()) {
			return (int) (mMenuWidthFactor * mContentWidth);
		} else {
			return 0;
		}
	}

	public void openLeftMenu() {
		openLeftMenu(true);
	}

	public void openLeftMenu(boolean isScroll) {
		if (isScroll) {
			startScrollX(getScrollX(), getLeftOpenMenuPosition());
		} else {
			judgeOpenOrClose();
		}
	}

	public void openRightMenu() {
		openRightMenu(true);
	}

	public void openRightMenu(boolean isScroll) {
		if (isScroll) {
			// getRightOpenMenuPosition 获取这个值有错
			startScrollX(getScrollX(), getRightOpenMenuPosition());
		} else {
			judgeOpenOrClose();
		}
	}

	private void startScrollX(int startX, int endY) {
		
		Log.d(TAG, "startX:" + startX + ",endY:" + endY);
		
		isWaitingCallStatusListener = true; // 标记监听的
		mScroller.startScroll(startX, 0, endY - startX, 0, mScrollDuration);

		invalidate();
	}

	public void closeMenu() {
		closeMenu(true);
	}

	public void closeMenu(boolean isScroll) {
		if (isScroll) {
			startScrollX(getScrollX(), getCloseMenuPosition());
		} else {
			judgeOpenOrClose();
		}
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

}
