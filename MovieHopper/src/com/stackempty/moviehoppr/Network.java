/**
 * Network.java
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
import org.json.JSONTokener;

public class Network {
	public static JSONArray getHttpRequest(String hRequest) {
		BufferedReader in = null;

		try {
			HttpClient hClient = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			URI website = new URI(hRequest);
			request.setURI(website);
			HttpResponse response = hClient.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			String json = in.readLine();
			JSONTokener tokener = new JSONTokener(json);
			return new JSONArray(tokener);
		} catch (Exception e) {
			return null;
		}
	}
}
