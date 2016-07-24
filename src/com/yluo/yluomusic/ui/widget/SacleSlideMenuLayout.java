package com.yluo.yluomusic.ui.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SacleSlideMenuLayout extends AbstractSlideMenuLayout {

	private static final String TAG = "SlideMenuLayout";

	private float mScaleFactor = 0.8f;

	private View mLeftViewMenu;

	private View mRightViewMenu;

	private View mViewContent;

	private static final int OPEN_LEFT = 1; // 打开左菜单

	private static final int OPEN_RIGHT = -1; // 打开右菜单

	private static final int NOT_OPEN = 0; // 关闭

	private int mMenuOpenStatus = NOT_OPEN;

	private boolean isFinishInflate = false; // 是否已经完成枚举

	private int mSlideLayoutWidth;

	public SacleSlideMenuLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public SacleSlideMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SacleSlideMenuLayout(Context context) {
		super(context);
		init();
	}

	@Override
	protected void init() {
		super.init();
		mMenuWidthFactor = 0.8f;

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

	private void resetWidthAndMenuScrollSpan() {
		mMaxScrollSpan = 0;
		mSlideLayoutWidth = 0;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		resetWidthAndMenuScrollSpan();

		setLeftMenuWidthAndHeight(heightMeasureSpec);

		setRightMenuWidthAndHeight(heightMeasureSpec);

		setContentWidthAndHeight(heightMeasureSpec); // 设置内容高度

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(mSlideLayoutWidth,
				MeasureSpec.EXACTLY);

		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

	}

	private void setContentWidthAndHeight(int heightMeasureSpec) {

		int contentWidth = setViewWidthAndHeight(mViewContent, false,
				heightMeasureSpec);
		mSlideLayoutWidth += contentWidth;
		setContentWidth(contentWidth);

	}

	private void setRightMenuWidthAndHeight(int heightMeasureSpec) {
		int rightMenuWidth = setViewWidthAndHeight(mRightViewMenu, true,
				heightMeasureSpec);

		mSlideLayoutWidth += rightMenuWidth;

		addScrollSpan(rightMenuWidth); // 设置向右滚动的最大距离
	}

	private void setLeftMenuWidthAndHeight(int heightMeasureSpec) {

		int leftMenuWidth = setViewWidthAndHeight(mLeftViewMenu, true,
				heightMeasureSpec);

		mSlideLayoutWidth += leftMenuWidth;

		addScrollSpan(leftMenuWidth); // 设置向左滚动的最大距离
	}

	private int setViewWidthAndHeight(View view, boolean isMenu,
			int heightMeasureSpec) {
		if (view != null) {
			int viewWidth = 0;
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
		return 0;
	}

	@Override
	public void layOutChildren(boolean changed, int left, int top, int right,
			int bottom) {
		layoutLeftMenu();

		layoutContent();

		layoutRighttMenu();

		moveView(getCloseMenuPosition());
	}

	private void layoutLeftMenu() {
		if (mLeftViewMenu != null) {
			mLeftViewMenu.layout(0, 0, mLeftViewMenu.getMeasuredWidth(),
					mLeftViewMenu.getMeasuredHeight());
		}

	}

	private void layoutContent() {

		if (mLeftViewMenu != null) {
			mViewContent.layout(
					mLeftViewMenu.getMeasuredWidth(),
					0,
					mLeftViewMenu.getMeasuredWidth()
							+ mViewContent.getMeasuredWidth(),
					mViewContent.getMeasuredHeight());
		} else {
			mViewContent.layout(0, 0, mViewContent.getMeasuredWidth(),
					mViewContent.getMeasuredHeight());
		}
	}

	private void layoutRighttMenu() {
		if (mRightViewMenu != null) {
			mRightViewMenu
					.layout(mViewContent.getRight(), 0, mViewContent.getRight()
							+ mRightViewMenu.getMeasuredWidth(),
							mRightViewMenu.getMeasuredHeight());
		}
	}

	@Override
	protected void onTouchDown(MotionEvent event) {

	}

	// 调整高度的
	private int adjustMenuPosition(float disX) {
		int scrollToX = (int) (getScrollX() + disX);

		if (scrollToX > getRightOpenMenuPosition()) {
			scrollToX = getRightOpenMenuPosition();

		} else if (scrollToX < getLeftOpenMenuPosition()) {
			scrollToX = getLeftOpenMenuPosition();
		}
		return scrollToX;
	}

	@Override
	protected void onTouchMove(MotionEvent event, float disX, float disY) {
		moveView(adjustMenuPosition(disX));
	}

	@Override
	protected void onScroll(int curXPosition) {
		moveView(curXPosition);
	}

	private void moveView(int scrollToX) {
		scrollTo(scrollToX, 0);

		sacleView(scrollToX);

	}

	private void sacleView(int scrollToX) {
		// 左菜单的范围 0<leftMax
		// 右菜单的 >leftMax
		View scaleView = null;
		boolean isLeft = false;
		if (scrollToX < getCloseMenuPosition()) { // 左菜单
			scaleView = mLeftViewMenu;
			isLeft = true;
		} else if (scrollToX > getCloseMenuPosition()) {
			scaleView = mRightViewMenu;
			isLeft = false;
		} else {
			// 关闭了
			scaleView(mViewContent, 1, isLeft);
			return;
		}

		float menuFactor = mMenuWidthFactor;
		float contentFactor = mMenuWidthFactor;
		if (isLeft) {
			// 基本的缩放因子+现在比例算的
			menuFactor += (1 - (scrollToX * 1.0f / getMenuWidth()))
					* (1 - mScaleFactor);
		} else {
			menuFactor += ((scrollToX - getCloseMenuPosition()) * 1.0f / getMenuWidth())
					* (1 - mScaleFactor);
		}

		contentFactor = (1 - menuFactor) + mMenuWidthFactor;

		scaleView(scaleView, menuFactor, isLeft);

		scaleView(mViewContent, contentFactor, !isLeft);
	}

	private void scaleView(View sacleView, float scaleFactor, boolean isLeft) {

		int viewHeight = sacleView.getMeasuredHeight();
		int viewWidth = sacleView.getMeasuredWidth();
		sacleView.setPivotY(viewHeight / 2);
		if (isLeft) {
			sacleView.setPivotX(viewWidth);
		} else {
			sacleView.setPivotX(0);
		}

		sacleView.setScaleY(scaleFactor);
		sacleView.setScaleX(scaleFactor);
	}

	@Override
	protected void onTouchUp(MotionEvent event, float curVelocitX) {

	}

	@Override
	protected void judgeOpenOrClose() {
		if (getScrollX() == getCloseMenuPosition()) {
			if (!isMenuClose()) {
				mMenuOpenStatus = NOT_OPEN;

				Log.d(TAG, "关闭菜单");
			}
		} else if (getScrollX() == getLeftOpenMenuPosition()) { //
			if (!isMenuLeftOpen()) {
				mMenuOpenStatus = OPEN_LEFT;
				Log.d(TAG, "打开左菜单");

			}
			// 打开的
		} else if (getScrollX() == getRightOpenMenuPosition()) {
			if (!isMenuRightOpen()) {
				mMenuOpenStatus = OPEN_RIGHT;

				Log.d(TAG, "打开右菜单");
			}
		}
	}

	public boolean isMenuClose() {
		return mMenuOpenStatus == NOT_OPEN;
	}

	@Override
	public boolean isMenuLeftOpen() {
		return mMenuOpenStatus == OPEN_LEFT;
	}

	@Override
	public boolean isMenuRightOpen() {
		return mMenuOpenStatus == OPEN_RIGHT;
	}

	@Override
	protected void openMenu(float curVelectoryDirection) {

		if (isMeetOpenLeftMenu() || curVelectoryDirection > 0) {
			openLeftMenu();
		} else if (isMeetOpenRightMenu() || curVelectoryDirection < 0) {
			openRightMenu();
		}
	}

	@Override
	protected boolean hasLeftMenu() {
		return mLeftViewMenu != null;
	}

	@Override
	protected boolean hasRightMenu() {
		return mRightViewMenu != null;
	}

	// 满足打开左菜单的条件
	protected boolean isMeetOpenLeftMenu() {
		if (!hasLeftMenu()) {
			return false;
		}
		return getScrollX() <= mLeftViewMenu.getMeasuredWidth() / 2;
	}

	// 满足打开右菜单的条件
	protected boolean isMeetOpenRightMenu() {

		if (!hasRightMenu()) {
			return false;
		}

		int openWidth = 0;

		if (hasLeftMenu()) {
			openWidth += mLeftViewMenu.getMeasuredWidth();
		}
		openWidth += getMenuWidth() / 2;

		return getScrollX() >= openWidth;
	}

	@Override
	protected boolean isMeetOpentMenu() {

		return isMeetOpenLeftMenu() || isMeetOpenRightMenu();
	}
}
