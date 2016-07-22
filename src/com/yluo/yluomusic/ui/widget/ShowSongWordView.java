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
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class ShowSongWordView extends View {

	private static final String TAG = "ShowSongWordView";
	private ArrayList<WordLine> wordLines;
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
		getMeasureWidthAndHeight();

		createXfermode();
	}

	private void initConfig() {
		mSongWordTextSize = dp2px(16);

	}

	private int dp2px(float dp) {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		return (int) (outMetrics.density * dp + 0.5f);

	}

	private void getMeasureWidthAndHeight() {
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						createWordMask();

						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});
	}

	private void createWordMask() {

		mMaskBmp = Bitmap.createBitmap(getMeasuredWidth(),
				mTextRect.height() * 2, Bitmap.Config.ARGB_8888);

		Canvas maskCanvas = new Canvas(mMaskBmp);

		maskCanvas.drawColor(mSongWordColor);
	}

	private void calcWordLinesDrawWidth() {
		for (int i = 0; i < wordLines.size(); i++) {
			wordLines.get(i).calcWidth();
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

			drawText = wordLines.get(i).words;

			if (drawText.length() != 0) {
				lineIndex = i;
				break;
			}
		}
		return lineIndex;
	}

	protected void onDraw(Canvas canvas) {

		drawCurWordLine(canvas);

		drawNextWordLine(canvas);
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

				setLinePrecent(i, hasPlayPrecent);

				break;
			}
		}
		invalidate();
	}

	private void srcollToLine(int lineIndex) {
		WordLine wordLine = wordLines.get(lineIndex);
		if (wordLine.words.length() == 0) {
			return;
		}
		setCurSongingLine(lineIndex);

	}

	public void setLinePrecent(int curPlaySongLine, float precent) {

		if (curPlaySongLine > mCurSongingLine) {
			return;
		}
		mCurLineProgress = (wordLines.get(mCurSongingLine).width + mTextRect
				.width()) * precent;

		invalidate();
	}

	private void createXfermode() {
		mMaskXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
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

	private float calcDrawMaskX(WordLine wordLine) {
		float drawX = 0;
		if (mCurDrawTextUp) {
			drawX = 0;
		} else {
			drawX = getMeasuredWidth() - wordLine.width - mTextRect.width() / 2;
		}

		return drawX;
	}

	private float calcDrawMaskY(WordLine wordLine) {
		float drawY = 0;
		if (mCurDrawTextUp) {
			drawY = mTextRect.height() / 3;
		} else {
			drawY = getMeasuredHeight() - mTextRect.height() * 2;
		}
		return drawY;
	}

	private void drawNextWordLine(Canvas canvas) {

		int lineIndex = findNotEmptySongLine(mCurSongingLine + 1);
		drawWorldLine(canvas, lineIndex, mNextDrawTextUp);
	}

	private void drawWorldLine(Canvas canvas, int wordLineIndex,
			boolean isDrawUp) {
		if (wordLineIndex != -1) {
			WordLine wordLine = wordLines.get(wordLineIndex);
			if (isDrawUp) {
				canvas.drawText(wordLine.words, mTextRect.width() / 2,
						mTextRect.height() * 3 / 2, mPaint);
			} else {
				canvas.drawText(wordLine.words, getMeasuredWidth()
						- wordLine.width - mTextRect.width() / 2,
						getMeasuredHeight() - mTextRect.height() / 2, mPaint);
			}
		}
	}

	public void setSongWords(int resId) {
		if (wordLines != null) {
			wordLines.clear();
		} else {
			wordLines = new ArrayList<WordLine>();
		}

		InputStream inputStream = getResources().openRawResource(resId);

		try {
			new UtilLrc(inputStream);
		} catch (IOException e) {
			Log.d(TAG, "解析歌词失败!");
			e.printStackTrace();
		}

		calcWordLinesDrawWidth();

		invalidate();
	}

	public void setSongDuration(int songDuration) {
		mSongDuration = songDuration;

		calcWordLineDurationTime();// 计算每一行持续的时间是多长
	}

	private void calcWordLineDurationTime() {
		for (int i = 0; i < wordLines.size() - 1; i++) {
			WordLine curWordLine = wordLines.get(i);
			WordLine nextWordLine = wordLines.get(i + 1);
			curWordLine.duration = (float) (nextWordLine.time - curWordLine.time);
		}
		WordLine lastWordLine = wordLines.get(wordLines.size() - 1);
		lastWordLine.duration = (float) (mSongDuration - lastWordLine.time);
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

		public void calcWidth() {
			mPaint.setTextSize(mSongWordTextSize);
			Rect temFontpRect = new Rect(); //
			mPaint.getTextBounds(words, 0, words.length(), temFontpRect);
			width = temFontpRect.width();
		}
	}

	class UtilLrc {
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
				// 添加歌词
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
