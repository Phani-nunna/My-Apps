package com.esainc.lib.uc;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;

import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.backend.APICaller;

public final class Logger {
	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int ASSERT = 7;
	
	private Context mContext = SIDHelpApplication.mContext;
	private static boolean mUseLog = false;
	private static String logAPI = null;
	private static int mLogLevel = 6;
	private static Logger instance = null;
	
	private Logger() {
		new IPAddressTask().execute(true);
	}
	
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	public static void setUseLog(boolean useLog) {
		mUseLog = useLog;
	}
	
	public static boolean isUseLog() {
		return mUseLog;
	}
	
	public static void setLogLevel(int logLevel) {
		mLogLevel = logLevel;
	}
	
	public static int getLogLevel() {
		return mLogLevel;
	}
	
	/** Send a DEBUG log message. */
	public static int d(String tag, String msg) {
		if (mUseLog) {
			Log.d(tag, msg);
		}
		if (DEBUG >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg));
		}
		return 0;
	}
	
	/** Send a DEBUG log message and log the exception. */
	public static int d(String tag, String msg, Throwable tr) {
		if (mUseLog) {
			Log.d(tag, msg, tr);
		}
		if (DEBUG >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg + " : " + URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	/** Send an ERROR log message. */
	public static int e(String tag, String msg) {
		if (mUseLog) {
			Log.e(tag, msg);
		}
		if (ERROR >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			getInstance().new CallAPITask().execute(String.format(logAPI, "Error", tag, msg));
		}
		return 0;
	}
	
	/** Send a ERROR log message and log the exception. */
	public static int e(String tag, String msg, Throwable tr) {
		if (mUseLog) {
			Log.e(tag, msg, tr);
		}
		if (ERROR >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Error", tag, msg + " : " + URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	/** Handy function to get a loggable stack trace from a Throwable */
	public static String getStackTraceString(Throwable tr) {
		return Log.getStackTraceString(tr);
	}
	
	/** Send an INFO log message. */
	public static int i(String tag, String msg) {
		if (mUseLog) {
			Log.i(tag, msg);
		}
		if (INFO >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg));
		}
		return 0;
	}
	
	/** Send a INFO log message and log the exception. */
	public static int i(String tag, String msg, Throwable tr) {
		if (mUseLog) {
			Log.i(tag, msg, tr);
		}
		if (INFO >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg + " : " + URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	/** Checks to see whether or not a log for the specified tag is loggable at the specified level. */
	public static boolean isLoggable(String tag, int level) {
		return Log.isLoggable(tag, level);
	}
	
	/** Low-level logging call. */
	public static int println(int priority, String tag, String msg) {
		if (mUseLog) {
			Log.println(priority, tag, msg);
		}
		return 0;
	}
	
	/** Send a VERBOSE log message. */
	public static int v(String tag, String msg) {
		if (mUseLog) {
			Log.v(tag, msg);
		}
		if (VERBOSE >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg));
		}
		return 0;
	}
	
	/** Send a VERBOSE log message and log the exception. */
	public static int v(String tag, String msg, Throwable tr) {
		if (mUseLog) {
			Log.v(tag, msg, tr);
		}
		if (VERBOSE >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg + " : " + URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	/** Send a WARN log message and log the exception. */
	public static int w(String tag, Throwable tr) {
		if (mUseLog) {
			Log.w(tag, tr);
		}
		if (WARN >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	/** Send a WARN log message and log the exception. */
	public static int w(String tag, String msg, Throwable tr) {
		if (mUseLog) {
			Log.w(tag, msg, tr);
		}
		if (WARN >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg + " : " + URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	/** Send a WARN log message. */
	public static int w(String tag, String msg) {
		if (mUseLog) {
			Log.w(tag, msg);
		}
		if (WARN >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			getInstance().new CallAPITask().execute(String.format(logAPI, "Notice", tag, msg));
		}
		return 0;
	}
	
	/** What a Terrible Failure: Report an exception that should never happen. */
	public static int wtf(String tag, Throwable tr) {
		if (mUseLog) {
			Log.wtf(tag, tr);
		}
		if (ASSERT >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Error", tag, URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	/** What a Terrible Failure: Report a condition that should never happen. */
	public static int wtf(String tag, String msg) {
		if (mUseLog) {
			Log.wtf(tag, msg);
		}
		if (ASSERT >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			getInstance().new CallAPITask().execute(String.format(logAPI, "Error", tag, msg));
		}
		return 0;
	}
	
	/** What a Terrible Failure: Report a condition that should never happen. */
	public static int wtf(String tag, String msg, Throwable tr) {
		if (mUseLog) {
			Log.wtf(tag, msg, tr);
		}
		if (ASSERT >= mLogLevel && !TextUtils.isEmpty(logAPI)) {
			try {
				getInstance().new CallAPITask().execute(String.format(logAPI, "Error", tag, msg + " : " + URLEncoder.encode(getStackTraceString(tr), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return 0;
	}
	
	
	private class CallAPITask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... urls) {
			String url = urls[0].replaceAll(" ", "+");
			APICaller caller = new APICaller();
			caller.loadDataFromUrl(url);
			return null;
		}
		
	}
	
	private class IPAddressTask extends AsyncTask<Boolean, Void, String> {
		@Override
		protected String doInBackground(Boolean... useIPv4) {
			try {
				List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
				for (NetworkInterface intf : interfaces) {
					List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
					for (InetAddress addr : addrs) {
						if (!addr.isLoopbackAddress()) {
							String sAddr = addr.getHostAddress().toUpperCase(Locale.US);
							boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
							if (useIPv4[0]) {
								if (isIPv4) return sAddr;
							} else {
								if (!isIPv4) {
									int delim = sAddr.indexOf('%'); // drop IPv6 port suffix
									return delim < 0 ? sAddr : sAddr.substring(0, delim);
								}
							}
						}
					}
				}
			} catch (Exception e) {
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String ipAddress) {
			String version = "0.0.0";
			try {
				version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
			}
			logAPI = mContext.getResources().getString(R.string.SiteURL) + "/" + mContext.getResources().getString(R.string.api_default, mContext.getResources().getString(R.string.api), "a-" + version)  + 
				"/logging/log/error/%s/IPAddress/" + ipAddress + "/iOSVersion/" + VERSION.RELEASE + "/appVersion/" + version + "/moduleName/%s/message/%s.json";
		}
	}
}
