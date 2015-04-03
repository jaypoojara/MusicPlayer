package com.tagtask.musicplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.constants.Constants;

public class ArtistGridViewAdapter extends BaseAdapter {
	Context context;
	LayoutInflater li;
	ArrayList<HashMap<String, String>> allArtistSong;

	public ArtistGridViewAdapter(Context context,
			ArrayList<HashMap<String, String>> data) {
		this.context = context;
		li = LayoutInflater.from(context);
		allArtistSong = data;

	}

	@Override
	public int getCount() {
		return allArtistSong.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Holder holder;
		if (view == null) {
			view = li.inflate(R.layout.album_item, parent, false);
			holder = new Holder();
			holder.ivAlbumArt = (ImageView) view
					.findViewById(R.id.ivAlbumArtAlbumFragment);
			holder.tvAlbumName = (TextView) view.findViewById(R.id.tvAlbumName);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		Picasso.with(context).load(R.drawable.cloud_music).error(R.drawable.album_art_not_found)
				.into(holder.ivAlbumArt);

		holder.tvAlbumName.setText(allArtistSong.get(position).get(
				Constants.ARTIST));
		return view;
	}

	public class Holder {
		ImageView ivAlbumArt;
		TextView tvAlbumName;
	}
}
