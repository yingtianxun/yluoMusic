package com.yluo.yluomusic.bean;

import com.yluo.yluomusic.R;

import android.R.integer;
import android.widget.ImageView;
import android.widget.TextView;

public class LoveSongBean {

//	private ImageView[] addImageViews;
//	private TextView[] songNameTextView;
//	private ImageView[] mvImageViews;
//	private ImageView[] loveImageViews;
//	private TextView[] singerTextViews;
//	private ImageView[] expandImageViews;
//	public LoveSongBean() {
//		init();
//	}
//
//	private void init() {
//		// TODO Auto-generated method stub
//		songNameTextView[0].setText("³ó°Ë¹Ö");
//		songNameTextView[1].setText("Ó¢ÐÛÀá");
//		songNameTextView[2].setText("ÄãµÄ±³°ü");
//		
//		singerTextViews[0].setText("Ñ¦Ö®Ç«");
//		singerTextViews[1].setText("Íõ   ½Ü");
//		singerTextViews[2].setText("³ÂÞÈÑ¸");
//		
//		addImageViews[0].setBackgroundResource(R.drawable.recycle_view_love_add);
//	}
//	
	
//	private ImageView addImageViews;
//	private TextView songNameTextView;
//	private ImageView mvImageViews;
//	private ImageView loveImageViews;
//	private TextView singerTextViews;
//	private ImageView expandImageViews;
	
	private int id;
	private String songName;
	private boolean hasMv;
	private boolean isLove;
	private String singerName;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public boolean hasMv() {
		return hasMv;
	}
	public void setMv(boolean hasMv) {
		this.hasMv = hasMv;
	}
	public boolean isLove() {
		return isLove;
	}
	public void setLove(boolean isLove) {
		this.isLove = isLove;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	@Override
	public String toString() {
		return "LoveSongBean [id=" + id + ", songName=" + songName + ", hasMv="
				+ hasMv + ", isLove=" + isLove + ", singerName=" + singerName
				+ "]";
	}


	
	
}





















