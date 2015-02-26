package com.esainc.lib.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VideosRotatorFragment extends SherlockFragment implements OnClickListener {
	private static final String LOG_TAG = "Videos Rotator Fragment";
	private String mVideoID;
	private boolean mCrop;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_youtube_top_row, container, false);
		mCrop = getArguments().getBoolean("crop");
		
		// Set the image
		RelativeLayout rlVideo = (RelativeLayout) view.findViewById(R.id.rlVideo);
		ImageView image = (ImageView) view.findViewById(R.id.imgVideo);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (!mCrop) {
				rlVideo.getLayoutParams().height = (int) Math.floor((((float) getResources().getInteger(R.integer.video_rotator_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth()));
				image.getLayoutParams().height = (int) Math.floor((((float) getResources().getInteger(R.integer.video_rotator_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth()));
				image.setScaleType(ScaleType.FIT_CENTER);
			} else {
				rlVideo.getLayoutParams().height = (int) Math.floor((((float) getResources().getInteger(R.integer.video_rotator_crop_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth()));
				image.getLayoutParams().height = (int) Math.floor((((float) getResources().getInteger(R.integer.video_rotator_crop_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth()));
				image.setScaleType(ScaleType.CENTER_CROP);
			}
		} else {
			// make sure pager isn't too tall - constrain to half display height
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			if (!mCrop) {
				rlVideo.getLayoutParams().height = (int) Math.min(Math.floor((((float) getResources().getInteger(R.integer.video_rotator_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth())), displayHeight * 0.5);
				image.getLayoutParams().height = (int) Math.min(Math.floor((((float) getResources().getInteger(R.integer.video_rotator_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth())), displayHeight * 0.5);
				image.setScaleType(ScaleType.FIT_CENTER);
			} else {
				rlVideo.getLayoutParams().height = (int) Math.min(Math.floor((((float) getResources().getInteger(R.integer.video_rotator_crop_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth())), displayHeight * 0.5);
				image.getLayoutParams().height = (int) Math.min(Math.floor((((float) getResources().getInteger(R.integer.video_rotator_crop_height) / (float) getResources().getInteger(R.integer.video_rotator_width)) * (float) getActivity().getWindowManager().getDefaultDisplay().getWidth())), displayHeight * 0.5);
				image.getLayoutParams().width = (int) Math.floor(((float) getResources().getInteger(R.integer.video_rotator_width) / (float) getResources().getInteger(R.integer.video_rotator_crop_height)) * (float) image.getLayoutParams().height);
				image.setScaleType(ScaleType.CENTER_CROP);
			}
		}
		
		String photoURL = getArguments().getString("photoURL").replaceAll(" ", "%20");
		imageLoader.displayImage(photoURL, image);
		
		ImageView playButton = (ImageView) view.findViewById(R.id.imgPlay);
		DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			.showStubImage(0)
			.showImageForEmptyUri(0)
			.showImageOnFail(0)
			.cacheInMemory(true)
			.build();
		
		
		double limitedWidth = (double) ((ViewPager) getActivity().findViewById(R.id.youtube_rotator)).getLayoutParams().height * (getResources().getInteger(R.integer.video_rotator_width) / ((mCrop) ? getResources().getInteger(R.integer.video_rotator_crop_height) : getResources().getInteger(R.integer.video_rotator_height)));
		int width = (int) Math.min((double) ((getResources().getInteger(R.integer.video_rotator_width) * (getResources().getDisplayMetrics().densityDpi / 160f)) / 4), limitedWidth);
		imageLoader.displayImage("https://lh6.ggpht.com/NrQdFAdPSI9-hreB4C7HNhj3yXRiW1jqOOi7eFyakIx_IA-Im0huIeYCs5jTidMT2qA=w" + width, playButton, displayOptions);
		
		// Set title, description, uploader, and duration
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
		TextView txtUploader = (TextView) view.findViewById(R.id.txtUploader);
		TextView txtDuration = (TextView) view.findViewById(R.id.txtDuration);
		
		txtTitle.setText(getArguments().getString("title"));
		txtDescription.setText(getArguments().getString("description"));
		txtUploader.setText(getArguments().getString("uploader"));
		txtDuration.setText(getArguments().getString("duration"));
		
		mVideoID = getArguments().getString("videoID");
		view.setOnClickListener(this);
		view.setSaveEnabled(false);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// Load Video
		Logger.i(LOG_TAG, "Loading Video");
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mVideoID)));
	}
}
