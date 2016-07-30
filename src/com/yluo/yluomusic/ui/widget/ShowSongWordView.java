package com.yluo.yluomusic.ui.widget;

import java.util.List;

import com.yluo.yluomusic.aidl.WordLine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class ShowSongWordView extends View {

	private static final String TAG = "ShowSongWordView";
	private List<WordLine> wordLines;
	private int mSongDuration;
	private Paint mPaint;
	private boolean mCurDrawTextUp = true; // 用来判断画在上面还是下面的
	private boolean mNextDrawTextUp = false; // 用来判断画在上面还是下面的
	private int mCurSongingLine = 0; // 当前唱的行
	private float mSongWordTextSize;
	private Rect mTextRect;
	private Bitmap mMaskBmp;
	private int mSongWordColor = 0xFF54BEE9;
	private float mCurLineProgress;
	private Xfermode mMaskXfermode;

	private static final String NO_WORDS = "暂无歌词...";

	public ShowSongWordView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ShowSongWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ShowSongWordView(Context context) {
		super(context);
		init();
	}

	private void init() {

		initConfig();

		createPaint();
		calcTextHeight();
		// getMeasureWidthAndHeight();

		createXfermode();
	}

	private void initConfig() {
		mSongWordTextSize = dp2px(14);

	}

	private int dp2px(float dp) {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		return (int) (outMetrics.density * dp + 0.5f);

	}
	/**
	 * 创建歌词的遮罩
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		createWordMask();

	}

	private void createWordMask() {

		if (mMaskBmp != null || wordLines.size() == 0) {

			return;
		}

		mMaskBmp = Bitmap.createBitmap(getAvailableWidth(),
				mTextRect.height() * 2, Bitmap.Config.ARGB_8888);

		Canvas maskCanvas = new Canvas(mMaskBmp);

		maskCanvas.drawColor(mSongWordColor);

	}

	private void calcWordLinesDrawWidth() {
		for (int i = 0; i < wordLines.size(); i++) {
			calcWidth(wordLines.get(i));
		}
	}

	private void calcTextHeight() {
		mTextRect = new Rect();
		mPaint.setTextSize(mSongWordTextSize);
		mPaint.getTextBounds("1", 0, 1, mTextRect);
	}

	private void createPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(30);
	}

	public void setCurSongingLine(int curSongingLine) {
		if (mCurSongingLine == curSongingLine) {
			return;
		}
		mCurSongingLine = curSongingLine;

		mNextDrawTextUp = mCurDrawTextUp;

		mCurDrawTextUp = !mCurDrawTextUp;

	}

	private int findNotEmptySongLine(int curSongingLine) {
		String drawText = "";
		int lineIndex = -1;
		for (int i = curSongingLine; i < wordLines.size(); i++) {

			drawText = wordLines.get(i).getWords();

			if (drawText.length() != 0) {
				lineIndex = i;
				break;
			}
		}
		return lineIndex;
	}

	protected void onDraw(Canvas canvas) {

		if (wordLines.size() == 0) {
			drawEmptyWord(canvas); // 提示没歌词
		} else {
			drawCurWordLine(canvas);

			drawNextWordLine(canvas);

		}
	}

	private void drawEmptyWord(Canvas canvas) {
		int drawX = (getAvailableWidth() - mTextRect.width()
				* NO_WORDS.length()) / 2;
		int drawY = (getAvailableHeight() + mTextRect.height()) / 2;
		canvas.drawText(NO_WORDS, drawX, drawY, mPaint);
	}

	/**
	 * 获取减去padding之后的值
	 * 
	 * @return
	 */
	private int getAvailableWidth() {
		return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
	}

	/**
	 * 
	 * @return
	 */
	private int getAvailableHeight() {
		return getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
	}

	private void drawNextWordLine(Canvas canvas) {

		int lineIndex = findNotEmptySongLine(mCurSongingLine + 1);
		drawWorldLine(canvas, lineIndex, mNextDrawTextUp);
	}

	private void drawCurWordLine(Canvas canvas) {

		WordLine wordLine = wordLines.get(mCurSongingLine);

		int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
				Canvas.ALL_SAVE_FLAG);
		drawWorldLine(canvas, mCurSongingLine, mCurDrawTextUp); // 画普通行
		mPaint.setXfermode(mMaskXfermode);
		canvas.clipRect(calcDrawMaskX(wordLine), calcDrawMaskY(wordLine),
				calcDrawMaskX(wordLine) + mCurLineProgress, getHeight());

		canvas.drawBitmap(mMaskBmp, 0, calcDrawMaskY(wordLine), mPaint); // 画遮罩
		mPaint.setXfermode(null);
		canvas.restoreToCount(layerId);
	}

	public void setCurPlayTime(int curTime) {
		if (wordLines.size() == 0) {
			return;
		}

		for (int i = 0; i < wordLines.size(); i++) {
			WordLine wordLine = wordLines.get(i);
			if (wordLine.getTime() <= curTime
					&& (wordLine.getTime() + wordLine.getDuration()) >= curTime) {

				if (i != mCurSongingLine) {
					srcollToLine(i);
				}
				float hasPlayPrecent = (float) ((curTime - wordLine.getTime()) / wordLine.getDuration());

				setLinePrecent(i, hasPlayPrecent);

				break;
			}
		}
		invalidate();
	}

	private void srcollToLine(int lineIndex) {
		WordLine wordLine = wordLines.get(lineIndex);
		if (wordLine.getWords().length() == 0) {
			return;
		}
		setCurSongingLine(lineIndex);

	}

	public void setLinePrecent(int curPlaySongLine, float precent) {

		if (curPlaySongLine > mCurSongingLine) {
			return;
		}
		mCurLineProgress = (wordLines.get(mCurSongingLine).getWidth() + mTextRect
				.width()) * precent;

		invalidate();
	}

	private void createXfermode() {
		mMaskXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
	}

	private float calcDrawMaskX(WordLine wordLine) {
		float drawX = 0;
		if (mCurDrawTextUp) {
			drawX = getPaddingLeft();
		} else {
			drawX = getMeasuredWidth() - getPaddingRight() - wordLine.getWidth()
					- mTextRect.width() / 2;
		}

		return drawX;
	}

	private float calcDrawMaskY(WordLine wordLine) {
		float drawY = 0;
		if (mCurDrawTextUp) {
			drawY = getPaddingTop() + mTextRect.height() / 3;
		} else {
			drawY = getMeasuredHeight() - getPaddingBottom()
					- mTextRect.height() * 2;
		}
		return drawY;
	}

	private void drawWorldLine(Canvas canvas, int wordLineIndex,
			boolean isDrawUp) {
		if (wordLineIndex != -1) {
			WordLine wordLine = wordLines.get(wordLineIndex);
			if (isDrawUp) {
				canvas.drawText(wordLine.getWords(),
						getPaddingLeft() + mTextRect.width() / 2,
						getPaddingTop() + mTextRect.height() * 3 / 2, mPaint);
			} else {
				canvas.drawText(wordLine.getWords(),
						getMeasuredWidth() - getPaddingRight() - wordLine.getWidth()
								- mTextRect.width() / 2, getMeasuredHeight()
								- getPaddingBottom() - mTextRect.height() / 2,
						mPaint);
			}
		}
	}

	public void setSongWords(List<WordLine> wordLines) {

		this.wordLines = wordLines;
		calcWordLinesDrawWidth(); // 就算歌词的坐标
		invalidate();
	}

	public void setSongDuration(int songDuration) {
		mSongDuration = songDuration;
	}
	public void calcWidth(WordLine wordLine) {
		mPaint.setTextSize(mSongWordTextSize);
		Rect temFontpRect = new Rect(); //
		mPaint.getTextBounds(wordLine.getWords(), 0, wordLine.getWords().length(), temFontpRect);
		wordLine.setWidth(temFontpRect.width());
	}
}
