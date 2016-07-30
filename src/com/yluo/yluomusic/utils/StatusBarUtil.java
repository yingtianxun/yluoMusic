package com.yluo.yluomusic.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.Toast;

public class StatusBarUtil {
	public static int getStatusBarHeight(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= 19
				&& context instanceof Activity) {

			Point onPoint = new Point();

			((Activity) context).getWindowManager().getDefaultDisplay()
					.getSize(onPoint);
			
			Rect outRect = new Rect();
			((Activity) context).getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(outRect);
			
			return onPoint.y - outRect.height();
		}
		return 0;
	}
}
