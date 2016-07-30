package com.yluo.yluomusic.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class PaintUtil {
	public static Paint createPaint() {
		return createPaint(Color.RED);
	}
	public static Paint createPaint(int color) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		paint.setColor(color);
		
		paint.setStrokeWidth(1);
		
		paint.setStyle(Style.FILL);
		
		return paint;
	}
	
	public static Paint createPaint(int color,int LineHeight) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		paint.setColor(color);
		
		paint.setStrokeWidth(LineHeight);
		
		paint.setStyle(Style.FILL);
		
		return paint;
	}
}
