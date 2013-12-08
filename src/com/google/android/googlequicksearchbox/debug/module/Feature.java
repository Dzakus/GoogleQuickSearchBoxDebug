package com.google.android.googlequicksearchbox.debug.module;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.util.Log;

import com.google.android.googlequicksearchbox.debug.Common;
import com.google.android.googlequicksearchbox.debug.SettingsReceiver;
import com.google.android.googlequicksearchbox.debug.Utils;
import com.google.android.googlequicksearchbox.debug.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Feature {
	private static final String CLASS_FEATURE = "com.google.android.search.core.Feature";

	private static final String TAG = Feature.class.getSimpleName();
	private static boolean sEnableHook = false;
	private static List<String> sKnowKey = new ArrayList<String>();
	private static List<String> sUnlockedKey = new ArrayList<String>();

	public static void handleLoadPackage(LoadPackageParam lpparam, final XSharedPreferences pref) {
		sEnableHook = pref.getBoolean(Common.KEY_ENABLE_FEATURE_HOOK, Common.DEF_ENABLE_FEATURE_HOOK);

		sKnowKey.addAll(Utils.getMultiPrefAsList(pref, Common.KEY_KNOW_FEATURE, Common.DEF_KNOW_FEATURE));
		sUnlockedKey.addAll(Utils.getMultiPrefAsList(pref, Common.KEY_UNLOCK_FEATURE, Common.DEF_UNLOCKED_FEATURE));

		Class<?> FreaturesClass = XposedHelpers.findClass(CLASS_FEATURE, lpparam.classLoader);
		// ctor_dec: private
		// com.google.android.search.core.Feature(java.lang.String,int,boolean)
		Constructor<?> FreatureCtor = XposedHelpers.findConstructorExact(FreaturesClass, String.class, int.class,
				boolean.class);
		XposedBridge.hookMethod(FreatureCtor, new XC_MethodHook() {

			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				//XposedBridge.log(Arrays.toString(param.args));
				String key = (String) param.args[0];
				// int id = (Integer) param.args[1];
				boolean enabled = (Boolean) param.args[2];
				// param.args[2] = true;
				if (!sKnowKey.contains(key)) {
					Log.i(TAG, "Unknown " + key);
					notifyNewFreatue(key, enabled);
				} else if (sEnableHook) {
					if (sUnlockedKey.contains(key)) {
						param.args[2] = true;
					} else {
						param.args[2] = false;
					}
				}
			}
		});
	}

	private static void notifyNewFreatue(String key, boolean enabled) {
		if (Xposed.context != null) {
			sKnowKey.add(key);

			Intent i = new Intent(SettingsReceiver.ACTION_SAVE_SETTINGS);
			i.putExtra(SettingsReceiver.EXTRA_KNOW_FEATURE, Utils.getMultiString(sKnowKey));

			if (enabled) {
				sUnlockedKey.add(key);
				i.putExtra(SettingsReceiver.EXTRA_UNLOCKED_FEATURE, Utils.getMultiString(sUnlockedKey));
			}

			i.setPackage(Common.PKG_NAME);
			Xposed.context.sendOrderedBroadcast(i, null);
			Log.i(TAG, "notifyNewFreatue");
		}
	}
}
