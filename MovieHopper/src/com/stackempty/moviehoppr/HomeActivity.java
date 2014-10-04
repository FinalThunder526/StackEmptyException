/**
 * HomeFragment.java
 * Oct 4, 2014
 * Sarang Joshi
 */

package com.stackempty.moviehoppr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.stackempty.moviehopper.R;

public class HomeActivity extends Activity {
	Button submitZipBtn;
	EditText zipEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		submitZipBtn = (Button) findViewById(R.id.submitZip);
		zipEditText = (EditText) findViewById(R.id.zipEditText);
		submitZipBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, NearbyTheatresActivity.class);
				intent.putExtra(MainActivity.ZIPCODE_KEY + "", zipEditText
						.getText().toString());
				startActivity(intent);
			}
		});
	}

	// onAttach:
	// ((MainActivity)
	// activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

}
