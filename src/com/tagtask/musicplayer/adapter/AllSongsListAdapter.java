package com.tagtask.musicplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.utils.Utils;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class AllSongsListAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<HashMap<String, String>> allSongsData;
	LayoutInflater li;

	public AllSongsListAdapter(Context context,
			ArrayList<HashMap<String, String>> data) {
		mContext = context;
		li = LayoutInflater.from(mContext);
		allSongsData = data;
	}

	@Override
	public int getCount() {
		return allSongsData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Holder holder;
		if (view == null) {
			view = li.inflate(R.layout.all_song_list_item, parent, false);
			holder = setWidgetReference(view);
			view.setTag(holder);

		} else {
			holder = (Holder) view.getTag();
		}
		long duration = Long.parseLong(allSongsData.get(position).get(
				Constants.DURATION));
		Picasso.with(mContext)
				.load(Utils.getAlbumArt(
						position,
						mContext,
						Long.parseLong(allSongsData.get(position).get(
								Constants.ALBUM_ID)), true))
				.error(R.drawable.album_art_not_found).into(holder.ivAlbumArt);

		holder.tvSongName.setText(allSongsData.get(position).get(
				Constants.TITLE)
				+ ".mp3");

		holder.tvSongLength.setText(String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes((long) duration),
				TimeUnit.MILLISECONDS.toSeconds((long) duration)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes((long) duration))));
		return view;
	}

	private Holder setWidgetReference(View view) {
		Holder holder = new Holder();
		holder.ivAlbumArt = (ImageView) view.findViewById(R.id.ivAlbumArt);
		holder.tvSongName = (TextView) view.findViewById(R.id.tvSongName);
		holder.tvSongLength = (TextView) view.findViewById(R.id.tvSongTime);
		return holder;
	}

	public class Holder {
		ImageView ivAlbumArt;
		TextView tvSongName, tvSongLength;
	}

}
