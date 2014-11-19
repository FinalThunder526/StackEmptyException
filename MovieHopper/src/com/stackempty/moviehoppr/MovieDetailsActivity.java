/**
 * MovieDetailsActivity.java
 * Nov 14, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import com.stackempty.moviehopper.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MovieDetailsActivity extends Activity {
	public static final String THEATRE_N_KEY = "theatre-n";
	public static final String MOVIE_N_KEY = "movie-n";

	private Movie mMovie;

	TextView mMovieTitle;
	TextView mMovieDescription;
	ListView mGenres;

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

		mMovieTitle = (TextView) findViewById(R.id.movieDetailsName);
		mMovieTitle.setText(mMovie.getTitle());
		mMovieDescription = (TextView) findViewById(R.id.movieDetailsDescription);
		mMovieDescription.setText(mMovie.getLongDescription());
		mGenres = (ListView) findViewById(R.id.movieDetailsGenreList);
		mGenres.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mMovie.getGenres()));
	}
}
