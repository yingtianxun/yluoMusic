package com.yluo.yluomusic.ui.widget;

import java.util.List;

import com.yluo.yluomusic.utils.MotionEventUtil;
import com.yluo.yluomusic.utils.PaintUtil;


import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ViewPagerIndicator extends LinearLayout{
	
	private static final String TAG = "ViewPagerIndicator";
	private Paint mPaint;
	private int defaultMargin = 6;

	private int mMarginWidth;
	
	private int defaultPadding = 6;
	
	private int mPaddingWidth ;
	
	private MotionEventUtil eventUtil;

	private int mMaxScrollSpan = 0;

	private int mMinScrollSpan = 0;

	private VelocityTracker velocityTracker;

	private Scroller mScroller;
	
	private int mCurSelectItem;
	
	private boolean IgnoreChangeItemByOuter;

	private float mLineHeight = 1.5f; // 线宽

	private float mDrawLineY = 0; // 画线的位置

	private float mCurLineStartX = 0; // 线开始的位置

	private float mCurLineEndX = 0; // 线结束的位置

	private ValueAnimator mDrawLineAnimator;
	private float mDeltaWidth;
	private View mLastItem;
	private View mCurItem;
	private View mNextItem;
	
	private OnSelectItemChangedListener changedListener;
	
	private boolean mIsDrawLineFull = false;
	private int mLastPadding;

	public ViewPagerIndicator(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewPagerIndicator(Context context) {
		super(context);
		init();
	}

	private void init() {

		velocityTracker = VelocityTracker.obtain();

		eventUtil = new MotionEventUtil(getContext());

		defaultMargin = dp2px(defaultMargin);

		mMarginWidth = defaultMargin;
		
		defaultPadding = dp2px(defaultPadding);
		
		mPaddingWidth = defaultPadding;

		mLineHeight = dp2px(mLineHeight);
		
		mPaint = PaintUtil.createPaint(Color.WHITE, (int) mLineHeight);

		mScroller = new Scroller(getContext());

		mCurSelectItem = 0;

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		View curSelectItem = getChildAt(mCurSelectItem);
		mCurLineStartX = curSelectItem.getLeft();
		mCurLineEndX = mCurLineStartX + curSelectItem.getWidth();

	}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);

			invalidate();
		}
		
		

	}
	@Override
	 protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawLine(canvas);
	}
	public void addTagView(List<View> tagViews) {

		addTagView((View[]) tagViews.toArray());
	}

	public void addTagView(View[] tagViews) {
		for (View view : tagViews) {

			addTagView(view);
		}
		// 在这里监听事件
	}

	public void addTagView(View tagView) {

		tagView.setId(getChildCount() - 1);

		addView(tagView);

	}
	
	public void setCheck(int index) {
//		CheckBox checkBox = (CheckBox) getChildAt(index);
		
//		checkBox.setClickable(clickable);
		
//		getChildAt(index).setClickable(true);	
		
		getChildAt(index).setPressed(true);
	}
	

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int actualWidth = MeasureSpec.getSize(widthMeasureSpec);

		int childrenWidth = getChildTotalOriginWidth(heightMeasureSpec);
		
		if(mIsDrawLineFull) {
			actualWidth = adjustChildrenPadding(actualWidth, childrenWidth);
		} else {
			actualWidth = adjustChildrenMargin(actualWidth, childrenWidth);
		}

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(actualWidth,
				MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		initDrawLinePosition();
	}

	private void initDrawLinePosition() {
		mDrawLineY = getMeasuredHeight() - mLineHeight + dp2px(1); // 计算画线的Y位置

		mCurLineEndX = getChildAt(0).getMeasuredWidth();
	}
	
	private int adjustChildrenPadding(int actualWidth, int childrenWidth) {
		int actualNeedWidth = childrenWidth ;
		
		if (actualNeedWidth > actualWidth) {
			mMaxScrollSpan = actualNeedWidth - actualWidth;
			actualWidth = actualNeedWidth;
		} else if ((actualNeedWidth + mLastPadding) < actualWidth) {
			mMaxScrollSpan = 0;
			int remainWidth = actualWidth - childrenWidth;
			mPaddingWidth = remainWidth / getChildCount() / 2;
			mLastPadding = remainWidth - (mPaddingWidth * getChildCount() * 2);
		}
		for (int i = 0; i < getChildCount(); i++) {

			View child = getChildAt(i);

			child.setPadding(mPaddingWidth, child.getPaddingTop()
					, mPaddingWidth, child.getPaddingBottom());
		
		}
		return actualWidth;
	}
	

	private int adjustChildrenMargin(int actualWidth, int childrenWidth) {
		
		
		int actualNeedWidth = childrenWidth + mMarginWidth * 2 * getChildCount();
		
		if (actualNeedWidth > actualWidth) {
			mMaxScrollSpan = actualNeedWidth - actualWidth;
			actualWidth = actualNeedWidth;
		} else if (actualNeedWidth < actualWidth) {
			mMaxScrollSpan = 0;
			
			int remainWidth = actualWidth - childrenWidth;
			
			mMarginWidth = remainWidth/ getChildCount() / 2;
			
		}
		for (int i = 0; i < getChildCount(); i++) {

			View child = getChildAt(i);

			MarginLayoutParams layoutParams = (MarginLayoutParams) child
					.getLayoutParams();

			layoutParams.height = LayoutParams.MATCH_PARENT;
			// 设置左右外边距
			layoutParams.leftMargin = mMarginWidth;
			layoutParams.rightMargin = mMarginWidth;
		}
		return actualWidth;
	}
	
	// 获取原始子控件的大小
	private int getChildTotalOriginWidth(int heightMeasureSpec) {
		

		
		int widthMeasureChildrenSpec = 0;
		int childrenWidth = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			
			MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
			
			if(layoutParams.width > 0) {
				widthMeasureChildrenSpec = MeasureSpec.makeMeasureSpec(
						layoutParams.width, MeasureSpec.EXACTLY);
			} else {
				widthMeasureChildrenSpec = MeasureSpec.makeMeasureSpec(
							(1 << 30) - 1, MeasureSpec.AT_MOST);
			}
			
			
			child.measure(widthMeasureChildrenSpec, heightMeasureSpec);
			
//			Log.d(TAG, "childWidth:" + child.getMeasuredWidth());
			childrenWidth += child.getMeasuredWidth();
		}
		return childrenWidth;
	}
	

	

	public boolean onInterceptTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			eventUtil.resetMoveStatus();
			eventUtil.resetMoveDirection();
			eventUtil.recordEventXY(event);
			velocityTracker.addMovement(event);
			mScroller.abortAnimation();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			eventUtil.resetMoveStatus();
			eventUtil.resetMoveDirection();
			velocityTracker.addMovement(event);
			mScroller.abortAnimation();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			eventUtil.calcMoveDirection(event);
			// 上下移动的话就不做和任何处理
			if (eventUtil.isMoveY()) {
//				Log.d(TAG, "-------3333-----++++");
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}
			if (getScrollX() == mMinScrollSpan && eventUtil.isMoveRight(event)) {
//				Log.d(TAG, "-------1111-----++++");
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}

			if (getScrollX() == mMaxScrollSpan && !eventUtil.isMoveRight(event)) {
				Log.d(TAG, "-------2222-----++++");
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}

			// 获取补偿的后的滑动距离
			float disX = eventUtil.getRealDisX(event);

			if (disX == 0) { // 如果为0的话那就跳过了
				break;
			}
			velocityTracker.addMovement(event);

			int scrollXPosition = judgeScrollPosition(disX);

			scrollTo(scrollXPosition, 0);

			break;
		case MotionEvent.ACTION_UP:
			float curXVelocity = eventUtil.getCurXVelocity(velocityTracker);

			if (eventUtil.isMeetMinFlingVelocity(curXVelocity)) {

				mScroller.fling(getScrollX(), 0, (int) -curXVelocity, 0,
						mMinScrollSpan, mMaxScrollSpan, 0, 0);

				invalidate();
			} else {
				// 点击事件
				handleClickEvent(event);
			}

			break;
		default:
			break;
		}

		eventUtil.recordEventXY(event);
		return true;
	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//	}

	private void drawLine(Canvas canvas) {

		canvas.drawLine(mCurLineStartX, mDrawLineY, mCurLineEndX, mDrawLineY,
				mPaint);
	}

	private void handleClickEvent(MotionEvent event) {
		for (int index = 0; index < getChildCount(); index++) {
			if (eventUtil.isClick(getChildAt(index),
					getScrollX() + event.getX(), event.getY())) {
				if (mCurSelectItem != index) {
					scrollToItem(index,false);
					// 忽略外部的改变
				}
				break;
			}
		}
	}

	private void scrollToItem(int index,boolean isIngoreAnimation) {
		View tagView = getChildAt(index);
		MarginLayoutParams layoutParams = (MarginLayoutParams) tagView
				.getLayoutParams();

		int scrollXPosition = tagView.getLeft() - layoutParams.leftMargin
				- tagView.getHeight();

		scrollXPosition = judgeScrollPosition2(scrollXPosition);

		mScroller.startScroll(getScrollX(), 0, scrollXPosition - getScrollX(),
				0, 300);

		if(!isIngoreAnimation) {
			
			startDrawLineAnimator(mCurSelectItem, index);
			
			mCurSelectItem = index;
			
			IgnoreChangeItemByOuter = true;
			if(changedListener != null) {
				changedListener.onPageChange(mCurSelectItem);
			}
//			this.changedListener
		}
		
		invalidate();
	}

	private void startDrawLineAnimator(int lastItemIndex, int curItemIndex) {

		mLastItem = getChildAt(lastItemIndex);

		mCurItem = getChildAt(curItemIndex);

		mDeltaWidth = mCurItem.getWidth() - mLastItem.getWidth();

		mDrawLineAnimator = ValueAnimator.ofFloat(mLastItem.getLeft(),
				mCurItem.getLeft());

		mDrawLineAnimator.setDuration(300);

		mDrawLineAnimator
				.setInterpolator(new AccelerateDecelerateInterpolator());

		mDrawLineAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {

				mCurLineStartX = (Float) animation.getAnimatedValue();

				mCurLineEndX = (mCurLineStartX + mLastItem.getWidth() + animation
						.getAnimatedFraction() * mDeltaWidth);

				invalidate();
			}
		});

		mDrawLineAnimator.start();

	}

	private int judgeScrollPosition(float disX) {
		float scrollToPosition = getScrollX() + disX;
		if (scrollToPosition < mMinScrollSpan) {
			scrollToPosition = mMinScrollSpan;
		} else if (scrollToPosition > mMaxScrollSpan) {
			scrollToPosition = mMaxScrollSpan;
		}
		return (int) scrollToPosition;
	}

	private int judgeScrollPosition2(float scrollToPosition) {

		if (scrollToPosition < mMinScrollSpan) {
			scrollToPosition = mMinScrollSpan;
		} else if (scrollToPosition > mMaxScrollSpan) {
			scrollToPosition = mMaxScrollSpan;
		}
		return (int) scrollToPosition;
	}

	public int dp2px(float dp) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dp, getContext().getResources().getDisplayMetrics()) + 0.5f);

	}

	public void onPageScrolled(int curItem, float curItemOffset) {
		
//		Log.d(TAG, "curItem:" + curItem + ",curItemOffset:" + curItemOffset + ",mCurSelectItem:" +  mCurSelectItem);
 
		
//		if(curItem == mCurSelectItem && curItemOffset != 0) {
//			// 向右,必须的
//			Log.d(TAG, "向右---curItem + :" + (curItem + 1));
//			Log.d(TAG, "curItem:" + curItem + ",curItemOffset:" + curItemOffset + ",mCurSelectItem:" +  mCurSelectItem);
//			
//			mCurItem = getChildAt(curItem);
//			
//			mCurLineStartX = mCurItem.getLeft() + (mCurItem.getWidth() + 2 * mMarginWidth) * curItemOffset;
//			
//			mNextItem = getChildAt(curItem + 1);
//			
//			mCurLineEndX = mCurLineStartX + mCurItem.getWidth() 
//					+ (mNextItem.getWidth() - mCurItem.getWidth()) * curItemOffset;
//			
//		} else {
//			mCurItem = getChildAt(mCurSelectItem);
//			
//			mNextItem = getChildAt(curItem);
//			
//			if(curItem == mCurSelectItem && curItemOffset == 0) {
//				
//				mCurLineStartX = mCurItem.getLeft() ;
//				
//				mCurLineEndX = mCurLineStartX +  mCurItem.getWidth(); 
//				
//			} else {
//				
//				// 这里是对的
//				mCurLineStartX = mCurItem.getLeft() - (mNextItem.getWidth() + 2 * mMarginWidth) * (1 - curItemOffset);
//				
//				mCurLineEndX = mCurLineStartX + mCurItem.getWidth() +  (mNextItem.getWidth() - mCurItem.getWidth()) * (1 - curItemOffset);
//			}
//		}
		
		
		if(mIsDrawLineFull) {
			mCurItem = getChildAt(mCurSelectItem);
			mCurLineStartX = (int) (curItem * mCurItem.getWidth() + curItemOffset * mCurItem.getWidth());
		} else {
			mCurItem = getChildAt(curItem);
			mCurLineStartX = mCurItem.getLeft() + (mCurItem.getWidth() + 2 * mMarginWidth) * curItemOffset;
		}
		
		
	
		mCurLineEndX = mCurLineStartX + mCurItem.getWidth() ;
		if(!IgnoreChangeItemByOuter) {
			scrollToItem(mCurSelectItem,true);
		}
		
		invalidate();
	}

	public void onPageSelected(int curItem) {
		if(mCurSelectItem == curItem){
			return;
		}
		mCurSelectItem = curItem;
		IgnoreChangeItemByOuter = false;
	}
	
	public void setOnSelectItemChangedListener(OnSelectItemChangedListener listen) {
		this.changedListener = listen;
	}
	public interface OnSelectItemChangedListener{
		void onPageChange(int itemIndex);
	}

}
