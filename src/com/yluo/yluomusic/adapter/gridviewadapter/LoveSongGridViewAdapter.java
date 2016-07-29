package com.yluo.yluomusic.adapter.gridviewadapter;

import java.util.List;

import com.yluo.yluomusic.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LoveSongGridViewAdapter extends BaseAdapter {

	private static final String TAG = "LoveSongGridViewAdapter";

	private boolean mIsShowMv = true;

	private Context mContext;

	private int[] mImagesIds;
	private String[] mImageText;

	public LoveSongGridViewAdapter(Context context) {
		mContext = context;
		mImagesIds = new int[] { R.drawable.love_song_gridview_download,
				R.drawable.love_song_gridview_add,
				R.drawable.love_song_gridview_share,
				R.drawable.love_song_gridview_songinfo,
				R.drawable.love_song_gridview_delete,
				R.drawable.love_song_gridview_songwild,
				R.drawable.love_song_gridview_ksong,
				R.drawable.love_song_gridview_mv };
		mImageText = new String[] { new String("œ¬‘ÿ"), new String("ÃÌº”"),
				new String("∑÷œÌ"), new String("∏Ë«˙–≈œ¢"), new String("…æ≥˝"),
				new String("∏Ë«˙¬˛”Œ"), new String("K∏Ë"), new String("MV") };
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImagesIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setmIsShowMv(boolean mIsShowMv) {
		this.mIsShowMv = mIsShowMv;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// View.inflate(mContext, resource, null);
		if (convertView == null) {
			// convertView = View.inflate(mContext, resource, null);
			// convertView.findViewById(R.id.tv_gv_download);

			convertView = new TextView(mContext);
		}

		if (position == (mImagesIds.length - 1)) {
			if (mIsShowMv) {
				convertView.setVisibility(View.VISIBLE);
			} else {
				convertView.setVisibility(View.INVISIBLE);
			}
		} else {
			// ImageView imageView = (ImageView) convertView;
			// imageView.setImageResource(mImagesIds[position]);

			TextView textView = (TextView) convertView;
			Drawable drawable = ContextCompat.getDrawable(mContext,
					mImagesIds[position]);
			// drawable.setBounds(0, 10, drawable.getIntrinsicWidth(),
			// drawable.getIntrinsicHeight());
			// drawable.setBounds(0, 10, drawable.getIntrinsicWidth(), 30);
			drawable.setBounds(0, 0, 40, 40);
			textView.setCompoundDrawables(null, drawable, null, null);
			textView.setCompoundDrawablePadding(20);
			textView.setText(mImageText[position]);
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(10);
			textView.setPadding(0, 10, 0, 0);
			Log.d(TAG,
					"position = " + position + "width="
							+ drawable.getIntrinsicWidth() + ",height="
							+ drawable.getIntrinsicHeight());
		}
		return convertView;
	}

}
