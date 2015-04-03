package com.tagtask.musicplayer.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.tagtask.musicplayer.R;
import com.tagtask.musicplayer.constants.Constants;

public class Utils {
	public static Bitmap getAlbumArt(int position, Context mContext,
			long albumId) {
		Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
		Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
		Bitmap bitmap = null;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(
					mContext.getContentResolver(), albumArtUri);

		} catch (FileNotFoundException | NullPointerException e) {
			e.printStackTrace();
			bitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.album_art_not_found);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return bitmap;
	}

	public static Uri getAlbumArt(int position, Context mContext, long albumId,
			boolean isUriNeeded) {
		Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
		Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
		return albumArtUri;
	}
}
