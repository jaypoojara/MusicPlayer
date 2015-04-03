package com.tagtask.musicplayer.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.activity.MusicPlayer;
import com.tagtask.musicplayer.adapter.GenreGridViewAdapter;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.service.MusicPlayService;

public class GenreFagment extends Fragment implements OnItemClickListener {
	ArrayList<HashMap<String, String>> genreData, allSong;
	HashMap<String, String> eachGenreData, eachSongData;
	GridView gvGenres;
	GenreGridViewAdapter adapter;

	public static Fragment newInstance() {
		GenreFagment genre = new GenreFagment();
		return genre;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.album_fragment, null, false);
		setWidgetReference(view);
		genreData = new ArrayList<HashMap<String, String>>();
		allSong = new ArrayList<HashMap<String, String>>();
		getGeneres();
		initialization();
		bindWidgetEvent();
		return view;
	}

	private void bindWidgetEvent() {
		gvGenres.setOnItemClickListener(this);
	}

	private void initialization() {
		adapter = new GenreGridViewAdapter(getActivity(), genreData);
		gvGenres.setAdapter(adapter);
	}

	private void setWidgetReference(View v) {
		gvGenres = (GridView) v.findViewById(R.id.gvAlbums);
	}

	@SuppressWarnings("deprecation")
	private void getGeneres() {
		int index;
		Cursor genrecursor;
		String[] proj1 = { MediaStore.Audio.Genres.NAME,
				MediaStore.Audio.Genres._ID };

		genrecursor = getActivity().managedQuery(
				MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, proj1, null,
				null, null);
		if (genrecursor.moveToFirst()) {
			do {
				eachGenreData = new HashMap<String, String>();
				eachSongData = new HashMap<String, String>();
				index = genrecursor
						.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
				eachGenreData.put(Constants.GENRE_NAME,
						genrecursor.getString(index));
				index = genrecursor
						.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
				eachGenreData.put(Constants.GENRE_ID,
						genrecursor.getString(index));
				genreData.add(eachGenreData);
			} while (genrecursor.moveToNext());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Constants.playList = new ArrayList<HashMap<String, String>>();
		ListView lvSongList = new ListView(getActivity());
		lvSongList.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				getAllSong(position)));
		Dialog dialog = new Dialog(getActivity());
		dialog.setTitle(genreData.get(position).get(Constants.GENRE_NAME));
		dialog.setContentView(lvSongList);
		lvSongList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> song = Constants.playList.get(position);
				Intent serviceIntent = new Intent(getActivity()
						.getApplicationContext(), MusicPlayService.class);
				serviceIntent.setAction(Constants.ACTION_PLAY);
				serviceIntent.putExtra(SongsFragment.SINGLE_SONG, song);
				if (MusicPlayService.getInstance() == null) {
					getActivity().getApplicationContext().startService(
							serviceIntent);

				} else {
					MusicPlayService.getInstance().stopSong();
					MusicPlayService.getInstance().stopSelf();
					getActivity().getApplicationContext().startService(
							serviceIntent);

				}
				Intent intent = new Intent(getActivity(), MusicPlayer.class);
				intent.putExtra(SongsFragment.SINGLE_SONG, song);
				intent.putExtra(SongsFragment.POSITION, position);
				startActivity(intent);

			}
		});
		dialog.show();
	}

	private ArrayList<String> getAllSong(int position) {
		ArrayList<String> title = new ArrayList<String>();
		Uri uri = MediaStore.Audio.Genres.Members
				.getContentUri(
						"external",
						Long.parseLong(genreData.get(position).get(
								Constants.GENRE_ID)));
		String[] proj2 = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ALBUM_ID };
		Cursor tempcursor = getActivity().managedQuery(uri, proj2, null, null,
				null);
		if (tempcursor.moveToFirst()) {
			do {
				eachSongData = new HashMap<String, String>();
				title.add(tempcursor.getString(tempcursor
						.getColumnIndex(Constants.TITLE)));
				for (int j = 0; j < tempcursor.getColumnCount(); j++) {
					eachSongData.put(tempcursor.getColumnName(j),
							tempcursor.getString(j));
				}
				Constants.playList.add(eachSongData);
			} while (tempcursor.moveToNext());
		}
		return title;
	}

}
