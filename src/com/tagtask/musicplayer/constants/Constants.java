package com.tagtask.musicplayer.constants;

import java.util.ArrayList;
import java.util.HashMap;

import android.provider.MediaStore;

public class Constants {
	public static final String ARTIST = MediaStore.Audio.Media.ARTIST;
	public static final String ALBUM = MediaStore.Audio.Media.ALBUM;
	public static final String TITLE = MediaStore.Audio.Media.TITLE;
	public static final String DATA = MediaStore.Audio.Media.DATA;
	public static final String ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID;
	public static final String DURATION = MediaStore.Audio.Media.DURATION;
	public static final String ACTION_PLAY = "com.tagtask.ACTION_PLAY";
	public static ArrayList<HashMap<String, String>> allSongs = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> playList = new ArrayList<HashMap<String, String>>();
	public static final String ACTION_PAUSE = "com.tagtask.ACTION_PAUSE";
	public static final String ACTION_SHUTDOWN = "com.tagtask.ACTION_SHUTDOWN";
	public static final String GENRE_NAME = MediaStore.Audio.Genres.NAME;
	public static final String GENRE_ID = MediaStore.Audio.Genres._ID;
	public static final String ACTION_NEXT = "com.tagtask.ACTION_NEXT";
	public static final String ACTION_PREVIOUS = "com.tagtask.ACTION_PREVIOUS";
}
