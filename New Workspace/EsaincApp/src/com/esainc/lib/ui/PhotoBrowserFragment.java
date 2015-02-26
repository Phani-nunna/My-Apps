package com.esainc.lib.ui;

import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.esainc.lib.R;
import com.esainc.lib.adapters.PhotoBrowserAdapter;
import com.esainc.lib.uc.Logger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

public class PhotoBrowserFragment extends SherlockFragment {
	private static final String LOG_TAG = "Photo Browser Fragment";
	private int mPhotoPos;
	private JSONArray mPhotos;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	private Tracker tracker;
	private ShareActionProvider mShareActionProvider;
	private Intent mShareIntent;
	public static ConditionVariable mCondition = new ConditionVariable(false);
	public static boolean canShare = true;
	private static PhotoBrowserFragment instance;
	private boolean onEnds = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		EasyTracker.getInstance().setContext(getActivity());
		tracker = EasyTracker.getTracker();
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			canShare = true;
		} else {
			canShare = false;
		}
		
		if (canShare) {
			mShareIntent = new Intent();
			mShareIntent.setAction(Intent.ACTION_SEND);
			mShareIntent.setType("image/*");
		}
		
		instance = this;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.photo_browser, menu);
		
		if (canShare) {
			mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
			mShareActionProvider.setShareIntent(mShareIntent);
		} else {
			menu.findItem(R.id.action_share).setVisible(false);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_share) {
			startActivity(Intent.createChooser(mShareIntent, "Share via"));
			return true;
		} else if (itemId == android.R.id.home) {
			getSherlockActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mPhotoPos = getArguments().getInt("photoPos");
		try {
			mPhotos = new JSONArray(getArguments().getString("data"));
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		
		return inflater.inflate(R.layout.activity_photos_browser, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPager = (ViewPager) view.findViewById(R.id.photoBrowser);
		mPager.setAdapter(new PhotoBrowserAdapter(this, getActivity(), mPhotos));
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager, mPhotoPos);
		if (mPhotoPos == 0 || mPhotoPos == mPager.getAdapter().getCount() - 1) {
			onEnds = true;
		}
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
			}
			
			@Override
			public void onPageScrolled(int pos, float offset, int offsetPixels) {
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_IDLE) {
					if (canShare) {
						new ShareSetup().execute();
					}
				}
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tracker.sendView("Photos page");
		
		if (canShare) {
			for (Entry<String, String> entry : PhotoBrowserAdapter.paths.entrySet()) {
				Cursor cur = Images.Media.query(getActivity().getContentResolver(), Uri.parse(entry.getValue()), null);
				if (cur.getCount() == 0) {
					ImageView temp = new ImageView(getActivity());
					ImageLoader.getInstance().displayImage(entry.getKey(), temp, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							saveImage(imageUri, loadedImage);
						}
					});
				}
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (canShare) {
			new ShareSetup().execute();
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		if (canShare) {
			for (Entry<String, String> entry : PhotoBrowserAdapter.paths.entrySet()) {
				Cursor cur = Images.Media.query(getActivity().getContentResolver(), Uri.parse(entry.getValue()), null);
				if (cur.getCount() != 0) {
					getActivity().getContentResolver().delete(Uri.parse(entry.getValue()), null, null);
				}
			}
			PhotoBrowserAdapter.pathCount = 0;
		}
	}
	
	private class ShareSetup extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			mCondition.block();
			PhotoBrowserImageFragment currentFragment = (PhotoBrowserImageFragment) mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem());
			String path = PhotoBrowserAdapter.paths.get(currentFragment.photoURL);
			String caption = currentFragment.caption;
			
			if (path != null) {
				mShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
				mShareIntent.putExtra(Intent.EXTRA_TEXT, caption);
				mShareIntent.putExtra(Intent.EXTRA_SUBJECT, caption);
			}
			mCondition.close();
			return null;
		}
	}
	
	public void saveImage(String imageUri, Bitmap loadedImage) {
		String path = Images.Media.insertImage(getActivity().getContentResolver(), loadedImage, "photo_gallery", null);
		PhotoBrowserAdapter.paths.put(imageUri, path);
		PhotoBrowserAdapter.pathCount++;
		int checkCount = ((onEnds) ? 2 : 3);
		if (PhotoBrowserAdapter.pathCount >= checkCount) {
			PhotoBrowserFragment.mCondition.open();
		}
	}
	
	public static PhotoBrowserFragment getInstance() {
		return instance;
	}
}
