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
import com.tagtask.musicplayer.utils.Utils;

public class AlbumGridviewAdapter extends BaseAdapter {
	Context context;
	LayoutInflater li;
	ArrayList<HashMap<String, String>> allAlbums;

	public AlbumGridviewAdapter(Context context,
			ArrayList<HashMap<String, String>> data) {
		this.context = context;
		li = LayoutInflater.from(context);
		allAlbums = data;

	}

	@Override
	public int getCount() {
		return allAlbums.size();
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
		Picasso.with(context)
				.load(Utils.getAlbumArt(
						position,
						context,
						Long.parseLong(allAlbums.get(position).get(
								Constants.ALBUM_ID)), true))
				.error(R.drawable.album_art_not_found).into(holder.ivAlbumArt);
		holder.tvAlbumName
				.setText(allAlbums.get(position).get(Constants.ALBUM));
		return view;
	}

	public class Holder {
		ImageView ivAlbumArt;
		TextView tvAlbumName;
	}
}
