package com.google.android.googlequicksearchbox.debug;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class module implements IXposedHookLoadPackage {
	public static final String FILE_STARTUP_SETTINGS = "StartupSettings.bin";

	public static final String CLASS_SHARED_PREF_PROTO_OLD = "com.google.android.searchcommon.preferences.SharedPreferencesProto";
	public static final String CLASS_SHARED_PREF_PROTO = "com.google.android.search.core.preferences.SharedPreferencesProto";

	public static final String CLASS_REMOTE_API_1 = "com.google.android.apps.sidekick.remoteapi.GoogleNowRemoteService$1";

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals(Common.PACKAGE_NAME))
			return;
		final XSharedPreferences pref = new XSharedPreferences(module.class.getPackage().getName());
		pref.getString(Common.KEY_DEBUG_LEVEL, Common.DEF_DEBUG_LEVEL);
		Class<?> SharedPrefClass;
		try {
			SharedPrefClass = XposedHelpers.findClass(CLASS_SHARED_PREF_PROTO, lpparam.classLoader);
		} catch (Exception e) {
			SharedPrefClass = XposedHelpers.findClass(CLASS_SHARED_PREF_PROTO_OLD, lpparam.classLoader);
		}
		// final Class<?> RemoteApi1Class =
		// XposedHelpers.findClass(CLASS_REMOTE_API_1, lpparam.classLoader);

		XposedHelpers.findAndHookMethod(SharedPrefClass, "commitLoadedMapLocked", Map.class, new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				File mFile = (File) XposedHelpers.getObjectField(param.thisObject, "mFile");
				XposedBridge.log("mFile = " + mFile.getName());
				if (!mFile.getName().equals(FILE_STARTUP_SETTINGS))
					return;
				@SuppressWarnings("unchecked")
				HashMap<String, Object> mMap = (HashMap<String, Object>) XposedHelpers.getObjectField(param.thisObject,
						"mMap");
				Integer value = (Integer) mMap.get(Common.KEY_DEBUG_LEVEL);
				XposedBridge.log("DEBUG_LVL = " + value);
				mMap.put(Common.KEY_DEBUG_LEVEL,
						Integer.parseInt(pref.getString(Common.KEY_DEBUG_LEVEL, Common.DEF_DEBUG_LEVEL)));
			}

		});
		Class<?> FreaturesClass = XposedHelpers
				.findClass("com.google.android.search.core.Feature", lpparam.classLoader);
		//ctor_dec: private com.google.android.search.core.Feature(java.lang.String,int,boolean)
		Constructor<?> FreatureCtor = XposedHelpers.findConstructorExact(FreaturesClass, String.class,int.class, boolean.class);
		XposedBridge.hookMethod(FreatureCtor, new XC_MethodHook(){

			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				XposedBridge.log(Arrays.toString(param.args));
				param.args[2]=true;
			}});
		// XposedHelpers.findAndHookMethod(RemoteApi1Class,
		// "checkForValidSignature", int.class,
		// XC_MethodReplacement.returnConstant(true));

	}

}
