/**
 * TheatreShowtimesActivity.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.stackempty.moviehopper.R;

public class TheatreShowtimesActivity extends Activity {
	TextView mTheatreNameTv;
	ListView mMovieListView;

	private int mIndex;
	private Theatre t;

	ProgressDialog mDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theatreshowtimes);

		mIndex = getIntent().getIntExtra(HomeActivity.THEATRE_INDEX, 0);
		t = Data.mTheatreList.get(mIndex);

		mMovieListView = (ListView) findViewById(R.id.movieList);
		
		mTheatreNameTv = (TextView) findViewById(R.id.theatreName);
		mTheatreNameTv.setText(t.getName());

		Time t = new Time();
		t.setToNow();

		String startDate = "" + t.year + "-"
				+ ((t.month < 9) ? "0" + (t.month + 1) : t.month + 1) + "-"
				+ ((t.monthDay < 10) ? "0" + t.monthDay : t.monthDay);
		String numDays = "5";
		new GetMoviesTask().execute(startDate, numDays);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		Menu actionBar = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tshowtimes, actionBar);
		return super.onCreateOptionsMenu(actionBar);
	}

	/**
	 * 0 = startDate yyyy-mm-dd<br>
	 * 1 = numDays
	 * 
	 * @author Sarang
	 * 
	 */
	private class GetMoviesTask extends AsyncTask<String, Void, JSONArray[]> {
		@Override
		protected void onPreExecute() {
			mDialog = ProgressDialog.show(TheatreShowtimesActivity.this, "",
					"Downloading movies...");
		}

		@Override
		protected JSONArray[] doInBackground(String... params) {
			JSONArray[] movieCollection = new JSONArray[Data.mTheatreList
					.size()];
			for (int i = 0; i < Data.mTheatreList.size(); i++) {
				String hRequest = Data.baseEndpoint + "theatres/"
						+ Data.mTheatreList.get(i).getId()
						+ "/showings?startDate=" + params[0] + "&numDays="
						+ params[1] + Data.CLOSE;
				movieCollection[i] = Network.getHttpRequest(hRequest);
			}
			return movieCollection;
		}

		@Override
		protected void onPostExecute(JSONArray[] result) {
			t.setMovies(parseJSONMovieArray(result[mIndex]));

			ArrayAdapter<String> a = new ArrayAdapter<String>(TheatreShowtimesActivity.this, android.R.layout.simple_list_item_1);
			for(Movie m : t.getMovies()) {
				a.add(m.toString());
			}
			mMovieListView.setAdapter(a);
			
			mDialog.dismiss();

			// mExpListAdapter = new
			// ExpandableListAdapter(NearbyTheatresActivity.this);
			// mExpListView.setAdapter(mExpListAdapter);
		}
	}

	/**
	 * Parses a JSONArray of movies into a List of Movie objects.
	 * 
	 * @param movies
	 *            a JSONArray of movies based on the OnConnect API's.
	 * @return a list of Movie objects with corresponding data fields.
	 */
	private List<Movie> parseJSONMovieArray(JSONArray movies) {
		List<Movie> movieArray = new ArrayList<Movie>();
		for (int i = 0; i < movies.length(); i++) {
			try {
				JSONObject obj = movies.getJSONObject(i);
				movieArray.add(new Movie(obj));
			} catch (JSONException e) {
				movieArray.add(null);
			}
		}
		return movieArray;
	}

	private class MovieArrayAdapter extends ArrayAdapter<Movie> {
		List<Movie> mMovies;
		Context mContext;

		public MovieArrayAdapter(Context context, List<Movie> movies) {
			super(context, R.layout.list_movie, movies);
			mContext = context;
			mMovies = movies;
		}
	}
}
