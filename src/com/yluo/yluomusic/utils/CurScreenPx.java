package com.yluo.yluomusic.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

public class CurScreenPx {

	private static Point point;

	private static void getScreenPoint(Context context) {
		if (point == null) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			point = new Point();
			wm.getDefaultDisplay().getSize(point);
		}
	}

	public static int getX(Context context, float x) {

		getScreenPoint(context);

		return (int) (x * point.x / 320 + 0.5f);
	}

	public static int getY(Context context, float y) {

		getScreenPoint(context);

		return (int) (y * point.y / 480 + 0.5f);
	}

}
