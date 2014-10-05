/**
 * Movie.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

public class Movie {
	private String mId;
	private String mTitle;
	private String mShortDescription;
	private List<String> mGenres;
	private Time mRuntime;
	private List<Showtime> mShowtimes;

	public Movie(JSONObject obj) {
		try {
			mId = obj.getString(TmsApiKeys.MOVIE_ID);
			mTitle = obj.getString(TmsApiKeys.MOVIE_TITLE);
			mShortDescription = obj.getString(TmsApiKeys.MOVIE_DESCRIPTION);
		} catch (JSONException e) {
			mId = "MV696969";
			mTitle = "The Joshi Show";
			mShortDescription = "Basically my life. Literally, though.";
			mGenres = new ArrayList<String>();
			mGenres.add("Romance");
			mGenres.add("Erotica");
			mGenres.add("Thriller");
			mGenres.add("Animated");
			mGenres.add("Sports - LOLJK");
			mGenres.add("Single-Actor/No-Friends-Actor");
			mRuntime = new Time();
			mRuntime.hour = 7;
			mRuntime.minute = 42;
			mShowtimes = new ArrayList<Showtime>();
			mShowtimes.add(new Showtime(10, 0, 17, 0));
		}
	}

	@Override
	public String toString() {
		return mTitle;
	}
}
