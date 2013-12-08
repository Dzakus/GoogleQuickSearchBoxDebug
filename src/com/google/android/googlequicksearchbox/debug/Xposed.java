package com.google.android.googlequicksearchbox.debug;

import android.app.Application;

import com.google.android.googlequicksearchbox.debug.module.Debug;
import com.google.android.googlequicksearchbox.debug.module.Feature;
import com.google.android.googlequicksearchbox.debug.module.Reminder;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Xposed implements IXposedHookLoadPackage {

	public static Application context;

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals(Common.PKG_NAME_SEARCH))
			return;
		
		XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				context = (Application) param.thisObject;
			}
		});
		
		XSharedPreferences pref = new XSharedPreferences(Common.PKG_NAME);
		Feature.handleLoadPackage(lpparam, pref);
		Debug.handleLoadPackage(lpparam, pref);
		Reminder.handleLoadPackage(lpparam, pref);
	}

}
