package com.google.android.googlequicksearchbox.debug;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

public class Utils {
	public static String getDefaultSharedPreferencesName() {
		return Common.PKG_NAME + "_preferences";
	}

	public static String[] getMultiPref(SharedPreferences pref, String key, String def_value) {
		String str = pref.getString(key, def_value);
		if (TextUtils.isEmpty(str))
			return new String[] {};
		String[] arr = TextUtils.split(str, Common.SEPERATOR);
		return arr;
	}

	public static List<String> getMultiPrefAsList(SharedPreferences pref, String key, String def_value) {

		return Arrays.asList(getMultiPref(pref, key, def_value));
	}

	public static String getMultiString(Object[] value) {
		return TextUtils.join(Common.SEPERATOR, value);
	}

	public static String getMultiString(List<String> value) {
		return TextUtils.join(Common.SEPERATOR, value.toArray());
	}
	
	public static String BundleToString(Bundle bundle) {
		if (bundle == null)
			return "null";

		StringBuilder sb = new StringBuilder();
		final Set<String> keySet = bundle.keySet();

		for (final String key : keySet) {
			sb.append('\"').append(key).append("\"=\"").append(bundle.get(key)).append("\", ");
		}
		return sb.toString();

	}
}
