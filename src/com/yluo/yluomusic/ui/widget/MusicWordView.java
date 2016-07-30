package com.yluo.yluomusic.ui.widget;

import java.util.ArrayList;
import java.util.List;

import com.yluo.yluomusic.aidl.WordLine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.OverScroller;

public class MusicWordView extends View {

	private static final String TAG = "MusicWordView";

	private List<WordLine> wordLines;

	private Paint paint;

	private Rect mTextRect;
	private Rect mTimeIndicateRect;

	private Bitmap mBmp;

	private int mCurSongingLine = 0;

	private float mScorllDisY = 0;

	private float mLineHeight; // �и�

	private float mLinePadding;

	private int mTopLinePosition; // �ڶ�����λ��

	private float mLastY; // ��¼��һ�δ����¼���x,y����
	private float mLastX;
	private int mTouchSlop;

	private int mOldColor;

	private int songWordColor = 0xFFD8EB92;

	private Style mOldStyle;

	private float mStrokeWidth;

	private Path mTrianglePath;

	private float mPlayBtnRadius; // ���Ƶķ�Χ

	private float mPlayBtnTouchRadius; // �����жϵķ�Χ

	private float mOldTextSize;

	private OverScroller mScroller;

	private VelocityTracker mVelocityTracker;

	private float mMinFlingVelocity;

	private float mMaxFlingVelocity;

	private float mMinScrollPosition = 0;
	private float mMaxScrollPosition = 0;

	private float mCurLineProgress = 0;

	private float mTimeIndicatorTextSize;

	private float mSongWordTextSize; // �����С

	private int mSongDuration = 0;

	private int mPaintLineWidth; // �и�

	private float mPrecent;

	private boolean mIsMove;

	private int mCurSelecLine;
	private boolean mIsMoveX = false;
	private boolean mIsMoveY = false;

	private static final String NO_WORDS = "���޸��...";
	private Rect mNoWordRect;

	private boolean mIsDrawBtn = false;

	private Runnable mCancleDrawBtnRunable = new Runnable() {

		@Override
		public void run() {
			mIsDrawBtn = false;
			invalidate();
		}
	};

	public MusicWordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MusicWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MusicWordView(Context context) {
		super(context);
		init();
	}

	private void init() {
		initConfig();

		initPaint();

		getFontRect();

		getMeasureWidthAndHeight();

		mScroller = new OverScroller(getContext());

		mVelocityTracker = VelocityTracker.obtain();
	}

	private void initConfig() {

		setDrawingCacheEnabled(true);

		mLinePadding = dp2px(15);
		mTimeIndicatorTextSize = dp2px(15);

		mSongWordTextSize = dp2px(18);

		mPaintLineWidth = dp2px(2.5f); //

		mPlayBtnRadius = dp2px(10);

		mPlayBtnTouchRadius = dp2px(15);

		ViewConfiguration viewConfiguration = ViewConfiguration
				.get(getContext());

		mTouchSlop = viewConfiguration.getScaledTouchSlop();

		mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

		mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
	}

	private int dp2px(float dp) {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		return (int) (outMetrics.density * dp + 0.5f);

	}

	private void initPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(mPaintLineWidth);
		paint.setStyle(Style.FILL);
		paint.setTextSize(mSongWordTextSize);
		paint.setColor(Color.WHITE);
	}

	private void getFontRect() {
		mTextRect = new Rect();
		paint.setTextSize(mSongWordTextSize);
		paint.getTextBounds("1", 0, 1, mTextRect);

		mLineHeight = mTextRect.height() + mLinePadding; // ��ȡ�и�

		paint.setTextSize(mTimeIndicatorTextSize);
		mTimeIndicateRect = new Rect();
		paint.getTextBounds("1", 0, 1, mTimeIndicateRect);

		paint.setTextSize(mSongWordTextSize);

		mNoWordRect = new Rect();

		paint.getTextBounds(NO_WORDS, 0, NO_WORDS.length(), mNoWordRect);

	}

	@SuppressLint("NewApi")
	private void getMeasureWidthAndHeight() {
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						// ��������
						createWordMask();
						calcWordLinesDrawXY();
						// ���������ε�·��
						createTrianglePath();
						// ����������
						// ������������Χ
						calcScrollScope();
						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});
	}

	private void calcScrollScope() {
		if(wordLines == null || wordLines.size() == 0) {
			return;
		}
		
		mMaxScrollPosition = wordLines.size() * mLineHeight
				- getMeasuredHeight() / 2 - mLineHeight / 2;
		mMinScrollPosition = -getMeasuredHeight() / 2 + mLineHeight / 2;
		srcollToLine(0);
	}

	private void createTrianglePath() {
		mTrianglePath = new Path();

		float topPointX = getMeasuredWidth() - mPlayBtnRadius - mPlayBtnRadius
				/ 2 - 2 - getPaddingRight();
		float topPointY = getMeasuredHeight() / 2 - mPlayBtnRadius / 2;

		float bottomPointX = topPointX;
		float bottomPointY = topPointY + mPlayBtnRadius;

		float rightPointX = topPointX + mPlayBtnRadius;

		float rightPointY = getMeasuredHeight() / 2;

		mTrianglePath.moveTo(topPointX, topPointY);
		mTrianglePath.lineTo(bottomPointX, bottomPointY);

		mTrianglePath.lineTo(rightPointX, rightPointY);
	}

	private void createWordMask() {

		if (getMeasuredWidth() == 0) {
			return;
		}

		mBmp = Bitmap.createBitmap(getMeasuredWidth(), (int) mLineHeight,
				Bitmap.Config.ARGB_8888);

		Canvas maskCanvas = new Canvas(mBmp);

		maskCanvas.drawColor(songWordColor);
	}

	private void calcWordLinesDrawXY() {
		if(wordLines == null || wordLines.size() == 0) {
			return;
		}
		for (int i = 0; i < wordLines.size(); i++) {
			
			calcDrawX(wordLines.get(i));
			
			calcDrawY(wordLines.get(i),i);
		}
	}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mScorllDisY = mScroller.getCurrY();
			invalidate();
		}
	}

	// �������δ����¼����ƶ�����
	private float calcMoveDistance(MotionEvent event) {

		float disX = event.getX() - mLastX;
		float disY = event.getY() - mLastY;

		return (float) Math.sqrt(disX * disX + disY * disY);
	}

	private boolean isClickPlayBtn(MotionEvent event) {
		float moveDistance = calcMoveDistance(event);

		if (moveDistance != 0) {
			return false;
		}

		float disX = getPlayBtnCenterX() - event.getX();
		float disY = getPlayBtnCenterY() - event.getY();
		float distance = (float) Math.sqrt(disX * disX + disY * disY);
		if (distance > mPlayBtnTouchRadius) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			mVelocityTracker.clear();
			mVelocityTracker.addMovement(event);
			getParent().requestDisallowInterceptTouchEvent(true);
			mIsMove = false;
			mIsMoveX = false;
		}
			break;
		case MotionEvent.ACTION_MOVE: {

			mVelocityTracker.addMovement(event);

			float disY = event.getY() - mLastY;

			float disX = event.getX() - mLastX;
			// ������С������
			if (!mIsMove) {
				if (Math.abs(disY) >= mTouchSlop) {
					if (disY > 0) {
						disY -= mTouchSlop;
					} else {
						disY += mTouchSlop;
					}
					mIsMove = true;
				} else if (Math.abs(disX) >= mTouchSlop) {
					mIsMoveX = true;
					mIsMove = true;
				} else {
					break;
				}
			}
			if (mIsMoveX) {
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			mScorllDisY -= disY;

			removeCallbacks(mCancleDrawBtnRunable);
			mIsDrawBtn = true; // ����ť����
			invalidate();
		}
			break;
		case MotionEvent.ACTION_UP: {
			// �ж��Ƿ���Բ����
			if (isClickPlayBtn(event)) {

				Log.d(TAG, "���Բ����--------");
			} else {
				// �����¼�
				mVelocityTracker
						.computeCurrentVelocity(1000, mMaxFlingVelocity);

				float curYVelocity = mVelocityTracker.getYVelocity();

				if (Math.abs(curYVelocity) < mMinFlingVelocity) {
					scrollViewBack(); // ������Χ��������
				} else {
					filingView(curYVelocity); // ������
				}
			}

			postDelayed(mCancleDrawBtnRunable, 2000);
		}
			break;
		default:
			break;
		}
		mLastX = event.getX();
		mLastY = event.getY();
		return true;
	}

	public void srcollToLine(int lineIndex) {

		if (wordLines == null || wordLines.size() == 0) {
			return;
		}
		WordLine wordLine = wordLines.get(lineIndex);
		if (wordLine.getWords().length() == 0) {
			return;
		}

		// �ж��Ƿ񳬳�����
		if (lineIndex < 0 || lineIndex >= wordLines.size()) {
			return;
		}
		int disY = (int) ((-(getMeasuredHeight() / 2 - lineIndex * mLineHeight - mLineHeight / 2)) - mScorllDisY);

		mCurSongingLine = lineIndex;

		mCurSelecLine = mCurSongingLine;

		mCurLineProgress = (wordLines.get(mCurSongingLine).getWidth() + mTextRect
				.width()) * mPrecent;

		mScroller.startScroll(0, (int) mScorllDisY, 0, disY, 300);

		invalidate();
	}

	public void nextLine() {
		srcollToLine(mCurSongingLine + 1);
	}

	public void preLine() {
		srcollToLine(mCurSongingLine - 1);
	}

	private void filingView(float curYVelocity) {
		mScroller.fling(0, (int) mScorllDisY, 0, -(int) curYVelocity, 0, 0,
				(int) mMinScrollPosition, (int) mMaxScrollPosition, 0,
				dp2px(75));
		invalidate();
	}

	private void scrollViewBack() {
		if (mScorllDisY > mMaxScrollPosition) {
			mScroller.startScroll(0, (int) mScorllDisY, 0,
					(int) (mMaxScrollPosition - mScorllDisY), 500);
		} else if (mScorllDisY < mMinScrollPosition) {
			mScroller.startScroll(0, (int) mScorllDisY, 0,
					(int) (mMinScrollPosition - mScorllDisY), 500);
		}
		invalidate();
	}



	// ���㶥��Ӧ���ڵ�λ��
	private void getTopLinePosition() {
		// ���㵱ǰ��λ��
		mTopLinePosition = (int) (mScorllDisY / mLineHeight);

		if (mTopLinePosition < 0) {
			mTopLinePosition = 0;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
	
		if (wordLines == null || wordLines.size() == 0) {
			drawEmptyWord(canvas); // ��ʾû���
		} else {
			if (mIsDrawBtn) {
				drawIndicateLine(canvas);
				drawPlayBtn(canvas);
				drawCurLineTime(canvas);
			}
			drawWordLines(canvas);

		}
	}

	private void drawEmptyWord(Canvas canvas) {

		int drawX = (getAvailableWidth() - mNoWordRect.width()) / 2;
		int drawY = (getAvailableHeight() + mNoWordRect.height()) / 2;
		canvas.drawText(NO_WORDS, drawX, drawY, paint);
	}

	/**
	 * ��ȡ��ȥpadding֮���ֵ
	 * 
	 * @return
	 */
	private int getAvailableWidth() {
		return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
	}

	/**
	 * @return
	 */
	private int getAvailableHeight() {
		return getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
	}

	private void drawIndicateLine(Canvas canvas) {
		// ������
		paint.setColor(0x44ffffff);
		paint.setStrokeWidth(1);
		paint.setAlpha(80);
		canvas.drawLine(mTimeIndicateRect.width() * 10 + getPaddingLeft(),
				getMeasuredHeight() / 2 - 1, getMeasuredWidth() / 2
						- mPlayBtnRadius * 3 - getPaddingRight(),
				getMeasuredHeight() / 2 - 1, paint);

		// ������

		canvas.drawLine(getMeasuredWidth() / 2 + mPlayBtnRadius * 3
				- getPaddingRight(), getMeasuredHeight() / 2 - 1,
				getMeasuredWidth() - mPlayBtnRadius * 3 - getPaddingRight(),
				getMeasuredHeight() / 2 - 1, paint);

		paint.setAlpha(255);
	}

	private void drawWordLines(Canvas canvas) {
		getTopLinePosition(); // ���㶥��λ��,�ӵڼ��п�ʼ��

		float curShowHeight = 0;
		for (int i = mTopLinePosition; i < wordLines.size(); i++) {

			curShowHeight = i * mLineHeight - mScorllDisY;

			WordLine wordLine = wordLines.get(i);
			changeLineByScroll((int) curShowHeight, i); // ������Ź�����ɫ��

			if (i == mCurSongingLine) {
				drawMaskWordLine(canvas, wordLine);
			} else {
				drawNormalWordLine(canvas, wordLine, i);
			}
			// ����жϵľͲ�Ҫ�������
			if (curShowHeight > getMeasuredHeight()) {
				break;
			}
		}
	}

	private void changeLineByScroll(int curShowHeight, int lineIndex) {
		if (curShowHeight <= getMeasuredHeight() / 2
				&& curShowHeight + mLineHeight >= getMeasuredHeight() / 2) {

			mCurSelecLine = lineIndex;

		} else if (mScorllDisY <= mMinScrollPosition) {
			mCurSelecLine = 0;
		} else if (mScorllDisY >= mMaxScrollPosition) {
			mCurSelecLine = wordLines.size() - 1;
		}
	}

	private void drawMaskWordLine(Canvas canvas, WordLine wordLine) {

		// ������Ҫ�Ż�,��ͼ��ССһ��ͺ���
		int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
				Canvas.ALL_SAVE_FLAG);

		drawNormalWordLine(canvas, wordLine, mCurSongingLine); // ����ͨ��

		Xfermode oldXferMode = paint.getXfermode();

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		canvas.clipRect(wordLine.getDrawX(), 0, mCurLineProgress 
				+ wordLine.getDrawX(),
				getHeight());

		canvas.drawBitmap(mBmp, 0, calcDrawMaskY(wordLine), paint); // ������

		canvas.restoreToCount(layerId);

		paint.setXfermode(oldXferMode);
	}

	private float calcDrawMaskY(WordLine wordLine) {
		return calcDrawLineY(wordLine) - mTextRect.height() * 3 / 2;
	}

	// ����ʵ�ʵĻ��е�λ��
	private float calcDrawLineY(WordLine wordLine) {
		return wordLine.getDrawY() - mScorllDisY;
	}

	private void drawNormalWordLine(Canvas canvas, WordLine wordLine,
			int lineIndex) {

		int oldAlpha = paint.getAlpha();

		paint.setAlpha((int) ((1 - (Math.abs(lineIndex - mCurSelecLine)) * 0.1f) * 255));

		canvas.drawText(wordLine.getWords(), wordLine.getDrawX(),
				calcDrawLineY(wordLine), paint);

		paint.setAlpha(oldAlpha);
	}

	private void savePaint() {
		mOldColor = paint.getColor();
		mOldStyle = paint.getStyle();
		mStrokeWidth = paint.getStrokeWidth();
		mOldTextSize = paint.getTextSize();
	}

	private void restorePaint() {
		paint.setColor(mOldColor);
		paint.setStyle(mOldStyle);
		paint.setStrokeWidth(mStrokeWidth);
		paint.setTextSize(mOldTextSize);
	}

	private void drawCurLineTime(Canvas canvas) {
		if (wordLines.size() == 0) {
			return;
		}
		savePaint();
		paint.setColor(songWordColor);
		paint.setTextSize(mTimeIndicatorTextSize);

		WordLine wordLine = wordLines.get(mCurSongingLine);

		canvas.drawText(wordLine.getWordLineTime(), 0 + getPaddingLeft(),
				(getMeasuredHeight() + mTimeIndicateRect.height()) / 2, paint);

		restorePaint();
	}

	private float getPlayBtnCenterX() {
		return getMeasuredWidth() - mPlayBtnRadius - mPaintLineWidth
				- getPaddingRight();
	}

	private float getPlayBtnCenterY() {
		return getMeasuredHeight() / 2;
	}

	private void drawPlayBtn(Canvas canvas) {
		savePaint();
		paint.setColor(songWordColor);

		paint.setStyle(Style.STROKE);

		paint.setStrokeWidth(2);

		// ����Ȧ��Բ��
		canvas.drawCircle(getPlayBtnCenterX(), getPlayBtnCenterY(),
				mPlayBtnRadius, paint);

		// ��Ȧ�ڵ�������
		paint.setStyle(Style.FILL);
		canvas.drawPath(mTrianglePath, paint);

		restorePaint();
	}

	/**
	 * @param songDuration
	 *            �������ŵ�ʱ��,��λ�Ǻ���
	 */
	public void setSongDuration(int songDuration) {
		mSongDuration = songDuration;

//		calcWordLineDurationTime();// ����ÿһ�г�����ʱ���Ƕ೤
	}

	public void setLinePrecent(int curPlaySongLine, float precent) {
		if (curPlaySongLine > mCurSongingLine) {
			return;
		}
		mPrecent = precent;

		mCurLineProgress = (wordLines.get(mCurSongingLine).getWidth() + mTextRect
				.width()) * precent;

		invalidate();
	}

	public void setCurPlayTime(int curTime) {
		
		if(wordLines == null || wordLines.size() == 0) {
			return;
		}
		
		for (int i = 0; i < wordLines.size(); i++) {

			WordLine wordLine = wordLines.get(i);
			if (wordLine.getTime() <= curTime
					&& (wordLine.getTime() + wordLine.getDuration()) >= curTime) {

				if (i != mCurSongingLine) {
					srcollToLine(i);
				}
				float hasPlayPrecent = (float) ((curTime - wordLine.getTime()) 
						/ wordLine.getDuration());

				setLinePrecent(i, hasPlayPrecent);

				break;
			}
		}
		invalidate();
	}
	public void setSongWords(List<WordLine> wordLines) {
		this.wordLines = wordLines;
		calcWordLinesDrawXY();
		createTrianglePath();
		calcScrollScope();

		invalidate();
	}


//	private void calcWordLineDurationTime() {
//		
//		if(wordLines == null && wordLines.size() == 0) {
//			return;
//		}
//		
//		for (int i = 0; i < wordLines.size() - 1; i++) {
//			WordLine curWordLine = wordLines.get(i);
//			WordLine nextWordLine = wordLines.get(i + 1);
//			// �������ʱ��// ���һ�����ں������
//			
//			curWordLine.setDuration((float)(nextWordLine.getTime() 
//						- curWordLine.getTime()));
//		}
//		WordLine lastWordLine = wordLines.get(wordLines.size() - 1);
//		
//		lastWordLine.setDuration((float) (mSongDuration - lastWordLine.getTime()));
//		
//	}

	public void calcWordLineWidth(WordLine wordLine) {
		paint.setTextSize(mSongWordTextSize);
		Rect temFontpRect = new Rect(); //
		paint.getTextBounds(wordLine.getWords(), 0, wordLine.getWords()
				.length(), temFontpRect);
		wordLine.setWidth(temFontpRect.width());
	}

	public void calcDrawX(WordLine wordLine) {
		calcWidth(wordLine);
		float drawX = (getMeasuredWidth() - wordLine.getWidth()) / 2;
		
		wordLine.setDrawX(drawX);
	}

	public void calcWidth(WordLine wordLine) {
		paint.setTextSize(mSongWordTextSize);
		Rect temFontpRect = new Rect(); //
		paint.getTextBounds(wordLine.getWords(), 
				0, wordLine.getWords().length(), temFontpRect);
	 	float width = temFontpRect.width();
		
		wordLine.setWidth(width);
	}

	public void calcDrawY(WordLine wordLine, int index) {

		float drawY = (mLineHeight + mTextRect.height()) / 2 + mLineHeight
				* index;

		wordLine.setDrawY(drawY);
	}

}
