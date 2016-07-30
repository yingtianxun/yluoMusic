package com.yluo.yluomusic.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class YluoChangeColorImageView extends ImageView{

	private final int defaultColor = 0xff959595;
	public YluoChangeColorImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public YluoChangeColorImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public YluoChangeColorImageView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		BitmapDrawable drawable = (BitmapDrawable) getDrawable();
		
		Bitmap srcBitmap = drawable.getBitmap();
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(defaultColor);
		
		Bitmap newBmp = Bitmap.createBitmap(srcBitmap.getWidth(), 
				srcBitmap.getHeight(), Config.ARGB_8888);
		
		Canvas canvas = new Canvas(newBmp);
		
		canvas.drawBitmap(srcBitmap, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawRect(new Rect(0,0,srcBitmap.getWidth(), 
				srcBitmap.getHeight()), paint);
		
		setImageBitmap(newBmp);
	}
}



















