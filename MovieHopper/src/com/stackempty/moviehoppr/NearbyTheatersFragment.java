/**
 * NearbyTheatersFragment.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.stackempty.moviehopper.R;

public class NearbyTheatersFragment extends ListFragment {
	String baseEndpoint = "http://data.tmsapi.com/v1/";

	String[] theatreStringArray;

	public static NearbyTheatersFragment newInstance(int zipCode) {
		NearbyTheatersFragment fragment = new NearbyTheatersFragment();
		Bundle args = new Bundle();
		args.putInt(MainActivity.ZIPCODE_KEY + "", zipCode);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nearbytheaters,
				container, false);

		Bundle args = getArguments();
		int zip = args.getInt(MainActivity.ZIPCODE_KEY + "");
		// ONLINE: Downloads theatre
		new GetTheatresTask().execute(zip);

		return rootView;
	}

	/**
	 * Downloads the theatre data from the given ZIP code, accessing the TMS API
	 * endpoint.
	 * 
	 * @author Sarang
	 */
	private class GetTheatresTask extends AsyncTask<Integer, Void, JSONArray> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), "",
					"Downloading information...");
		}

		@Override
		protected JSONArray doInBackground(Integer... params) {
			return getTheaters(params[0]);
		}

		private JSONArray getTheaters(int zip) {
			String hRequest = "http://data.tmsapi.com/v1/theatres?zip=" + zip
					+ "&api_key=" + MainActivity.TMSAPI_KEY;

			BufferedReader in = null;

			try {
				HttpClient hClient = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				URI website = new URI(hRequest);
				request.setURI(website);

				// HttpParams params = new BasicHttpParams();
				// params.setParameter("zip", zip + "");
				// params.setParameter("api_key", MainActivity.TMSAPI_KEY);
				// request.setParams(params);

				HttpResponse response = hClient.execute(request);
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				String json = in.readLine();
				JSONTokener tokener = new JSONTokener(json);
				return new JSONArray(tokener);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			pd.dismiss();
			// OFFLINE: Parses theatre data into ListView
			new LoadTheatreDetails().execute(result);
		}
	}

	/**
	 * Loads the downloaded theatre details (parameterized as a JSONArray) to
	 * the Fragment's ListView.
	 * 
	 * @author Sarang
	 */
	private class LoadTheatreDetails extends
			AsyncTask<JSONArray, Void, Boolean> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), "",
					"Loading information...");
		}

		@Override
		protected Boolean doInBackground(JSONArray... params) {
			JSONArray myArray = params[0];
			String[] stringArray = new String[myArray.length()];
			try {
				for (int i = 0; i < myArray.length(); i++) {
					JSONObject obj = myArray.getJSONObject(i);
					String s = obj.getString("name");
					s += obj.getString("theatreId");
					stringArray[i] = s;
				}
			} catch (JSONException e) {
				return false;
			}

			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, stringArray));

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			Toast.makeText(getActivity(), "Theatre loading success: " + result,
					Toast.LENGTH_SHORT).show();
		}
	}
}
