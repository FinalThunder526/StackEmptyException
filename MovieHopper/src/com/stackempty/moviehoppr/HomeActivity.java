/**
 * HomeFragment.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.stackempty.moviehopper.R;

public class HomeActivity extends Activity {
	Button submitZipBtn;
	EditText zipEditText;

	public static final String THEATRE_INDEX = "thtr";
	public static final String TMSAPI_KEY = "kcabtt7gmfemuzm949nex5dv";
	public static final int ZIPCODE_KEY = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		submitZipBtn = (Button) findViewById(R.id.submitZip);
		zipEditText = (EditText) findViewById(R.id.zipEditText);
		submitZipBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isZipValid(zipEditText.getText().toString())) {
					Intent intent = new Intent(HomeActivity.this,
							NearbyTheatresActivity.class);
					intent.putExtra(ZIPCODE_KEY + "", zipEditText.getText()
							.toString());
					startActivity(intent);
				} else {
					Log.d("illegal-zip", "Illegal ZIP code");
				}
			}
		});
	}

	/**
	 * Checks if the given zip is valid.
	 * 
	 * @param zip
	 *            the ZIP code to check for
	 */
	private boolean isZipValid(String zip) {
		try {
			int z = Integer.parseInt(zip);
			return (z > 9999 && z < 100000);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// onAttach:
	// ((MainActivity)
	// activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

}
