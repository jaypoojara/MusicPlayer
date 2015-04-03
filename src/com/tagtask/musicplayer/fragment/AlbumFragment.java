package com.tagtask.musicplayer.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.activity.MusicPlayer;
import com.tagtask.musicplayer.adapter.AlbumGridviewAdapter;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.service.MusicPlayService;

public class AlbumFragment extends SherlockFragment implements
		LoaderCallbacks<Cursor>, OnItemClickListener {
	View view;
	GridView gvAlbums;
	AlbumGridviewAdapter adapter;
	ArrayList<HashMap<String, String>> allAlbums;

	public static AlbumFragment newInstance() {
		AlbumFragment fragment = new AlbumFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.album_fragment, container, false);
		setWidgetReference(view);
		initialization();
		bindWidgetEvent();
		return view;
	}

	private void bindWidgetEvent() {
		gvAlbums.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initialization() {
		allAlbums = new ArrayList<HashMap<String, String>>();
		getLoaderManager().initLoader(1, null, this);
	}

	private void setWidgetReference(View v) {
		gvAlbums = (GridView) v.findViewById(R.id.gvAlbums);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_ID };
		return new android.support.v4.content.CursorLoader(getActivity(),
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, null);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		HashMap<String, String> songData;
		data.moveToFirst();
		ArrayList<String> albumTemp = new ArrayList<String>();
		for (int i = 0; i < data.getCount(); i++) {
			songData = new HashMap<String, String>();
			if (!albumTemp.contains(data.getString(data
					.getColumnIndex(Constants.ALBUM)))) {
				albumTemp.add(data.getString(data
						.getColumnIndex(Constants.ALBUM)));
				for (int j = 0; j < data.getColumnCount(); j++) {
					songData.put(data.getColumnName(j), data.getString(j));
				}
				allAlbums.add(songData);
			}
			data.moveToNext();
		}
		adapter = new AlbumGridviewAdapter(getActivity(), allAlbums);
		gvAlbums.setAdapter(adapter);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Cursor albumDataCursor = getAlbumData(position);
		Constants.playList = new ArrayList<HashMap<String, String>>();
		ArrayList<String> songTitle = new ArrayList<String>();
		HashMap<String, String> songData = new HashMap<String, String>();
		albumDataCursor.moveToFirst();
		for (int i = 0; i < albumDataCursor.getCount(); i++) {
			songData = new HashMap<String, String>();
			songTitle.add(albumDataCursor.getString(albumDataCursor
					.getColumnIndex(Constants.TITLE)));
			for (int j = 0; j < albumDataCursor.getColumnCount(); j++) {
				songData.put(albumDataCursor.getColumnName(j),
						albumDataCursor.getString(j));
			}
			Constants.playList.add(songData);
			albumDataCursor.moveToNext();
		}
		ListView lvSongList = new ListView(getActivity());
		lvSongList.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				songTitle));
		showSongSelectionDialog(lvSongList, position);
	}

	private void showSongSelectionDialog(ListView lvSongList, int position) {
		Dialog dialog = new Dialog(getActivity());
		dialog.setTitle(allAlbums.get(position).get(Constants.ALBUM));
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

	private Cursor getAlbumData(int position) {
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ALBUM_ID };

		String where = android.provider.MediaStore.Audio.Media.ALBUM + "=?";

		String whereVal[] = { allAlbums.get(position).get(Constants.ALBUM) };

		String orderBy = android.provider.MediaStore.Audio.Media.TITLE;

		Cursor cursor = getActivity().managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, where,
				whereVal, orderBy);
		return cursor;
	}
}
