package com.google.android.googlequicksearchbox.debug.preference;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.googlequicksearchbox.debug.Common;
import com.google.android.googlequicksearchbox.debug.Utils;

@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles" })
public class FeaturePreference extends MultiSelectListPreference {

	private static final String TAG = FeaturePreference.class.getSimpleName();
	private SharedPreferences mPref;
	private String[] mKnowKey;
	private String[] mUnlockedKey;

	@SuppressWarnings("deprecation")
	public FeaturePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPref = getContext().getSharedPreferences(Utils.getDefaultSharedPreferencesName(),
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		Log.i(TAG, "pref = " + mPref.getAll().toString());

	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		Log.i(TAG, "pref = " + mPref.getAll().toString());
		mKnowKey = Utils.getMultiPref(mPref, Common.KEY_KNOW_FEATURE, Common.DEF_KNOW_FEATURE);
		mUnlockedKey = Utils.getMultiPref(mPref, Common.KEY_UNLOCK_FEATURE, Common.DEF_UNLOCKED_FEATURE);
		
		setEntries(mKnowKey);
		setEntryValues(mKnowKey);

		return super.onCreateView(parent);
	}

	@Override
	public void setValues(Set<String> values) {
		Editor edit = mPref.edit();
		Log.i(TAG, "values=" + values);
		String[] arr = (String[]) values.toArray(new String[] {});
		edit.putString(Common.KEY_UNLOCK_FEATURE, Utils.getMultiString(arr));
		Log.i(TAG, "setValues=" + Arrays.deepToString(arr));
		edit.apply();
		super.setValues(values);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		List<String> list = Arrays.asList(mUnlockedKey);
		HashSet<String> set = new HashSet<String>(list);
		super.onSetInitialValue(restoreValue, set);
	}
}
