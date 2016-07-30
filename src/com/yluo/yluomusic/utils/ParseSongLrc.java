package com.yluo.yluomusic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.res.Resources;

import com.yluo.yluomusic.aidl.WordLine;

public class ParseSongLrc {
	public static List<WordLine> parseSongLrc(Resources resources,int resourceId,int songDuration) {
		InputStream inputStream = resources.openRawResource(resourceId);
		
		
		UtilLrc uLrc = new UtilLrc();
		
		List<WordLine> wordLines = uLrc.parseWords(inputStream);
		
		calcWordLineDurationTime(wordLines,songDuration);
		
		return wordLines;
	}
	
	private static void calcWordLineDurationTime(List<WordLine> wordLines,int songDuration) {
		if (wordLines.size() == 0) {
			return;
		}
		for (int i = 0; i < wordLines.size() - 1; i++) {
			WordLine curWordLine = wordLines.get(i);
			WordLine nextWordLine = wordLines.get(i + 1);
			curWordLine.setDuration((float) (nextWordLine.getTime() - curWordLine.getTime()));
		}
		WordLine lastWordLine = wordLines.get(wordLines.size() - 1);
		
		lastWordLine.setDuration((float) (songDuration - lastWordLine.getTime()));
	}

	
	static private class UtilLrc {
		private BufferedReader bufferReader = null;
		private String title = "";
		private String artist = "";
		private String album = "";
		private String lrcMaker = "";

		public UtilLrc() {
		}
		
		public List<WordLine> parseWords(InputStream inputStream) {
			List<WordLine> wordLines  = null;
			try {
				bufferReader = new BufferedReader(new InputStreamReader(
						inputStream, "utf-8"));
				
				wordLines = new ArrayList<WordLine>();
				
				readData(wordLines);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			return wordLines;
		}

		private void readData(List<WordLine> wordLines) throws IOException {
			String strLine;
			while (null != (strLine = bufferReader.readLine())) {
				if ("".equals(strLine.trim())) {
					continue;
				}
				if (null == title || "".equals(title.trim())) {
					Pattern pattern = Pattern.compile("\\[ti:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						title = matcher.group(1);
						continue;
					}
				}
				if (null == artist || "".equals(artist.trim())) {
					Pattern pattern = Pattern.compile("\\[ar:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						artist = matcher.group(1);
						continue;
					}
				}
				if (null == album || "".equals(album.trim())) {
					Pattern pattern = Pattern.compile("\\[al:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						album = matcher.group(1);
						continue;
					}
				}
				if (null == lrcMaker || "".equals(lrcMaker.trim())) {
					Pattern pattern = Pattern.compile("\\[by:(.+?)\\]");
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						lrcMaker = matcher.group(1);
						continue;
					}
				}
				int timeNum = 0;
				String str[] = strLine.split("\\]");
				for (int i = 0; i < str.length; ++i) {
					String str2[] = str[i].split("\\[");
					str[i] = str2[str2.length - 1];
					if (isTime(str[i])) {
						++timeNum;
					}
				}
				// 添加歌词
				for (int i = 0; i < timeNum; ++i) {

					WordLine wordLine = new WordLine();

					setWordLineTime(wordLine,str[i]); //好
					
					if (timeNum < str.length) {
						wordLine.setWords(str[str.length - 1]);
					} else {
						wordLine.setWords("");
					}
					wordLines.add(wordLine);
				}
			}
			sortLyric(wordLines);
		}
		public void setWordLineTime(WordLine wordLine,String time) {
			String str[] = time.split(":|\\.");
				
			wordLine.setWordLineTime(str[0] + ":" + str[1]);
			// 变成毫秒
			double realtime = (Integer.parseInt(str[0]) * 60
					+ Integer.parseInt(str[1]) + Integer.parseInt(str[2]) * 0.01) * 1000;
			
			wordLine.setTime(realtime);
			
		}
		
		private boolean isTime(String string) {
			String str[] = string.split(":|\\.");
			if (3 != str.length) {
				return false;
			}
			try {
				for (int i = 0; i < str.length; ++i) {
					Integer.parseInt(str[i]);
				}
			} catch (NumberFormatException e) {
				System.out.println();
				return false;
			}
			return true;
		}

		private void sortLyric(List<WordLine> wordLines) {
			for (int i = 0; i < wordLines.size() - 1; ++i) {
				int index = i;
				double delta = Double.MAX_VALUE;
				boolean moveFlag = false;
				for (int j = i + 1; j < wordLines.size(); ++j) {
					double sub;
					if (0 >= (sub = wordLines.get(i).getTime()
							- wordLines.get(j).getTime())) {
						continue;
					}
					moveFlag = true;
					if (sub < delta) {
						delta = sub;
						index = j + 1;
					}
				}
				if (moveFlag) {
					wordLines.add(index, wordLines.get(i));
					wordLines.remove(i);
					--i;
				}
			}
		}

		public String getTitle() {
			return title;
		}

		public String getArtist() {
			return artist;
		}

		public String getAlbum() {
			return album;
		}

		public String getLrcMaker() {
			return lrcMaker;
		}
	}
	
}
