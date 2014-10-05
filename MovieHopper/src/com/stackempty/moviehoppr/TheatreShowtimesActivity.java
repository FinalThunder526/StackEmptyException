/**
 * TheatreShowtimesActivity.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.util.ArrayList;
import java.util.List;

import com.stackempty.moviehopper.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class TheatreShowtimesActivity extends Activity {
	TextView theatreNameTv;
	ListView movieListView;
	
	private Boolean[] selectedMovies;
	private Theatre t;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theatreshowtimes);

		int index = getIntent().getIntExtra(HomeActivity.THEATRE_INDEX, 0);
		t = Data.mTheatreList.get(index);

		theatreNameTv = (TextView) findViewById(R.id.theatreName);
		theatreNameTv.setText(t.getName());
		
		movieListView = (ListView) findViewById(R.id.movieList);
		movieListView.setAdapter(new MovieArrayAdapter(this, t.getMovies()));
		
		selectedMovies = new Boolean[t.getMovies().size()];
	}
	
	private class MovieArrayAdapter extends ArrayAdapter<Movie> {
		List<Movie> mMovies;
		Context mContext;
		
		public MovieArrayAdapter(Context context, List<Movie> movies) {
			super(context, R.layout.list_movie, movies);
			mContext = context;
			mMovies = movies;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			final int i = position;

			View rowView = inflater.inflate(R.layout.list_movie, parent, false);
			
			CheckBox x = (CheckBox) rowView.findViewById(R.id.movieTitle);
			x.setText(mMovies.get(position).toString());
			//if(selectedMovies != null)
			//	x.setChecked(selectedMovies[i]);
			x.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					selectedMovies[i] = isChecked;
				}
			});
			return rowView;
		}
	}
}
