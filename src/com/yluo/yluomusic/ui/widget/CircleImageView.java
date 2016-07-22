package com.yluo.yluomusic.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

// ²Î¿¼¿í¸ßÊÇ70dp
@SuppressLint("DrawAllocation")
public class CircleImageView extends ImageView implements NestedScrollingChild{
	private static final String TAG = "CircleImageView";
	private Bitmap mDstBmp;
	private Paint paint;
	private Bitmap srcBitmap;
	private float mPaintWidth = 2.5f;
	private float mExacteWidth = 5;
	private RotateAnimation animation;
	private float mLastX;
	private float mLastY;
	private int[] mConsumed = new int[2];
	private NestedScrollingChildHelper mChildHelper;

	public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleImageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		initConfig();

		getMeasureWidthAndHeigh();

		mChildHelper = new NestedScrollingChildHelper(this);
		setNestedScrollingEnabled(true);
	}

	private void initConfig() {
		mExacteWidth = dp2px(mExacteWidth);

	}

	private void getMeasureWidthAndHeigh() {
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						changeBmp();
						createRotation();
						startRotation();
						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});
	}

	private void createRotation() {
		animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(5000);
		animation.setRepeatCount(Animation.INFINITE);
	}

	public void startRotation() {

		startAnimation(animation);
	}

	public void stopRotation() {
		clearAnimation();
	}

	private void CreatePaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xff000000);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(dp2px(mPaintWidth));
	}

	private void changeBmp() {
		CreatePaint();

		Canvas canvas = createDetBmp();

		createShader(canvas);

		setDrawable(canvas);
	}

	private Canvas createDetBmp() {
		mDstBmp = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mDstBmp);
		return canvas;
	}

	private void setDrawable(Canvas canvas) {
		int layerid = canvas.saveLayer(0, 0, getMeasuredWidth(),
				getMeasuredWidth(), paint, Canvas.ALL_SAVE_FLAG);
		canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
				getMeasuredWidth() / 2 - mExacteWidth, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		srcBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
		canvas.drawBitmap(srcBitmap, 0, 0, paint);
		paint.setXfermode(null);

		paint.setStyle(Style.STROKE);
		paint.setAlpha(150);
		canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
				getMeasuredWidth() / 2 - dp2px(mPaintWidth) / 2 - mExacteWidth,
				paint);
		canvas.restoreToCount(layerid);

		setImageBitmap(mDstBmp);
	}

	private void createShader(Canvas canvas) {
		RadialGradient gradient = new RadialGradient(getMeasuredWidth() / 2,
				getMeasuredHeight() / 2, getMeasuredWidth() / 2, 0xff000000,
				Color.TRANSPARENT, TileMode.CLAMP);

		paint.setShader(gradient);

		canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
				getMeasuredWidth(), paint);

		paint.setShader(null);
	}

	private int dp2px(float dp) {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return (int) (outMetrics.density * dp + 0.5f);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL);
			break;

		case MotionEvent.ACTION_MOVE:
			int disX = (int) (mLastX - event.getRawX());
			dispatchNestedPreScroll(disX, 0, mConsumed, null);
			dispatchNestedScroll(0, 0, disX - mConsumed[0], 0, null);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			stopNestedScroll();
			break;
		default:
			break;
		}

		mLastX = event.getRawX();
		mLastY = event.getRawY();
		return true;
	}

	@Override
	public void setNestedScrollingEnabled(boolean enabled) {
		mChildHelper.setNestedScrollingEnabled(enabled);
	}

	@Override
	public boolean isNestedScrollingEnabled() {
		return mChildHelper.isNestedScrollingEnabled();
	}

	@Override
	public boolean startNestedScroll(int axes) {
		return mChildHelper.startNestedScroll(axes);
	}

	@Override
	public void stopNestedScroll() {
		mChildHelper.stopNestedScroll();
	}

	@Override
	public boolean hasNestedScrollingParent() {
		return mChildHelper.hasNestedScrollingParent();
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
			int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {

		return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
				dxUnconsumed, dyUnconsumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed,
			int[] offsetInWindow) {
		return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed,
				offsetInWindow);
	}
}
