package com.google.android.googlequicksearchbox.debug;

import com.google.android.apps.sidekick.remoteapi.IGoogleNowRemoteService;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class MainActivity extends Activity implements OnClickListener, ServiceConnection {

	private static final String TAG = MainActivity.class.getSimpleName();
	private Button mButton1;
	private Button mButton2;
	private IGoogleNowRemoteService mService;
	private TextView mLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mButton1 = (Button) findViewById(R.id.button1);
		mButton1.setOnClickListener(this);
		mButton2 = (Button) findViewById(R.id.button2);
		mButton2.setOnClickListener(this);
		mLog = (TextView) findViewById(R.id.log);

	}

	@Override
	protected void onResume() {
		super.onResume();
		dLog("Bind to Service = " + bindToRemoteApiService());
	}

	@Override
	protected void onPause() {

		unbindService(this);
		super.onPause();
	}
	
	@Override
	public void onClick(View v) {
		if (v.equals(mButton1)) {
			try {
				dLog("getVersion = " + mService.getVersion());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else if (v.equals(mButton2)) {
			try {
				dLog("getSampleMap.byteCount = " + mService.getSampleMap().getByteCount());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Binds to the RemoteApiService and returns true if the bind succeeded.
	 * 
	 * @return true if the bind succeeded; false otherwise
	 */
	private boolean bindToRemoteApiService() {
		try {
			boolean bindResult = bindService(new Intent(Consts.REMOTE_API_SERVICE_ACTION), this, // ServiceConnection.
					Context.BIND_AUTO_CREATE);

			return bindResult;
		} catch (SecurityException e) {
			Log.e(TAG, "Security exception: " + e);
		}
		return false;
	}



	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = IGoogleNowRemoteService.Stub.asInterface(service);
		dLog("onServiceConnected");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
		dLog("onServiceDisconnected");
	}

	private void dLog(String text) {
		mLog.append(text + "\n");
	}
}
