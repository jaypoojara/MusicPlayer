package com.tagtask.musicplayer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.constants.Constants;
import com.tagtask.musicplayer.utils.Utils;

public class SongChangeViewPagerAdapter extends PagerAdapter {
	Context mContext;

	public SongChangeViewPagerAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return Constants.playList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView albumArt = new ImageView(mContext);
		albumArt.setImageBitmap(Utils.getAlbumArt(
				position,
				mContext,
				Long.parseLong(Constants.playList.get(position).get(
						Constants.ALBUM_ID))));
		Picasso.with(mContext)
				.load(Utils.getAlbumArt(
						position,
						mContext,
						Long.parseLong(Constants.playList.get(position).get(
								Constants.ALBUM_ID)), true))
				.error(R.drawable.album_art_not_found).into(albumArt);
		((ViewPager) container).addView(albumArt, 0);
		return albumArt;
	}

	@Override
	public boolean isViewFromObject(View container, Object obj) {
		return container == obj;
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}
}
