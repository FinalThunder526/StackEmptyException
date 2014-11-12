/**
 * Data.java
 * Oct 5, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

public class Data {
	public static List<Theatre> mTheatreList;
	public static String baseEndpoint = "http://data.tmsapi.com/v1/";
	public static final String CLOSE = "&api_key=" + HomeActivity.TMSAPI_KEY;

	public static final String NEARBY_THEATRES_FILE = "nt_file";
	public static final String NEARBY_THEATRES_PREF = "nt_pref";

	public static final String ZIP_KEY = "zip-key";

	private Context mContext;
	private SharedPreferences mPref;

	public Data(Context context) {
		mContext = context;
		mPref = mContext.getSharedPreferences(NEARBY_THEATRES_PREF,
				Context.MODE_PRIVATE);
	}

	/**
	 * Saves the given list of theatres. Usually {@link #mTheatreList}.
	 * 
	 * @param theatres
	 *            the list of theatres for the current zip code
	 * @return whether the save was successful
	 */
	public boolean saveTheatres(List<Theatre> theatres) {
		try {
			FileOutputStream fos = mContext.openFileOutput(
					NEARBY_THEATRES_FILE, Context.MODE_PRIVATE);
			for (Theatre t : theatres) {
				fos.write(t.toString().getBytes());
				fos.write("\n".getBytes());
			}
			fos.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Gets the currently saved theatres.
	 */
	public List<Theatre> getTheatres() {
		List<Theatre> theatres = new ArrayList<Theatre>();

		try {
			FileInputStream fis = mContext.openFileInput(NEARBY_THEATRES_FILE);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			String readString = null;
			while ((readString = br.readLine()) != null) {
				// Parse out the saved string
				int space = readString.indexOf(' ');
				String id = readString.substring(0, space);
				String name = readString.substring(space);
				theatres.add(new Theatre(id, name));
			}

			isr.close();
		} catch (IOException e) {
			return null;
		}

		return theatres;
	}

	/**
	 * Gets the current zip code that has been searched by the user.
	 */
	public int getZipCode() {
		return mPref.getInt(ZIP_KEY, -1);
	}

	public boolean saveZipCode(int zip) {
		return mPref.edit().putInt(ZIP_KEY, zip).commit();
	}
}
