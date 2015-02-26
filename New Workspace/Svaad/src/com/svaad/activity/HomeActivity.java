package com.svaad.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Session;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.fragment.HomeTabsFragment;
import com.svaad.utils.Constants;
import com.svaad.utils.KitkatMediaPath;
import com.svaad.utils.LocationUtil;
import com.svaad.utils.Utils;

public class HomeActivity extends BaseActivity {

	FragmentTransaction fragmentTransaction;
	HomeTabsFragment homeFragment;
	int pagerMode;
	LocationManager locationManager;
	private Bitmap bitmap;
	private String mediaPath;
	Uri imageUri;

	// public String getMediaPath() {
	// return mediaPath;
	// }
	//
	// public void setMediaPath(String mediaPath) {
	// this.mediaPath = mediaPath;
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// try{
		//
		// new SvaadGoogleAnalytics().startGoogleAnalytics(HomeActivity.this);
		// }
		// catch(Exception e)
		// {
		// e.printStackTrace();
		// }
		//
		setContentView(R.layout.home_activity_layout);
		// getimageUri();

		final ActionBar actionBar = getActionBar();

		// if (!isTaskRoot()) {
		// finish();
		// return;
		// }

		actionBar.hide();
		LocationUtil.getInstance(getApplicationContext())
				.registerLocationListener();
		if (this.getIntent().getExtras() != null) {
			pagerMode = this.getIntent().getExtras().getInt(Constants.PAGER);
			if (pagerMode != 0) {
				fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				homeFragment = new HomeTabsFragment();
				Bundle b = new Bundle();
				b.putInt(Constants.PAGER, pagerMode);
				homeFragment.setArguments(b);
				fragmentTransaction
						.replace(R.id.homeLinearLayout, homeFragment);

				fragmentTransaction.commit();
			}

		} else {

			fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			homeFragment = new HomeTabsFragment();
			fragmentTransaction.replace(R.id.homeLinearLayout, homeFragment);
			fragmentTransaction.commit();
		}

	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, intent);
			getMediaPath(requestCode, resultCode, intent);
		} else {
			getMediaPath(requestCode, resultCode, intent);
		}
	}

	@SuppressLint("NewApi")
	private void getMediaPath(int requestCode, int resultCode, Intent intent) {
		String media = Utils.getMediaPath();
		String mediaPath = media;
		reinitializeValues();

		if (mediaPath == null) {
			return;
		}
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case Constants.CAPTURE_IMAGE:

				Intent in = new Intent(HomeActivity.this,
						CamResListActivity.class);
				String path = Utils.getFromSharedPreference("cam_path");

				in.putExtra("path", path);
				startActivity(in);

				break;

			case Constants.GALLERY_IMAGE:
				if (mediaPath != null && intent != null
						&& intent.getData() != null) {

					Uri originalUri = intent.getData();
					Uri uri = intent.getData();

					String id = "";
					if (uri.getLastPathSegment().contains(":"))
						id = uri.getLastPathSegment().split(":")[1];
					else
						id = uri.getLastPathSegment();

					if (uri != null && "content".equals(uri.getScheme())) {
						Cursor cursor = getContentResolver()
								.query(uri,
										new String[] {
												MediaStore.Images.Media.DATA,
												MediaStore.Images.ImageColumns.MIME_TYPE },
										MediaStore.Images.Media._ID + "=" + id,
										null, null);

						if (cursor != null && cursor.moveToFirst()) {

							if (cursor.getString(0) != null) {

								String l = cursor
										.getString(cursor
												.getColumnIndex(MediaStore.Images.Media.DATA));
								Utils.copyFile(this, l, mediaPath);

								// Bitmap bitmap2 = Utils.compressImage(
								// mediaPath, this);
								Intent i = new Intent(HomeActivity.this,
										CamResListActivity.class);
								// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra("path", mediaPath);
								startActivity(i);
							}

							else {

								// ParcelFileDescriptor parcelFileDescriptor;
								// try {
								// parcelFileDescriptor = getContentResolver()
								// .openFileDescriptor(uri, "r");
								// FileDescriptor fileDescriptor =
								// parcelFileDescriptor
								// .getFileDescriptor();
								// Bitmap image = BitmapFactory
								// .decodeFileDescriptor(fileDescriptor);
								// parcelFileDescriptor.close();
								//
								// FileOutputStream out = null;
								// Bitmap bitmap2 = null;
								// try {
								// out = new FileOutputStream(
								// mediaPath);
								// image.compress(
								// Bitmap.CompressFormat.PNG,
								// 90, out);
								// // bitmap2 = Utils.compressImage(
								// // mediaPath, this);
								// } catch (Exception e) {
								// e.printStackTrace();
								// } finally {
								// try {
								// out.close();
								// } catch (Throwable ignore) {
								// }
								// }
								//
								// Intent inttent = new Intent(
								// HomeActivity.this,
								// CamResListActivity.class);
								// //
								// inttent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								// inttent.putExtra("path", mediaPath);
								// startActivity(inttent);
								//
								// } catch (FileNotFoundException e) {
								// e.printStackTrace();
								// } catch (IOException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// }

								Uri origUri = intent.getData();
								final int takeFlags = intent.getFlags()
										& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
								// Check for the freshest data.

								// String kitkatPath=origUri.toString();
								getContentResolver()
										.takePersistableUriPermission(origUri,
												takeFlags);
								String kitkathpath =KitkatMediaPath.getPath(HomeActivity.this, origUri);
								Utils.copyFile(this, kitkathpath, mediaPath);
								Intent inttent = new Intent(HomeActivity.this,
										CamResListActivity.class);
								// inttent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								inttent.putExtra("path", mediaPath);
								startActivity(inttent);
							}

						}
						cursor.close();
					} else if (mediaPath != null && uri != null
							&& "file".equals(uri.getScheme())) {
						Utils.copyFile(this, uri.getPath(), mediaPath);
						Intent inttent = new Intent(HomeActivity.this,
								CamResListActivity.class);
						// inttent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						inttent.putExtra("path", mediaPath);
						startActivity(inttent);
					} else {
						Toast.makeText(
								this,
								"The item you picked is not an image. Please pick an image.",
								Toast.LENGTH_LONG).show();
						return;
					}

				}
				break;

			default:
				break;
			}
		}
	}

	private String getRealPathFromFitkat(Uri contentURI) {
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}

	private void reinitializeValues() {

		if (mediaPath == null) {
			mediaPath = Utils.getFromSharedPreference(Constants.IMAGE_PATH);
		}
	}

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	//
	// new SvaadDialogs().showToast(HomeActivity.this, "Home Activity back");
	//
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.foodies_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// case android.R.id.home:
		//
		// finish();
		// break;

		case R.id.action_addfriends:

			Intent i = new Intent(HomeActivity.this, FoodiesActivity.class);
			startActivity(i);
			finish();

			break;
		case R.id.menu_load:

			// new SvaadDialogs().showLogoutDilog(getActivity());

			// getFeedsList();
			Intent in = new Intent(HomeActivity.this, HomeActivity.class);
			// in.putExtra(Constants.PAGER, Constants.PAGER_MODE_FEED);
			startActivity(in);
			finish();

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocationUtil.getInstance(getApplicationContext())
				.unRegisterLocationListener();

	}

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	//
	// Utils.saveToSharedPreferenceInt("radioLocaion", 0);
	// Utils.saveToSharedPreference("radioLocationValue", null);
	// Utils.saveToSharedPreference("radioLocationObjectId", null);
	// Utils.saveToSharedPreference("locationback", null);
	//
	// }
}
