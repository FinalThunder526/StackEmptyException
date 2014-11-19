/**
 * Theatre.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Encapsulates the details of a theatre.
 * 
 * @author Sarang
 */
public class Theatre {
	private List<Movie> mMovies;
	private String mName;
	private int mId;

	public Theatre(JSONObject obj) {
		try {
			mName = obj.getString(TmsApiKeys.THEATRE_NAME);
			mId = Integer.parseInt((String) obj.get(TmsApiKeys.THEATRE_ID));
		} catch (JSONException e) {
			mName = "Joshi & Liu Theatres Inc.";
			mId = 420420;
		}
	}

	public Theatre(String id, String name) {
		try {
			int i = Integer.parseInt(id);
			mId = i;
			mName = name;
		} catch (Exception e) {
		}
	}

	public Theatre(int id, String name) {
		mId = id;
		mName = name;
	}

	/**
	 * Gets the name of this theatre.
	 */
	public String getName() {
		return mName;
	}

	public List<Movie> getMovies() {
		return mMovies;
	}

	public void setMovies(List<Movie> list) {
		mMovies = new ArrayList<Movie>();
		for (int i = 0; i < list.size(); i++) {
			Movie m = list.get(i);
			if (m != null)
				mMovies.add(list.get(i));
		}
	}

	public int getId() {
		return mId;
	}

	@Override
	public String toString() {
		return mId + " " + mName;
	}

	public Movie getMovie(int movieN) {
		return mMovies.get(movieN);
	}
}
