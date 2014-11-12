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

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.stackempty.moviehopper.R;

public class NearbyTheatresActivity extends Activity {
	private ListView mTheatreList;
	private ProgressDialog mDialog;
	private MenuItem mRefreshItem;

	private Data d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbytheatres);

		// Set up the list
		mTheatreList = (ListView) findViewById(R.id.theatreList);
		mTheatreList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(NearbyTheatresActivity.this,
						TheatreShowtimesActivity.class);
				intent.putExtra(HomeActivity.THEATRE_INDEX, position);
				startActivity(intent);
			}
		});

		d = new Data(this);
		
		// Gets the current zip code
		Intent intent = getIntent();
		int zip = -1;
		try {
			// If this was a recently submitted search
			zip = Integer.parseInt(intent.getStringExtra(""
					+ HomeActivity.ZIPCODE_KEY));
			d.saveZipCode(zip);
			new GetTheatresTask().execute(zip);
		} catch (Exception e) {
			// Not recently submitted search
			zip = d.getZipCode();
			Data.mTheatreList = d.getTheatres();
			updateListData();
		}

		// ActionBar ab = getActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
	}

	public void onOptionsMenuItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refreshTheatres:
			mRefreshItem = item;
			mRefreshItem.setActionView(R.layout.actionbar_refresh);
			mRefreshItem.expandActionView();
			TestTask t = new TestTask();
			t.execute();
			break;
		}
	}

	/**
	 * Downloads the theatre data from the given ZIP code, accessing the TMS API
	 * endpoint.
	 * 
	 * @author Sarang
	 */
	private class GetTheatresTask extends AsyncTask<Integer, Void, JSONArray> {
		protected void onPreExecute() {
			mDialog = ProgressDialog.show(NearbyTheatresActivity.this, "",
					"Downloading details...");
		}

		@Override
		protected JSONArray doInBackground(Integer... params) {
			String hRequest = Data.baseEndpoint + "theatres?zip=" + params[0]
					+ Data.CLOSE;
			return Network.getHttpRequest(hRequest);
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// OFFLINE: Parse out the JSONArray
			Data.mTheatreList = parseJSONTheatreArray(result);

			updateListData();
			
			d.saveTheatres(Data.mTheatreList);

			mDialog.dismiss();
			// ONLINE: Download individual theatre showtimes
			// new GetMoviesTask().execute(getToday(), "5");
		}
	}

	public void updateListData() {
		ArrayAdapter<String> s = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		for (Theatre t : Data.mTheatreList) {
			s.add(t.getName());
		}
		mTheatreList.setAdapter(s);
	}

	public String getToday() {
		Time x = new Time();
		x.setToNow();
		return x.year + "-" + (x.month < 9 ? "0" : "") + (x.month + 1) + "-"
				+ (x.monthDay < 10 ? "0" : "") + x.monthDay;
	}

	/**
	 * Parses out the given JSONArray into a list of theatres.
	 * 
	 * @param theatres
	 *            a JSONArray retrieved from the TMS API endpoint
	 * @return the parsed-out list of Theatre objects
	 */
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

	private class TestTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... params) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			mRefreshItem.collapseActionView();
			mRefreshItem.setActionView(null);
		}
	}
}
