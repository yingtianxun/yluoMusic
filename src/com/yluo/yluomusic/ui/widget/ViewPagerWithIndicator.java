package com.yluo.ui.widget;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class ViewPagerWithIndicator extends LinearLayout {
	private static final String TAG = "ViewPagerWithIndicator";

	private ViewPager vpPager;

	private ViewpageIndicator indicator;
	
	private boolean ischangViewpageindicator = true;
	
	public ViewPagerWithIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewPagerWithIndicator(Context context) {
		super(context);
		init();
	}

	private void init() {
		addViewPager();
		setOrientation(VERTICAL);
	}

	private void addViewPager() {
		vpPager = new ViewPager(getContext());

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		vpPager.setLayoutParams(layoutParams);
		vpPager.setId(0x1000); // 这里要设置一个id过去
		initListener();
		
		addView(vpPager);

	}

	public void addTage(String[] tagNames) {
		if (indicator == null) {
			indicator = new ViewpageIndicator(getContext());

			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			indicator.setLayoutParams(layoutParams);

			indicator.setBackgroundColor(0xfff8f8f9);

			
			indicator.setTagChangeListener(new TagChangeListener() {

				@Override
				public void tagChanged(int position) {
					vpPager.setCurrentItem(position);
					ischangViewpageindicator = false;
				}
			});
			
			addView(indicator, 0);

		}

		indicator.addTag(tagNames);

	}
	
	private void initListener() {

		vpPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pageIndex) {
				if(indicator != null) {
					indicator.changePageIndex(pageIndex);
				}
				
			}

			@Override
			public void onPageScrolled(int position, float fragment, int arg2) {
				if (ischangViewpageindicator && indicator != null) {
					indicator.changePagePosByViewPage(position, fragment);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
					ischangViewpageindicator = true;
				}
			}
		});
	}

	public void setAdapter(PagerAdapter adapter) {
		vpPager.setAdapter(adapter);
		
	
	}

	public void setOnClickListener(OnClickListener listener) {
		vpPager.setOnClickListener(listener);
	}

	public void setCurrentItem(int item) {
		vpPager.setCurrentItem(item);
	}

	public void setCurrentItem(int item, boolean smoothScroll) {
		vpPager.setCurrentItem(item, smoothScroll);
	}

	public int getCurrentItem() {
		return vpPager.getCurrentItem();
	}

	class ViewpageIndicator extends LinearLayout {
		private static final String TAG = "ViewpageIndicator";
		private ArrayList<TextView> taglist;
		private int totalLen = 0;
		private int screenWidth = 0;
		private int defaltTagWidth = 0;
		private int tagWidth = defaltTagWidth;
		private Paint paint;
		private float drawLinePosY = 0;
		private int lineHeight = 4;
		private int lineOffSet = 0;

		private long DownTime = 0;;
		private float mLastX = 0;
		private float mLastY = 0;

		private int curSelectIndex = 0;
		private Scroller mScroller;

		private float MaxScrollSpan = 0;
		private boolean isMove = false;

		private Scroller mSpanScroller;
		private TagChangeListener tagChangeListener;

		public ViewpageIndicator(Context context, AttributeSet attrs,
				int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			init();
		}

		public ViewpageIndicator(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public ViewpageIndicator(Context context) {
			super(context);
			init();
		}

		private void init() {
			
			defaltTagWidth = dp2px(100);
			tagWidth = defaltTagWidth;
			taglist = new ArrayList<TextView>();
			mScroller = new Scroller(getContext());
			mSpanScroller = new Scroller(getContext());

			
			buildPaint();
			getDrawLinePosY();
			getScreenWidth();

		}

		public void setTagChangeListener(TagChangeListener tagChangeListener) {
			this.tagChangeListener = tagChangeListener;
		}

		@Override
		public void computeScroll() {
			if (mScroller.computeScrollOffset()) {

				lineOffSet = mScroller.getCurrX();
				invalidate();
			}
			if (mSpanScroller.computeScrollOffset()) {

				int scrollPos = mSpanScroller.getCurrX();

				scrollTo(scrollPos, 0);

				invalidate();
			}

		}

		@SuppressLint("NewApi")
		private void getDrawLinePosY() {
			getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {

						@Override
						public void onGlobalLayout() {
							drawLinePosY = getMeasuredHeight() - lineHeight;

							getViewTreeObserver().removeOnGlobalLayoutListener(
									this);
						}
					});

		}

		private void buildPaint() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(4);
			paint.setColor(0xff3ebbb1);
			paint.setStyle(Style.FILL);
		}

		private void getScreenWidth() {
			WindowManager wm = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);

			Point point = new Point();

			wm.getDefaultDisplay().getSize(point);

			screenWidth = point.x;
		}

		private void expandArrayList(int addLen) {
			taglist.ensureCapacity(addLen + taglist.size());
		}

		public void addTag(String[] tagNames) {
			expandArrayList(tagNames.length);

			for (int i = 0; i < tagNames.length; i++) {
				TextView tag = createTag(tagNames[i]);
				addView(tag);
				taglist.add(tag);
				totalLen += defaltTagWidth;
			}

			reSetTagWidth();
			reCalMaxScrollSpan();
		}

		public void changePageIndex(int pageIndex) {

			curSelectIndex = pageIndex;
			moveSpan();
		}

		public void changePagePosByViewPage(int position, float fragment) {
			mScroller.abortAnimation();

			lineOffSet = (int) (position * tagWidth + fragment * tagWidth);
			invalidate();

		}

		private void reCalMaxScrollSpan() {
			// 这里有问题,滚动距离
			MaxScrollSpan = taglist.size() * tagWidth - screenWidth;
			
			if(MaxScrollSpan < 0) {
				MaxScrollSpan = 0;
			}
			
		}

		private void reSetTagWidth() {
			if (totalLen < screenWidth) {
				tagWidth = (int) (screenWidth / getChildCount());
				for (int i = 0; i < taglist.size(); i++) {
					TextView tag = taglist.get(i);
					LinearLayout.LayoutParams layoutParams = (LayoutParams) tag
							.getLayoutParams();
					layoutParams.width = tagWidth;
					tag.setLayoutParams(layoutParams);
				}
			}
			invalidate(); // 重新画一下
		}

		@SuppressLint("ResourceAsColor")
		public TextView createTag(String tagName) {
			TextView tag = new TextView(getContext());

			tag.setText(tagName);
			tag.setTextColor(0xff33363f);
			tag.setTextSize(16);
			tag.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(
					defaltTagWidth, LayoutParams.MATCH_PARENT);
			tag.setLayoutParams(layoutParams);

			return tag;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			float lineStartX = lineOffSet;

			float lineEndS = tagWidth + lineOffSet;

			canvas.drawLine(lineStartX, drawLinePosY, lineEndS, drawLinePosY,
					paint);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				DownTime = System.currentTimeMillis();
				isMove = false;
			}
				break;
			case MotionEvent.ACTION_MOVE: {

				float disX = mLastX - event.getX();
				float disY = mLastY - event.getY();

				float distance = calcMoveDistance(disX, disY);

				if (distance == 0) {
					break;
				}

				isMove = true;
				
				int ScrollToPos = (int) (getScrollX() + disX);
				
				Log.d(TAG, "--ScrollToPos:" 
				+ ScrollToPos + ",disX:" + disX + ",MaxScrollSpan:" + MaxScrollSpan);
				
				if (ScrollToPos < 0) {
					ScrollToPos = 0;
				} else if (ScrollToPos > MaxScrollSpan) {
					ScrollToPos = (int) MaxScrollSpan;	
					
				}
				scrollTo(ScrollToPos, 0);
			}
				break;
			case MotionEvent.ACTION_UP: {
				handleTouchUp(event);
			}
				break;
			default:
				break;
			}

			mLastX = event.getX();
			mLastY = event.getY();
			return true;
		}

		private void handleTouchUp(MotionEvent event) {
			if (mLastX == event.getX() && mLastY == event.getY()
					&& System.currentTimeMillis() - DownTime < 300 && !isMove) {

				int tagIndex = (int) ((event.getX() + getScrollX()) / tagWidth);
				if (curSelectIndex == tagIndex) {
					return;
				}

				moveLine(tagIndex - curSelectIndex);

				curSelectIndex = tagIndex;

				moveSpan();

				if (this.tagChangeListener != null) {
					this.tagChangeListener.tagChanged(curSelectIndex);
				}
			}
		}

		private void moveSpan() {

			int wantToShowMinSpan = curSelectIndex * tagWidth;

			int wantToShowMaxSpan = (curSelectIndex + 1) * tagWidth;

			if (wantToShowMinSpan < getScrollX()) {

				int scorllToPos = (int) (curSelectIndex * tagWidth - 0.65f * screenWidth);

				if (scorllToPos < 0) {
					scorllToPos = 0;
				}

				mSpanScroller.startScroll(getScrollX(), 0, scorllToPos
						- getScrollX(), 0, 300);
				invalidate();

			} else if (wantToShowMaxSpan > getScrollX() + screenWidth) {
				int scorllToPos = (int) (getScrollX() + 0.65f * screenWidth);
				if (scorllToPos > MaxScrollSpan) {
					scorllToPos = (int) MaxScrollSpan;
				}

				mSpanScroller.startScroll(getScrollX(), 0, scorllToPos
						- getScrollX(), 0, 300);
				invalidate();
			}

		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			widthMeasureSpec = MeasureSpec.makeMeasureSpec(
					LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY);

			int height = dp2px(50);

			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

		public int dp2px(float dp) {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					dp, getContext().getResources().getDisplayMetrics());
		}

		private void moveLine(int moveToTagIndex) {
			mScroller.abortAnimation();
			mScroller.startScroll(lineOffSet, 0, moveToTagIndex * tagWidth, 0,
					300);
			invalidate();

		}
	}

	private float calcMoveDistance(float x, float y) {

		float distance = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

		return distance;
	}

	public interface TagChangeListener {
		void tagChanged(int position);
	}
}
