package com.tagtask.musicplayer.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.actionbarsherlock.app.SherlockFragment;
import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.activity.MusicPlayer;
import com.tagtask.musicplayer.adapter.AllSongsListAdapter;
import com.tagtask.musicplayer.broadcastreceiver.NotificationRemoteviewReceiver;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.service.MusicPlayService;

@SuppressLint("NewApi")
public class SongsFragment extends SherlockFragment implements
		LoaderCallbacks<Cursor> {

	View view;
	ListView lvAllSongs;
	AllSongsListAdapter adapter;
	ProgressDialog pd;
	ArrayList<HashMap<String, String>> allSongs;
	public static final String SINGLE_SONG = "song";
	public static final String POSITION = "postion";

	public static SongsFragment newInstance() {
		SongsFragment fragment = new SongsFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.all_songs, null, false);
		setWidgetReference(view);
		initialization();
		bindWidgetEvent();
		return view;

	}

	private void bindWidgetEvent() {
		lvAllSongs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Constants.playList = allSongs;
				HashMap<String, String> song = Constants.playList.get(position);
				Intent serviceIntent = new Intent(getActivity()
						.getApplicationContext(), MusicPlayService.class);
				serviceIntent.setAction(Constants.ACTION_PLAY);
				serviceIntent.putExtra(SINGLE_SONG, song);
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
				intent.putExtra(SINGLE_SONG, song);
				intent.putExtra(POSITION, position);
				startActivity(intent);

			}
		});
	}

	private void setWidgetReference(View v) {
		lvAllSongs = (ListView) v.findViewById(R.id.lvAllSongsListFrament);
	}

	private void initialization() {
		pd = new ProgressDialog(getActivity());
		pd.setMessage("Please Wait");
		allSongs = new ArrayList<HashMap<String, String>>();
		getLoaderManager().initLoader(1, null, this);
	}

	

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		pd.show();
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ALBUM_ID };
		return new android.support.v4.content.CursorLoader(getActivity(),
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		HashMap<String, String> songData;
		data.moveToFirst();
		for (int i = 0; i < data.getCount(); i++) {
			songData = new HashMap<String, String>();
			for (int j = 0; j < data.getColumnCount(); j++) {
				songData.put(data.getColumnName(j), data.getString(j));
			}
			allSongs.add(songData);
			data.moveToNext();
		}
		adapter = new AllSongsListAdapter(
				getActivity().getApplicationContext(), allSongs);
		lvAllSongs.setAdapter(adapter);
		pd.dismiss();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}
}
