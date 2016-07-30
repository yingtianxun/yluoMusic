package com.yluo.yluomusic.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class YluoClipImageView extends ImageView {
	private static final String TAG = "YluoClipImageView";
	private Bitmap srcBitmap;
	private Paint mPaint;
	private Rect srcRect;

	public YluoClipImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public YluoClipImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public YluoClipImageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		BitmapDrawable drawable = (BitmapDrawable) getDrawable();
		
		srcBitmap = drawable.getBitmap();
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		srcRect = new Rect(0,0,srcBitmap.getWidth(),srcBitmap.getHeight());
	}

	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		
		canvas.save();
		canvas.clipRect(new Rect(0,0,getMeasuredWidth(),getMeasuredHeight()));
		
		canvas.drawBitmap(srcBitmap, srcRect
		, new Rect(0,0,getMeasuredWidth(),getMeasuredWidth()), mPaint);
		
		canvas.restore();
	}
}


















