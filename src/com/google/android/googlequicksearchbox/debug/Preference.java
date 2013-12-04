package com.google.android.googlequicksearchbox.debug;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preference extends PreferenceActivity
{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		addPreferencesFromResource(R.xml.preference);
	}

}
