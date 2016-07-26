package com.yluo.yluomusic.adapter.gridviewadapter;

import java.util.List;

import com.yluo.yluomusic.R;

import android.R.integer;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class LoveSongGridViewAdapter extends BaseAdapter{
	
	private boolean mIsShowMv = true;
	
	private Context mContext;
	
	private int[] mImagesIds;
	
	public LoveSongGridViewAdapter(Context context) {
		mContext = context;
		mImagesIds = new int[]{R.drawable.love_song_gridview_download,
				R.drawable.love_song_gridview_add,R.drawable.love_song_gridview_share,
				R.drawable.love_song_gridview_songinfo,R.drawable.love_song_gridview_delete,
				R.drawable.love_song_gridview_songwild,R.drawable.love_song_gridview_ksong,
				R.drawable.love_song_gridview_mv};
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
		
		if (convertView == null) {
			convertView = new ImageView(mContext);
		}
		
		if(position == (mImagesIds.length - 1)) {
			if(mIsShowMv) {
				convertView.setVisibility(View.VISIBLE);
			} else {
				convertView.setVisibility(View.INVISIBLE);
			}
		} else {
			ImageView imageView = (ImageView) convertView;	
			imageView.setImageResource(mImagesIds[position]);
		}
		
		
		return convertView;
	}

}
















