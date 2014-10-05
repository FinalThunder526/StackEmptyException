/**
 * NearbyTheatersFragment.java
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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.stackempty.moviehopper.R;

public class NearbyTheatresActivity extends Activity {
	String baseEndpoint = "http://data.tmsapi.com/v1/";
	public static final String CLOSE = "&api_key=" + MainActivity.TMSAPI_KEY;

	private List<Theatre> mTheatreList;

	private ExpandableListAdapter mExpListAdapter;
	private ExpandableListView mExpListView;

	ProgressDialog pd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbytheatres);

		mExpListView = (ExpandableListView) findViewById(R.id.theatreList);

		Intent intent = getIntent();
		int zip = Integer.parseInt(intent.getStringExtra(""
				+ MainActivity.ZIPCODE_KEY));

		new GetTheatresTask().execute(zip);
	}

	/**
	 * Downloads the theatre data from the given ZIP code, accessing the TMS API
	 * endpoint.
	 * 
	 * @author Sarang
	 */
	private class GetTheatresTask extends AsyncTask<Integer, Void, JSONArray> {
		protected void onPreExecute() {
			pd = ProgressDialog.show(NearbyTheatresActivity.this, "",
					"Downloading details...");
		}

		@Override
		protected JSONArray doInBackground(Integer... params) {
			String hRequest = baseEndpoint + "theatres?zip=" + params[0]
					+ CLOSE;
			return Network.getHttpRequest(hRequest);
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// OFFLINE: Parse out the JSONArray
			mTheatreList = parseJSONTheatreArray(result);

			// ONLINE: Download individual theatre showtimes
			new GetMoviesTask().execute(getToday(), "5");
		}
	}

	/**
	 * 0 = startDate<br>
	 * 1 = numDays
	 * 
	 * @author Sarang
	 * 
	 */
	private class GetMoviesTask extends AsyncTask<String, Void, JSONArray[]> {
		@Override
		protected JSONArray[] doInBackground(String... params) {
			JSONArray[] movieCollection = new JSONArray[mTheatreList.size()];
			for (int i = 0; i < mTheatreList.size(); i++) {
				String hRequest = baseEndpoint + "theatres/"
						+ mTheatreList.get(i).getId() + "/showings?startDate="
						+ params[0] + "&numDays=" + params[1] + CLOSE;
				movieCollection[i] = Network.getHttpRequest(hRequest);
			}
			return movieCollection;
		}

		protected void onPostExecute(JSONArray[] result) {
			for (int i = 0; i < result.length; i++) {
				mTheatreList.get(i).setMovies(parseJSONMovieArray(result[i]));
			}
			pd.dismiss();

			mExpListAdapter = new ExpandableListAdapter(
					NearbyTheatresActivity.this);
			mExpListView.setAdapter(mExpListAdapter);
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
		try {
			for (int i = 0; i < movies.length(); i++) {
				JSONObject obj = movies.getJSONObject(i);
				movieArray.add(new Movie(obj));
			}
		} catch (JSONException e) {
			return null;
		}
		return movieArray;
	}

	public String getToday() {
		Time x = new Time();
		x.setToNow();
		return x.year + "-" + (x.month < 9 ? "0" : "") + (x.month + 1) + "-"
				+ (x.monthDay < 10 ? "0" : "") + x.monthDay;
	}

	private List<Theatre> parseJSONTheatreArray(JSONArray theatres) {
		List<Theatre> theatreArray = new ArrayList<Theatre>();
		try {
			for (int i = 0; i < theatres.length(); i++) {
				JSONObject obj = theatres.getJSONObject(i);
				theatreArray.add(new Theatre(obj));
			}
		} catch (JSONException e) {
			return null;
		}
		return theatreArray;
	}

	private class ExpandableListAdapter extends BaseExpandableListAdapter {
		Context mContext;

		public ExpandableListAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getGroupCount() {
			// how many theatres are nearby
			return mTheatreList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// how many movies are in the given theatre
			return (mTheatreList.get(groupPosition)).getMovies().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mTheatreList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return mTheatreList.get(groupPosition).getMovies()
					.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String theatreName = ((Theatre) getGroup(groupPosition)).getName();
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) this.mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_group, null);
			}

			TextView lblListHeader = (TextView) convertView
					.findViewById(R.id.tvListHeader);
			lblListHeader.setTypeface(null, Typeface.BOLD);
			lblListHeader.setText(theatreName);

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			final String movieName = ((Movie) getChild(groupPosition,
					childPosition)).toString();

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) this.mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item, null);
			}

			TextView txtListChild = (TextView) convertView
					.findViewById(R.id.tvListItem);

			txtListChild.setText(movieName);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}
}
