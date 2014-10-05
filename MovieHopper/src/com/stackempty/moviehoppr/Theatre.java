/**
 * Theatre.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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

	public String getName() {
		return mName;
	}

	public List<Movie> getMovies() {
		return mMovies;
	}

	public void setMovies(List<Movie> list) {
		mMovies = list;
	}

	public int getId() {
		return mId;
	}
}
