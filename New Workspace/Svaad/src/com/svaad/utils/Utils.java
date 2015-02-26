package com.svaad.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.svaad.BuildConfig;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CityDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.LocationDto;
import com.svaad.Dto.PlaceDto;
import com.svaad.Dto.PointDto;

public class Utils {

	private static String TAG = "Utils";
	private static boolean isGPSEnabled = false;
	private static boolean isNetworkEnabled = false;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

	public static Location location = null;
	public static final String DATE_TIME_STAMP_PATTERN = "yyyy-MM-dd_HHmmss";

	public static boolean isSDCardValid(Context context, boolean showToast) {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}

		if (Environment.MEDIA_REMOVED.equals(state)) {
			if (showToast) {
				Toast.makeText(
						context,
						context.getText(R.string.sdcard_not_present_toast_message),
						Toast.LENGTH_LONG).show();
			}

			return false;
		}

		if (Environment.MEDIA_UNMOUNTED.equals(state)) {
			if (showToast) {
				Toast.makeText(
						context,
						context.getText(R.string.sdcard_not_mounted_toast_message),
						Toast.LENGTH_LONG).show();
			}

			return false;
		}

		if (showToast) {
			Toast.makeText(
					context,
					"The SD card in the device is in '" + state
							+ "' state, and cannot be used.", Toast.LENGTH_LONG)
					.show();
		}

		return false;
	}

	public static Location getMyLocation(Context context) {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (status != ConnectionResult.SUCCESS) {
			// Google Play Services are not available
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
					(Activity) context, requestCode);
			dialog.show();

		} else {

			// LocationManager locationManager = (LocationManager) context
			// .getSystemService(Context.LOCATION_SERVICE);
			//
			// //
			// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			// // 0, 0, (LocationListener) context);
			//
			// // Creating a criteria object to retrieve provider
			// Criteria criteria = new Criteria();
			//
			// Location location = null;
			//
			// // Getting the name of the best provider
			//
			// String provider = null;
			//
			// provider = locationManager.getBestProvider(criteria, false);
			// // provider = locationManager.getBestProvider(criteria, true);
			//
			// if (provider != null) {
			//
			// // Getting Current Location
			// location = locationManager.getLastKnownLocation(provider);
			//
			// // location=getLastKnownLocation(locationManager);
			// if (location == null) {
			//
			// new SvaadDialogs().showGPSDisabledAlertToUser(context);
			//
			// }
			// }
			//
			// else {
			// // Toast.makeText(context, "Please on the GPS", 200).show();
			//
			// new SvaadDialogs().showGPSDisabledAlertToUser(context);
			//
			// // new MyDailogs().showGPSDisabledAlertToUser(context);
			//
			// }

			try {

				LocationManager locationManager = (LocationManager) context
						.getSystemService(Context.LOCATION_SERVICE);

				// getting GPS status
				isGPSEnabled = locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
				// getting network status
				isNetworkEnabled = locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

				if (!isGPSEnabled && !isNetworkEnabled) {
					// no network provider is enabled
					new SvaadDialogs().showToast(context,
							" No network provider is enabled");

				} else {
					if (isNetworkEnabled) {
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES,
								(LocationListener) context);
						Log.d("activity", "LOC Network Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								Log.d("activity", "LOC by Network");
								// latitude = location.getLatitude();
								// longitude = location.getLongitude();
							}
						}
					}
					// if GPS Enabled get lat/long using GPS Services
					if (isGPSEnabled) {
						if (location == null) {
							locationManager.requestLocationUpdates(
									LocationManager.GPS_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_CHANGE_FOR_UPDATES,
									(LocationListener) context);
							Log.d("activity", "RLOC: GPS Enabled");
							if (locationManager != null) {
								location = locationManager
										.getLastKnownLocation(LocationManager.GPS_PROVIDER);
								if (location != null) {
									Log.d("activity", "RLOC: loc by GPS");

									// latitude = location.getLatitude();
									// longitude = location.getLongitude();
								}
							}
						}
					}
					// else
					//
					// // if(!isGPS)
					// {
					// Intent intent = new
					// Intent("android.location.GPS_ENABLED_CHANGE");
					// intent.putExtra("enabled", true);
					// context.sendBroadcast(intent);
					//
					//
					// }

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return location;

		}
		return null;
	}

	// public static Location getMyLocation(Context context) {
	// int status = GooglePlayServicesUtil
	// .isGooglePlayServicesAvailable(context);
	// if (status != ConnectionResult.SUCCESS) {
	// // Google Play Services are not available
	// int requestCode = 10;
	// Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
	// (Activity) context, requestCode);
	// dialog.show();
	//
	// } else {
	//
	// LocationManager locationManager = (LocationManager) context
	// .getSystemService(Context.LOCATION_SERVICE);
	//
	// // Creating a criteria object to retrieve provider
	// Criteria criteria = new Criteria();
	//
	// // Getting the name of the best provider
	// String provider = locationManager.getBestProvider(criteria, true);
	// Location location=null ;
	// if(provider!=null)
	// {
	//
	// // Getting Current Location
	// location = locationManager.getLastKnownLocation(provider);
	// }
	//
	// return location;
	//
	// }
	// return null;
	// }

	// public static Bitmap compressImage(String imagePath, Context
	// activityContext) {
	// System.gc();
	// Options decodeBounds = new Options();
	// decodeBounds.inJustDecodeBounds = true;
	//
	// Bitmap bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds);
	// int numPixels = decodeBounds.outWidth * decodeBounds.outHeight;
	// int maxPixels = 2048 * 1536; // requires 12 MB heap
	//
	// Options options = new Options();
	// options.inSampleSize = (numPixels > maxPixels) ? 2 : 1;
	//
	// bitmap = BitmapFactory.decodeFile(imagePath, options);
	//
	// FileOutputStream out = null;
	// try {
	// out = new FileOutputStream(imagePath);
	// bitmap.compress(CompressFormat.JPEG, 60, out);
	// } catch (FileNotFoundException e) {
	// Toast.makeText(activityContext,
	// "Could not compress " + imagePath + ": " + e.getMessage(),
	// Toast.LENGTH_LONG).show();
	// Log.e(TAG, "File not found.", e);
	// } finally {
	// try {
	// if (out != null) {
	// out.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return bitmap;
	// }

	public static Bitmap compressImage(String imagePath, Context activityContext) {
		System.gc();
		Options decodeBounds = new Options();
		decodeBounds.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds);
		int numPixels = decodeBounds.outWidth * decodeBounds.outHeight;
		int maxPixels = 2048 * 1536; // requires 12 MB heap

		Options options = new Options();
		options.inSampleSize = (numPixels > maxPixels) ? 2 : 1;
		int cw = 600;
		float w = (float) decodeBounds.outWidth;
		float h = (float) decodeBounds.outHeight;
		float ratio = (float) h / w;
		float ch = ratio * cw;

		if (cw > 0 && ch > 0)
			bitmap = Bitmap.createScaledBitmap(
					BitmapFactory.decodeFile(imagePath, options), (int) cw,
					(int) ch, false);
		else
			bitmap = BitmapFactory.decodeFile(imagePath, options);

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(imagePath);
			bitmap.compress(CompressFormat.JPEG, 50, out);
		} catch (FileNotFoundException e) {
			Toast.makeText(activityContext,
					"Could not compress " + imagePath + ": " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			Log.e(TAG, "File not found.", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * 
	 * @param context
	 *            for displaying a toast in case of IO exceptions
	 * @param src
	 * @param dst
	 */
	public static void copyFile(Context context, String src, String dst) {
		if (TextUtils.equals(src, dst)) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "Source (" + src + ") and destination (" + dst
						+ ") are the same. Skipping file copying.");
			}
			return;
		}

		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException e) {
			Toast.makeText(
					context,
					"Failed to copy " + src + " to " + dst + ": "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					if (BuildConfig.DEBUG) {
						Log.w(TAG,
								"Ignored the exception caught while closing input stream for "
										+ src + ": " + e.getMessage(), e);
					}
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					if (BuildConfig.DEBUG) {
						Log.w(TAG,
								"Ignored the exception caught while closing output stream for "
										+ dst + ": " + e.getMessage(), e);
					}
				}
			}
		}
	}

	public static String makePostRequest(String url, String requestJson) {

		AndroidHttpClient httpClient = null;

		String syncMessage;
		try {

			httpClient = AndroidHttpClient.newInstance("EFFORT");
			HttpPost httpPost = new HttpPost(url);

			if (BuildConfig.DEBUG) {
				Log.d("Hi", "Request JSON: " + requestJson);
			}

			HttpEntity requestEntity = new StringEntity(requestJson);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader(Constants.APPLICATION_ID,
					Constants.APPLICATION_ID_VALUE);
			httpPost.setHeader(Constants.REST_API_KEY, Constants.REST_API_VALUE);
			if (Utils.getFromSharedPreference(Constants.SESSION_TOCKEN_KEY) != null) {
				httpPost.setHeader(Constants.X_PARSE_SESSION_TOKEN, Utils
						.getFromSharedPreference(Constants.SESSION_TOCKEN_KEY));
			}
			httpPost.setEntity(requestEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK
					|| httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED
					|| httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
				String response = null;
				try {
					response = EntityUtils.toString(httpResponse.getEntity());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return response;

			} else {
				syncMessage = "Sync failed, unexpected response from cloud.";
			}
		} catch (MalformedURLException e) {
			syncMessage = "Sync failed, due to bad URL.";
		} catch (IOException e) {
			syncMessage = "Sync failed, due to network error.";
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}

			if (BuildConfig.DEBUG) {
				Log.d("hi", "Sending tracks after sync completion.");
			}

		}
		Log.d(TAG, syncMessage);
		return null;

	}

	public static String makePostRequestNew(String url, String requestJson)
			throws IOException {

		AndroidHttpClient httpClient = null;

		String syncMessage;

		httpClient = AndroidHttpClient.newInstance("EFFORT");
		HttpPost httpPost = new HttpPost(url);

		if (BuildConfig.DEBUG) {
			Log.d("Hi", "Request JSON: " + requestJson);
		}

		HttpEntity requestEntity = new StringEntity(requestJson);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader(Constants.APPLICATION_ID,
				Constants.APPLICATION_ID_VALUE);
		httpPost.setHeader(Constants.REST_API_KEY, Constants.REST_API_VALUE);
		if (Utils.getFromSharedPreference(Constants.SESSION_TOCKEN_KEY) != null) {
			httpPost.setHeader(Constants.X_PARSE_SESSION_TOKEN,
					Utils.getFromSharedPreference(Constants.SESSION_TOCKEN_KEY));
		}
		httpPost.setEntity(requestEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK
				|| httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED
				|| httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
			String response = EntityUtils.toString(httpResponse.getEntity());

			return response;

		} else {
			syncMessage = "Sync failed, unexpected response from cloud.";
		}
		if (httpClient != null) {
			httpClient.close();
		}

		if (BuildConfig.DEBUG) {
			Log.d("hi", "Sending tracks after sync completion.");
		}

		Log.d(TAG, syncMessage);
		return null;

	}

	public static String makeGetRequest(String url) {

		AndroidHttpClient httpClient = null;
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "Request Url: " + url);
		}
		String syncMessage;
		try {

			httpClient = AndroidHttpClient.newInstance("EFFORT");
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(Constants.APPLICATION_ID,
					Constants.APPLICATION_ID_VALUE);
			httpGet.setHeader(Constants.REST_API_KEY, Constants.REST_API_VALUE);
			if (Utils.getFromSharedPreference(Constants.SESSION_TOCKEN_KEY) != null) {
				httpGet.setHeader(Constants.X_PARSE_SESSION_TOKEN, Utils
						.getFromSharedPreference(Constants.SESSION_TOCKEN_KEY));
			}

			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK
					|| httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED
					|| httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
				String response = EntityUtils
						.toString(httpResponse.getEntity());
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "Response JSON: " + response);
				}
				return response;

			} else {
				syncMessage = "Sync failed, unexpected response from cloud.";
			}
		} catch (MalformedURLException e) {
			syncMessage = "Sync failed, due to bad URL.";
		} catch (IOException e) {
			syncMessage = "Sync failed, due to network error.";
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "Sending tracks after sync completion.");
			}

		}
		Log.d(TAG, syncMessage);
		return null;

	}

	public static void saveToSharedPreference(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void saveToSharedPreferenceInt(String key, int value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void saveToSharedPreferenceList(String key, List<String> value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());
		SharedPreferences.Editor editor = sharedPreferences.edit();
		try {
			editor.putString(key, ObjectSerializer.serialize(value));
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int getFromIntegerSharedPreference(String key) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());
		return sharedPreferences.getInt(key, 0);
	}

	public static String getFromSharedPreference(String key) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());
		return sharedPreferences.getString(key, null);
	}

	public static String getMediaPath() {
		return Environment.getExternalStorageDirectory() + "/Svaad/img-"
				+ Utils.getDateTimeStamp() + ".jpg";
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDateTimeStamp() {
		return new SimpleDateFormat(DATE_TIME_STAMP_PATTERN).format(new Date());
	}

	public static void setColorsToDishTags(String dishTag, TextView tvDishTag) {
		if (dishTag.equalsIgnoreCase("1")) {
			tvDishTag.setText("Loved it");
			tvDishTag.setBackgroundColor(Color.parseColor("#00933b"));
		} else if (dishTag.equalsIgnoreCase("2")) {
			tvDishTag.setText("Good");
			tvDishTag.setBackgroundColor(Color.parseColor("#f2b50f"));
		} else if (dishTag.equalsIgnoreCase("3")) {
			tvDishTag.setText("Its ok");
			tvDishTag.setBackgroundColor(Color.parseColor("#0266c8"));
		} else if (dishTag.equalsIgnoreCase("4")) {
			tvDishTag.setText("Nevermind");
			tvDishTag.setBackgroundColor(Color.parseColor("#f90101"));
		}
	}

	public static boolean isOnline(Context applicationContext) {
		ConnectivityManager cm = (ConnectivityManager) applicationContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static List<FeedDetailDto> convertDataToFeedDetailDtoObject(
			List<FeedDetailDto> feedDetailDto) {

		List<FeedDetailDto> feedsList = new ArrayList<FeedDetailDto>();
		if (feedDetailDto != null && feedDetailDto.size() > 0) {

			for (FeedDetailDto feed : feedDetailDto) {
				// FeedDetailDto feedDetail=new FeedDetailDto();
				BranchDishIdDto branchDishIdDto = new BranchDishIdDto();

				DishIdDto dishIdDto = feed.getDishId();

				String branchDishId = feed.getObjectId();
				BranchIdDto branchIdDto = feed.getBranchId();
				CityDto cityDto = feed.getCity();
				LocationDto locationDto = feed.getLocation();
				PlaceDto placeDto = feed.getPlace();
				PointDto pointDto = feed.getPoint();

				String branchName = feed.getBranchName();
				int oneTag = feed.getOneTag();

				// String oneTagString=feed.getOneTag();
				// int oneTag=Integer.parseInt(oneTagString);

				if (branchDishId != null && branchDishId.length() > 0) {
					branchDishIdDto.setObjectId(branchDishId);
				}
				if (branchIdDto != null) {
					branchDishIdDto.setBranchId(branchIdDto);
				}
				if (cityDto != null) {
					branchDishIdDto.setCity(cityDto);
				}
				if (locationDto != null) {
					branchDishIdDto.setLocation(locationDto);
				}
				if (placeDto != null) {
					branchDishIdDto.setPlace(placeDto);
				}
				if (pointDto != null) {
					branchDishIdDto.setPoint(pointDto);
				}

				if (dishIdDto != null) {

					branchDishIdDto.setDishId(dishIdDto);
				}

				if (branchName != null && branchName.length() > 0) {
					branchDishIdDto.setBranchName(branchName);
				}

				// if(oneTag!=null && oneTag.length()>0)
				// {
				// branchDishIdDto.setOneTag(oneTag);
				// }

				if (oneTag != 0) {
					branchDishIdDto.setOneTag(oneTag);
				}

				feed.setBranchDishId(branchDishIdDto);

				feedsList.add(feed);
			}
		}
		return feedsList;

	}

}
