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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONTokener;

import com.stackempty.moviehopper.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NearbyTheatersFragment extends Fragment {
	String baseEndpoint = "http://data.tmsapi.com/v1/";

	public static NearbyTheatersFragment newInstance(int zipCode) {
		NearbyTheatersFragment fragment = new NearbyTheatersFragment();
		Bundle args = new Bundle();
		args.putInt(MainActivity.ZIPCODE_KEY + "", zipCode);
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nearbytheaters,
				container, false);
		int zip = this.getArguments().getInt(MainActivity.ZIPCODE_KEY + "");
		new GetTheatresTask().execute(zip);
		return rootView;
	}

	private class GetTheatresTask extends AsyncTask<Integer, Void, Boolean> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), "Loading", "Loading");
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			return getTheaters(params[0]);
		}

		private boolean getTheaters(int zip) {
			String hRequest = "http://data.tmsapi.com/v1/theatres?zip=" + zip + "&api_key=" + MainActivity.TMSAPI_KEY;

			BufferedReader in = null;

			try {
				HttpClient hClient = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				URI website = new URI(hRequest);
				request.setURI(website);

				//HttpParams params = new BasicHttpParams();
				//params.setParameter("zip", zip + "");
				//params.setParameter("api_key", MainActivity.TMSAPI_KEY);
				//request.setParams(params);
				
				HttpResponse response = hClient.execute(request);
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = in.readLine()) != null;) {
					builder.append(line).append("\n");
				}
				JSONTokener tokener = new JSONTokener(builder.toString());
				JSONArray finalResult = new JSONArray(tokener);
				// Object obj = JSONValue.parse(in.toString());

			} catch (Exception e) {
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
		}
	}

}
