package com.google.android.googlequicksearchbox.debug;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SettingsReceiver extends BroadcastReceiver {
	private static final String TAG = SettingsReceiver.class.getSimpleName();
	public static final String ACTION_SAVE_SETTINGS = Common.PKG_NAME + ".save";
	public static final String EXTRA_KNOW_FEATURE = Common.KEY_KNOW_FEATURE;
	public static final String EXTRA_UNLOCKED_FEATURE = Common.KEY_UNLOCK_FEATURE;

	public SettingsReceiver() {

	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles" })
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onReceiver");
		Log.i(TAG, "action : " + intent.getAction());
		if (intent.getAction().equals(ACTION_SAVE_SETTINGS)) {
			Log.i(TAG, "extra : " + Utils.BundleToString(intent.getExtras()));

			SharedPreferences pref = context.getSharedPreferences(Utils.getDefaultSharedPreferencesName(),
					Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

			Editor editor = pref.edit();
			
			if (intent.hasExtra(EXTRA_KNOW_FEATURE)) {
				String data = intent.getStringExtra(EXTRA_KNOW_FEATURE);
				editor.putString(Common.KEY_KNOW_FEATURE, data);
				
				Log.i(TAG, "know : " + data);
			}
			if (intent.hasExtra(EXTRA_UNLOCKED_FEATURE)) {
				String data = intent.getStringExtra(EXTRA_UNLOCKED_FEATURE);
				editor.putString(Common.KEY_UNLOCK_FEATURE, data);
				
				Log.i(TAG, "unlock : " + data);
			}

			editor.apply();
		}
	}

}
