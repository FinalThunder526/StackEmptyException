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

public class HomeFragment extends Fragment {
	Button submitZipBtn;
	EditText zipEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		submitZipBtn = (Button) rootView.findViewById(R.id.submitZip);
		zipEditText = (EditText) rootView.findViewById(R.id.zipEditText);
		submitZipBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(MainActivity.ZIPCODE_KEY + "", zipEditText
						.getText().toString());
				((MainActivity) getActivity()).onActivityResult(
						MainActivity.ZIPCODE_KEY, Activity.RESULT_OK, intent);
			}
		});
		return rootView;
	}

	// onAttach:
	// ((MainActivity)
	// activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

}
