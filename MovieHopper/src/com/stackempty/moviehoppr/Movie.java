/**
 * Movie.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

public class Movie {
	private static final String MOVIE_PICTURE_URI_TAG = "uri";

	private String mId;
	private String mTitle;
	private String mShortDescription;
	private String mLongDescription;
	private List<String> mGenres;
	private Time mRuntime;
	private List<Showtime> mShowtimes;
	private JSONObject mJSONObj;
	private JSONObject mImage;

	public Movie(JSONObject obj) {
		mJSONObj = obj;
		try {
			mId = obj.getString(TmsApiKeys.MOVIE_ID);
			mTitle = obj.getString(TmsApiKeys.MOVIE_TITLE);
			mShortDescription = obj.getString(TmsApiKeys.MOVIE_DESCRIPTION);
			mLongDescription = obj.getString(TmsApiKeys.MOVIE_LONG_DESC);
			mImage = obj.getJSONObject(TmsApiKeys.IMAGE_KEY);
			mGenres = Data.parseArray(obj.getJSONArray(TmsApiKeys.GENRES_KEY));
		} catch (JSONException e) {
			mId = null;
			/*
			 * mId = "MV696969"; mTitle = "The Joshi Show"; mShortDescription =
			 * "Basically my life. Literally, though."; mGenres = new
			 * ArrayList<String>(); mGenres.add("Romance");
			 * mGenres.add("Erotica"); mGenres.add("Thriller");
			 * mGenres.add("Animated"); mGenres.add("Sports - LOLJK");
			 * mGenres.add("Single-Actor/No-Friends-Actor"); mRuntime = new
			 * Time(); mRuntime.hour = 7; mRuntime.minute = 42; mShowtimes = new
			 * ArrayList<Showtime>(); mShowtimes.add(new Showtime(10, 0, 17,
			 * 0));
			 */
		}
	}

	@Override
	public String toString() {
		return mTitle;
	}

	/**
	 * Gets the movie id.
	 */
	public String getId() {
		return mId;
	}

	public JSONObject getJsonObject() {
		return mJSONObj;
	}

	public String getDescription() {
		return mShortDescription;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getLongDescription() {
		return mLongDescription;
	}

	public List<String> getGenres() {
		return mGenres;
	}

	public JSONObject getImage() {
		return mImage;
	}

	public String getImageUri() {
		try {
			return (String) mImage.get(MOVIE_PICTURE_URI_TAG);
		} catch (JSONException e) {
			return null;
		}
	}
}
