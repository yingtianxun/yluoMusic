package com.yluo.yluomusic.ui.widget;

/**
 * by ӣ��Ѱ
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

	private static final int INTERCE = 1; // ����
	private static final int DONINTERCE = -1; // ������
	private static final int NOTINTERCE = 0; // ��û���ж�

	protected float mMenuWidthFactor = 1.0f; // ��ʾҳ��İٷֱ�

	private final int mScrollDuration = 300;

	private Scroller mScroller;

	private VelocityTracker velocityTracker;

	private boolean isWaitingCallStatusListener = false;

	private int mInterceptFlag = NOTINTERCE; // 0��ʾû����,-1��ʾ������,1��ʾ����

	protected Point windowSize;

	private boolean canOpenLeft = true;

	private boolean canOpenRight = true;

	protected int curMinScrollSpan = 0;

	protected int curMaxScrollSpan = 0;

	protected MotionEventUtil eventUtil;

	public static enum MenuSize {
		LEFTSIZE, RIGHTSIZE, BOTHSIZE
	}

	protected int mMaxScrollSpan = 0; // �ұ���������Χ

	protected int mContentWidth = 0; // ���ݵĿ��

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
			super.addView(view); // ���ﵥ����Ӿͺ���,������ȡ
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
		// û�����Ҳ˵���ʱ�� ʲô��������
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
			eventUtil.calcMoveDirection(event); // ��������Y������ж�����
			// �����ƶ��Ļ��Ͳ������κδ���
			if (eventUtil.isMoveY()) {
				mInterceptFlag = DONINTERCE;// ������
				break;
			}
			if (isMenuLeftOpen() && eventUtil.isMoveRight(event)) {
				mInterceptFlag = DONINTERCE;// ������
				break;
			}

			if (isMenuRightOpen() && !eventUtil.isMoveRight(event)) {
				mInterceptFlag = DONINTERCE;// ������
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
			mInterceptFlag = NOTINTERCE; // �ָ�����״̬
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
		forceScrollerStop(); // ÿ���ж϶�Ҫǿ��scrollerֹͣ

		if (isMenuLeftOpen() && event.getX() >= getMenuWidth()) {
			curMaxScrollSpan = (int) getMenuWidth();
			return true;
			// �Ҳ˵��򿪵���ұ߲��ֲ���ʾ
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
		// �����ƶ��Ļ��Ͳ������κδ���

		if (eventUtil.isMoveY()) {
			return;
		}
		// ��ȡ�����ĺ�Ļ�������
		float disX = eventUtil.getRealDisX(event);

		if (disX == 0) { // ���Ϊ0�Ļ��Ǿ�������
			return;
		}
		velocityTracker.addMovement(event);

		judgeLeftAndRightCanOpen(disX);

		onTouchMove(event, disX, eventUtil.getDisY(event));
	}

	// �ж��Ƿ���Դ򿪲˵�,������ֹ��ҳ������
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
			
			Log.d(TAG, "-------�����ٶȴﵽ���ֵ");
			
			closeOrOpenMenu(closeOrOpenMenuByVelocity(curVelocity > 0),
					curVelocity);

		} else if (getScrollX() != mMaxScrollSpan && getScrollX() != 0) {

			closeOrOpenMenu(!isMeetOpentMenu(), 0);

		} else {
			// �����رյ�
			judgeOpenOrClose();
		}
		mInterceptFlag = NOTINTERCE; // �ָ�����״̬
		onTouchUp(event, curVelocity);
	}

	protected abstract boolean isMeetOpentMenu();

	protected abstract boolean isMeetOpenLeftMenu();

	protected abstract boolean isMeetOpenRightMenu();

	// �����ƶ�����,XY����Ļ��������

	//
	/**
	 * @param isClose
	 *            �жϲ˵��Ƿ�Ҫ�ر�
	 * @param curVelocity
	 *            �ٶȷ��� 1,����,-1�� ��,0û���ٶ�
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
			// getRightOpenMenuPosition ��ȡ���ֵ�д�
			startScrollX(getScrollX(), getRightOpenMenuPosition());
		} else {
			judgeOpenOrClose();
		}
	}

	private void startScrollX(int startX, int endY) {
		
		Log.d(TAG, "startX:" + startX + ",endY:" + endY);
		
		isWaitingCallStatusListener = true; // ��Ǽ�����
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
