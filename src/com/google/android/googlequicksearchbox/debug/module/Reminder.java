package com.google.android.googlequicksearchbox.debug.module;

import com.google.android.googlequicksearchbox.debug.Common;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Reminder {
	@SuppressWarnings("unused")
	private static final String TAG = Reminder.class.getSimpleName();
	private static final String CLASS_UI_MODE_MANAGER = "com.google.android.sidekick.main.inject.DefaultSidekickInjector";

	public static void handleLoadPackage(LoadPackageParam lpparam, final XSharedPreferences pref) {
		if (!pref.getBoolean(Common.KEY_ENABLE_REMINDER, Common.DEF_ENABLE_REMINDER))
			return;
		
		Class<?> UiModeManagerClass = XposedHelpers.findClass(CLASS_UI_MODE_MANAGER, lpparam.classLoader);

		XposedBridge.hookAllMethods(UiModeManagerClass, "areRemindersEnabled", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				param.setResult(true);
			}
		});

	}
}
