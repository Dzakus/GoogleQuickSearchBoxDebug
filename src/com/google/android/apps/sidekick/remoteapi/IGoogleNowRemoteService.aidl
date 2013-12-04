package com.google.android.apps.sidekick.remoteapi;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

interface IGoogleNowRemoteService{
	
	//CardsResponse getCards();
	Bitmap getSampleMap();
	Bitmap getStaticMap(in Location source,in byte[] encodedFrequentPlaceEntry, in boolean showRoute);
	String getVersion();
	boolean isUserOptedIn();
	void logAction(in int loggingRequest);
	//void logAction(in LoggingRequest loggingRequest);
	void markCalendarEntryDismissed(in long providerId);
	void recordUserAction(in byte[] encodedEntry,in int action);
	void recordViewAction(in byte[] encodedEntry,in long viewDurationMs, in int cardHeight, boolean portrait);
	void removeGroupChildEntry(in byte[] encodedParent, in byte[] encodedChild);
    
}