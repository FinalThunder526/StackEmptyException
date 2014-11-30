/**
 * MovieDetailsActivity.java
 * Nov 14, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.io.*;
import java.net.*;

import com.stackempty.moviehopper.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MovieDetailsActivity extends Activity {
	public static final String THEATRE_N_KEY = "theatre-n";
	public static final String MOVIE_N_KEY = "movie-n";

	private Movie mMovie;

	TextView mMovieTitle;
	TextView mMovieDescription;
	ListView mGenres;
	ImageView mMoviePicture;

	// Movie image URL format:
	// "http://developer.tmsimg.com/" + uri + "?api_key=" + key

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_moviedetails);

		Intent i = getIntent();
		int theatreN = i.getIntExtra(THEATRE_N_KEY, 0);
		int movieN = i.getIntExtra(MOVIE_N_KEY, 0);
		mMovie = Data.mTheatreList.get(theatreN).getMovie(movieN);

		if (mMovie != null) {
			mMovieTitle = (TextView) findViewById(R.id.movieDetailsName);
			mMovieTitle.setText(mMovie.getTitle());
			mMovieDescription = (TextView) findViewById(R.id.movieDetailsDescription);
			mMovieDescription.setText(mMovie.getLongDescription());
			mGenres = (ListView) findViewById(R.id.movieDetailsGenreList);
			mGenres.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mMovie.getGenres()));
			mMoviePicture = (ImageView) findViewById(R.id.movieDetailsPicture);
			String uri = mMovie.getImageUri();
			if (uri != null)
				mMoviePicture.setTag(getImageUrl(uri));

			new LoadImageTask().execute(mMoviePicture);
		}
	}

	private String getImageUrl(String uri) {
		return "http://developer.tmsimg.com/" + uri
				+ "?api_key=" + HomeActivity.TMSAPI_KEY;
	}

	private class LoadImageTask extends AsyncTask<ImageView, Void, Bitmap> {
		ImageView view = null;

		public Bitmap doInBackground(ImageView... params) {
			view = params[0];
			return downloadImage((String) view.getTag());
		}

		public void onPostExecute(Bitmap result) {
			view.setImageBitmap(result);
		}

		private Bitmap downloadImage(String url) {
			// ---------------------------------------------------
			Bitmap bm = null;
			try {
				URL aURL = new URL(url);
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				bm = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
			} catch (IOException e) {
				Log.e("Hub", "Error getting the image from server : "
						+ e.getMessage().toString());
			}
			return bm;
			// ---------------------------------------------------
		}
	}
}
