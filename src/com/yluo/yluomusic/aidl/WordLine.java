package com.yluo.yluomusic.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class WordLine implements Parcelable {
	private String words;
	private double time;
	private float duration;
	private float width;
	private float drawX;
	private float drawY;
	private String wordLineTime;

	public static final Parcelable.Creator<WordLine> CREATOR = new Parcelable.Creator<WordLine>() {

		@Override
		public WordLine createFromParcel(Parcel source) {
			return new WordLine(source);
		}

		@Override
		public WordLine[] newArray(int size) {
			return new WordLine[size];
		}
	};
	
	public WordLine() {
		
	}

	public WordLine(Parcel source) {
		words = source.readString();
		time = source.readDouble();
		duration = source.readFloat();
		width = source.readFloat();
		drawX = source.readFloat();
		drawY = source.readFloat();
		wordLineTime = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(words);
		dest.writeDouble(time);
		dest.writeFloat(duration);
		dest.writeFloat(width);
		dest.writeFloat(drawX);
		dest.writeFloat(drawY);
		dest.writeString(wordLineTime);
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getDrawX() {
		return drawX;
	}

	public void setDrawX(float drawX) {
		this.drawX = drawX;
	}

	public float getDrawY() {
		return drawY;
	}

	public void setDrawY(float drawY) {
		this.drawY = drawY;
	}

	public String getWordLineTime() {
		return wordLineTime;
	}

	public void setWordLineTime(String wordLineTime) {
		this.wordLineTime = wordLineTime;
	}

}
