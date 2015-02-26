package com.paradigmcreatives.audit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {
	private static Utility mUtility;
	public Utility() {
		mUtility = this;
	}
	public static Utility getInstance(){
		if(mUtility == null){
			mUtility = new Utility();
		}
		return mUtility;
	}
	public boolean isOnline(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm == null){
			return false;
		}
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if(ni!=null){
			return ni.isConnectedOrConnecting();
		}else{
			return false;
		}
		}
	public  boolean isInternetOn(Context mContext)
	{
	  ConnectivityManager connec = (ConnectivityManager)mContext.  getSystemService(Context.CONNECTIVITY_SERVICE);

	  // ARE WE CONNECTED TO THE NET
	  if ( connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
	       connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED )
	  {
	    // MESSAGE TO SCREEN FOR TESTING (IF REQ)
	    //Toast.makeText(this, connectionType + ” connected”, Toast.LENGTH_SHORT).show();
	    return true;
	  }
	  else if ( connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
	    ||  connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED  )
	  {
	    return false;
	  }

	  return false;
	}
}
