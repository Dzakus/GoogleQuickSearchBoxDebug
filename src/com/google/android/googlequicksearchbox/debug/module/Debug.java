package com.google.android.googlequicksearchbox.debug.module;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.android.googlequicksearchbox.debug.Common;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Debug {
	public static final String CLASS_SHARED_PREF_PROTO_OLD = "com.google.android.searchcommon.preferences.SharedPreferencesProto";
	public static final String CLASS_SHARED_PREF_PROTO = "com.google.android.search.core.preferences.SharedPreferencesProto";
	public static final String FILE_STARTUP_SETTINGS = "StartupSettings.bin";
	public static final String KEY_DEBUG_LEVEL = "debug_features_level";
	
	public static void handleLoadPackage(LoadPackageParam lpparam, final XSharedPreferences pref) {

		pref.getString(Common.KEY_DEBUG_LEVEL, Common.DEF_DEBUG_LEVEL);
		Class<?> SharedPrefClass;
		try {
			SharedPrefClass = XposedHelpers.findClass(CLASS_SHARED_PREF_PROTO, lpparam.classLoader);
		} catch (Exception e) {
			SharedPrefClass = XposedHelpers.findClass(CLASS_SHARED_PREF_PROTO_OLD, lpparam.classLoader);
		}

		XposedHelpers.findAndHookMethod(SharedPrefClass, "commitLoadedMapLocked", Map.class, new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				File mFile = (File) XposedHelpers.getObjectField(param.thisObject, "mFile");
				// XposedBridge.log("mFile = " + mFile.getName());
				if (!mFile.getName().equals(FILE_STARTUP_SETTINGS))
					return;
				@SuppressWarnings("unchecked")
				HashMap<String, Object> mMap = (HashMap<String, Object>) XposedHelpers.getObjectField(param.thisObject,
						"mMap");
				Integer value = (Integer) mMap.get(KEY_DEBUG_LEVEL);
				
				XposedBridge.log(KEY_DEBUG_LEVEL + " = " + value);
				
				mMap.put(KEY_DEBUG_LEVEL,
						Integer.parseInt(pref.getString(Common.KEY_DEBUG_LEVEL, Common.DEF_DEBUG_LEVEL)));
			}

		});
	}
}
