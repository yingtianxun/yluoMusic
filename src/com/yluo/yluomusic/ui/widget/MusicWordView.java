package com.yluo.yluomusic.ui.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Xfermode;
import android.support.v4.view.ViewCompat;
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
import android.widget.Scroller;

public class MusicWordView extends View {

	private static final String TAG = "MusicWordView";

	private ArrayList<WordLine> wordLines;

	private Paint paint;

	private Rect mTextRect;

	private Rect mTimeIndicateRect;

	private Bitmap mBmp;

	private int mCurSongingLine = 0;

	private float mScorllDisY = 0;

	private float mLineHeight; // 行高

	private  float mLinePadding ;
	
	private int mTopLinePosition; // 在顶部的位置
	
	private float mLastY; // 记录上一次触摸事件的x,y坐标
	private float mLastX;
	private int mTouchSlop;

	private int mOldColor;

	private int songWordColor = 0xFFD8EB92;

	private Style mOldStyle;

	private float mStrokeWidth;

	private Path mTrianglePath;

	private float mPlayBtnRadius ; //绘制的范围
	
	private float mPlayBtnTouchRadius ; // 触摸判断的范围

	private float mOldTextSize;

	private OverScroller mScroller;

	private VelocityTracker mVelocityTracker;

	private LinearGradient leftLinearGradient;
	private LinearGradient rightLinearGradient;

	private float mMinFlingVelocity;

	private float mMaxFlingVelocity;

	private float mMinScrollPosition = 0;
	private float mMaxScrollPosition = 0;

	private float mCurLineProgress = 0;

	private float mTimeIndicatorTextSize ;

	private float mSongWordTextSize;

	private int mSongDuration = 0;
	
	private int mPaintLineWidth ;
	
	private float mPrecent;

	private boolean mIsMove;

	private int mCurSelecLine;
	

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
		mLinePadding = dp2px(10);
		mTimeIndicatorTextSize = dp2px(15);
		
		mSongWordTextSize = dp2px(20);
		
		mPaintLineWidth = dp2px(2.5f);
		
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
		paint = new Paint();
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

		mLineHeight = mTextRect.height() + mLinePadding; // 获取行高

		paint.setTextSize(mTimeIndicatorTextSize);
		mTimeIndicateRect = new Rect();
		paint.getTextBounds("1", 0, 1, mTimeIndicateRect);

		paint.setTextSize(mSongWordTextSize);
	}

	private void getMeasureWidthAndHeight() {
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						// 创建遮罩
						createWordMask();
						calcWordLinesDrawXY();
						// 创建三角形的路径
						createTrianglePath();
						// 创建渐近线
						// createLinearGradient();s
						// 计算最大滚动范围
						calcScrollScope();
						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});
	}

	private void calcScrollScope() {
		mMaxScrollPosition = wordLines.size() * mLineHeight
				- getMeasuredHeight() / 2 - mLineHeight / 2;
		mMinScrollPosition = -getMeasuredHeight() / 2 + mLineHeight / 2;
		srcollToLine(0);
	}
	private void createTrianglePath() {
		mTrianglePath = new Path();

		float topPointX = getMeasuredWidth() - mPlayBtnRadius - mPlayBtnRadius
				/ 2 - 2;
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

		mBmp = Bitmap.createBitmap(getMeasuredWidth(), (int) mLineHeight,
				Bitmap.Config.ARGB_8888);

		Canvas maskCanvas = new Canvas(mBmp);

		maskCanvas.drawColor(songWordColor);
	}

	private void calcWordLinesDrawXY() {

		for (int i = 0; i < wordLines.size(); i++) {
			wordLines.get(i).calcDrawX();
			wordLines.get(i).calcDrawY(i);
		}
	}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mScorllDisY = mScroller.getCurrY();
			invalidate();
		}
	}
	// 计算两次触摸事件的移动距离
	private float calcMoveDistance(MotionEvent event) {
		
		float disX = event.getX() - mLastX;
		float disY = event.getY() - mLastY;
		
		return (float) Math.sqrt(disX * disX + disY * disY);
	}
	
	private boolean isClickPlayBtn(MotionEvent event) {
		float moveDistance = calcMoveDistance(event);
		
		if(moveDistance != 0) {
			return false;
		}
		
		float disX = getPlayBtnCenterX() - event.getX();
		float disY = getPlayBtnCenterY() - event.getY();
		float distance = (float) Math.sqrt(disX * disX + disY * disY);
		if(distance > mPlayBtnTouchRadius) {
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
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			
			float disY = event.getY() - mLastY;
			// 处理最小滑动的
			if(!mIsMove) {
				if(Math.abs(disY) < mTouchSlop) {
					break;
				}
				// 做滚动补偿
				if(disY > 0) {
					disY -= mTouchSlop;
				} else {
					disY += mTouchSlop;
				}
				mIsMove = true;
			} 
			mVelocityTracker.addMovement(event);
			mScorllDisY -= disY;
			invalidate();
		}
			break;
		case MotionEvent.ACTION_UP: {
			// 判断是否点击圆形了
			if(isClickPlayBtn(event)) {
				
				Log.d(TAG, "点击圆形了--------");
			} else {
				// 滚动事件
				mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
				
				float curYVelocity = mVelocityTracker.getYVelocity();
				
				if (Math.abs(curYVelocity) < mMinFlingVelocity) {
					scrollViewBack(); // 超出范围滚回来的
				} else {
					filingView(curYVelocity); // 滑动的
				}
			}
			
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
		WordLine wordLine = wordLines.get(lineIndex);
		if (wordLine.words.length() == 0) {
			return;
		}

		
		// 判断是否超出行数
		if (lineIndex < 0 || lineIndex >= wordLines.size()) {
			return;
		}
		int disY = (int) ((-(getMeasuredHeight() / 2 - lineIndex * mLineHeight - mLineHeight / 2)) - mScorllDisY);

		mCurSongingLine = lineIndex;
		
		mCurSelecLine = mCurSongingLine;
		

		mCurLineProgress = (wordLines.get(mCurSongingLine).width + mTextRect
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
				(int) mMinScrollPosition, (int) mMaxScrollPosition, 0, dp2px(75));
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

	public void setSongWords(String[] songWords) {
		if (wordLines != null) {
			wordLines.clear();
		} else {
			wordLines = new ArrayList<MusicWordView.WordLine>();
		}
		for (int i = 0; i < songWords.length; i++) {

			WordLine wordLine = new WordLine();

			wordLine.words = songWords[i];
			wordLines.add(wordLine);
		}
		calcScrollScope();

		invalidate();
	}

	// 计算顶部应该在的位置
	private void getTopLinePosition() {
		// 计算当前的位置
		mTopLinePosition = (int) (mScorllDisY / mLineHeight);

		if (mTopLinePosition < 0) {
			mTopLinePosition = 0;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		// drawIndicateLine(canvas);
		drawWordLines(canvas);

		drawPlayBtn(canvas);

		drawCurLineTime(canvas);
	}

	private void drawIndicateLine(Canvas canvas) {
		// 画左线
		savePaint();
		// paint.setShader(leftLinearGradient);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(1);
		canvas.drawLine(mTimeIndicateRect.width() * 10,
				getMeasuredHeight() / 2 - 1, getMeasuredWidth(),
				getMeasuredHeight() / 2 - 1, paint);
		// paint.setShader(null);

		restorePaint();
	}
	private void drawWordLines(Canvas canvas) {
		getTopLinePosition(); // 计算顶部位置,从第几行开始画
		
		float curShowHeight = 0;
		for (int i = mTopLinePosition; i < wordLines.size(); i++) {

			curShowHeight = i * mLineHeight - mScorllDisY;

			WordLine wordLine = wordLines.get(i);
			 changeLineByScroll((int)curShowHeight,i); //这里滚着滚动换色的
			 
			if (i == mCurSongingLine) {
				drawMaskWordLine(canvas, wordLine);
			} else {
				drawNormalWordLine(canvas, wordLine,i);
			}
			// 这个判断的就不要画多余的
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

		// 这里需要优化,截图大小小一点就好了
		int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
				Canvas.ALL_SAVE_FLAG);

		drawNormalWordLine(canvas, wordLine,mCurSongingLine); // 画普通行

		Xfermode oldXferMode = paint.getXfermode();

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		canvas.clipRect(wordLine.drawX, 0, mCurLineProgress + wordLine.drawX,
				getHeight());

		canvas.drawBitmap(mBmp, 0, calcDrawMaskY(wordLine), paint); // 画遮罩

		canvas.restoreToCount(layerId);

		paint.setXfermode(oldXferMode);
	}

	private float calcDrawMaskY(WordLine wordLine) {
		return calcDrawLineY(wordLine) - mTextRect.height() * 3 / 2;
	}

	// 计算实际的画行的位置
	private float calcDrawLineY(WordLine wordLine) {
		return wordLine.drawY - mScorllDisY;
	}

	private void drawNormalWordLine(Canvas canvas, WordLine wordLine,int lineIndex) {
		
		int oldAlpha = paint.getAlpha();
		
		paint.setAlpha((int) ((1 - (Math.abs(lineIndex - mCurSelecLine)) * 0.1f) * 255));
		
		canvas.drawText(wordLine.words, wordLine.drawX,
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
		savePaint();
		paint.setColor(songWordColor);
		paint.setTextSize(mTimeIndicatorTextSize);

		WordLine wordLine = wordLines.get(mCurSongingLine);

		canvas.drawText(wordLine.wordLineTime, 0,
				(getMeasuredHeight() + mTimeIndicateRect.height()) / 2, paint);

		restorePaint();
	}
	
	private float getPlayBtnCenterX() {
		return getMeasuredWidth() - mPlayBtnRadius - mPaintLineWidth;
	}
	private float getPlayBtnCenterY() {
		return getMeasuredHeight() / 2;
	}

	private void drawPlayBtn(Canvas canvas) {
		savePaint();
		paint.setColor(songWordColor);

		paint.setStyle(Style.STROKE);

		paint.setStrokeWidth(2);

		// 画外圈的圆形
		canvas.drawCircle(getPlayBtnCenterX(),getPlayBtnCenterY(), mPlayBtnRadius, paint);

		// 画圈内的三角形
		paint.setStyle(Style.FILL);
		canvas.drawPath(mTrianglePath, paint);

		restorePaint();
	}

	/**
	 * @param songDuration
	 *            歌曲播放的时长,单位是毫秒
	 */
	public void setSongDuration(int songDuration) {
		mSongDuration = songDuration;
		
		calcWordLineDurationTime();// 计算每一行持续的时间是多长
	}

	public void setLinePrecent(int curPlaySongLine,float precent) {
		if (curPlaySongLine > mCurSongingLine) {
			return;
		}
		mPrecent = precent;

		mCurLineProgress = (wordLines.get(mCurSongingLine).width + mTextRect
				.width()) * precent;

		invalidate();
	}

	public void setCurPlayTime(int curTime) {

		for (int i = 0; i < wordLines.size(); i++) {

			WordLine wordLine = wordLines.get(i);
			if (wordLine.time <= curTime
					&& (wordLine.time + wordLine.duration) >= curTime) {

				if (i != mCurSongingLine) {
					srcollToLine(i);
				}
				float hasPlayPrecent = (float) ((curTime - wordLine.time) / wordLine.duration);

				setLinePrecent(i,hasPlayPrecent);

				break;
			}
		}
		invalidate();
	}

	public void setSongWordsPath(String path) {
		
	}

	public void setSongWords(int resId) {
		if (wordLines != null) {
			wordLines.clear();
		} else {
			wordLines = new ArrayList<WordLine>();
		}

		InputStream inputStream = getResources().openRawResource(resId);

		try {
			UtilLrc utilLrc = new UtilLrc(inputStream);
		} catch (IOException e) {
			Log.d(TAG, "解析歌词失败!");
			e.printStackTrace();
		}
//		
		calcScrollScope();

		invalidate();
	}

	private void calcWordLineDurationTime() {
		for (int i = 0; i < wordLines.size() - 1; i++) {
			WordLine curWordLine = wordLines.get(i);
			WordLine nextWordLine = wordLines.get(i + 1);
			// 计算持续时间// 最后一个就在后面计算
			curWordLine.duration = (float) (nextWordLine.time - curWordLine.time);
		}
		
		WordLine lastWordLine = wordLines.get(wordLines.size() - 1);
		lastWordLine.duration =  (float) (mSongDuration - lastWordLine.time);
	}

	public class WordLine {
		public String words;
		public double time;
		public float duration;
		public float width;
		public float drawX;
		public float drawY;
		public String wordLineTime;

		public void setTime(String time) {
			String str[] = time.split(":|\\.");

			wordLineTime = str[0] + ":" + str[1];
			// 变成毫秒
			this.time = (Integer.parseInt(str[0]) * 60
					+ Integer.parseInt(str[1]) + Integer.parseInt(str[2]) * 0.01) * 1000;
		}

		public void calcDrawY(int index) {

			drawY = (mLineHeight + mTextRect.height()) / 2 + mLineHeight
					* index;
		}

		public void calcDrawX() {
			calcWidth();
			drawX = (getMeasuredWidth() - width) / 2;
		}

		public void calcWidth() {

			paint.setTextSize(mSongWordTextSize);
			Rect temFontpRect = new Rect(); //
			paint.getTextBounds(words, 0, words.length(), temFontpRect);
			width = temFontpRect.width();
		}
	}

	class UtilLrc {
		private final String TAG = "utilLrc";

		private BufferedReader bufferReader = null;
		private String title = "";
		private String artist = "";
		private String album = "";
		private String lrcMaker = "";

		public UtilLrc(InputStream inputStream) throws IOException {
			bufferReader = new BufferedReader(new InputStreamReader(
					inputStream, "utf-8"));
			readData();
		}

		private void readData() throws IOException {
			wordLines.clear();
			String strLine;
			while (null != (strLine = bufferReader.readLine())) {
				if ("".equals(strLine.trim())) {
					continue;
				}
				if (null == title || "".equals(title.trim())) {
					Pattern pattern = Pattern.compile("\\[ti:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						title = matcher.group(1);
						continue;
					}
				}
				if (null == artist || "".equals(artist.trim())) {
					Pattern pattern = Pattern.compile("\\[ar:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						artist = matcher.group(1);
						continue;
					}
				}
				if (null == album || "".equals(album.trim())) {
					Pattern pattern = Pattern.compile("\\[al:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						album = matcher.group(1);
						continue;
					}
				}
				if (null == lrcMaker || "".equals(lrcMaker.trim())) {
					Pattern pattern = Pattern.compile("\\[by:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						lrcMaker = matcher.group(1);
						continue;
					}
				}
				int timeNum = 0;
				String str[] = strLine.split("\\]");
				for (int i = 0; i < str.length; ++i) {
					String str2[] = str[i].split("\\[");
					str[i] = str2[str2.length - 1];
					if (isTime(str[i])) {
						++timeNum;
					}
				}
				for (int i = 0; i < timeNum; ++i) {

					WordLine wordLine = new WordLine();

					wordLine.setTime(str[i]);
					if (timeNum < str.length) {
						wordLine.words = str[str.length - 1];
					} else {
						wordLine.words = "";
					}
					wordLines.add(wordLine);
				}
			}
			sortLyric();
		}

		private boolean isTime(String string) {
			String str[] = string.split(":|\\.");
			if (3 != str.length) {
				return false;
			}
			try {
				for (int i = 0; i < str.length; ++i) {
					Integer.parseInt(str[i]);
				}
			} catch (NumberFormatException e) {
				System.out.println();
				return false;
			}
			return true;
		}

		private void sortLyric() {
			for (int i = 0; i < wordLines.size() - 1; ++i) {
				int index = i;
				double delta = Double.MAX_VALUE;
				boolean moveFlag = false;
				for (int j = i + 1; j < wordLines.size(); ++j) {
					double sub;
					if (0 >= (sub = wordLines.get(i).time
							- wordLines.get(j).time)) {
						continue;
					}
					moveFlag = true;
					if (sub < delta) {
						delta = sub;
						index = j + 1;
					}
				}
				if (moveFlag) {
					wordLines.add(index, wordLines.get(i));
					wordLines.remove(i);
					--i;
				}
			}
		}

		public String getTitle() {
			return title;
		}

		public String getArtist() {
			return artist;
		}

		public String getAlbum() {
			return album;
		}

		public String getLrcMaker() {
			return lrcMaker;
		}
	}

}
