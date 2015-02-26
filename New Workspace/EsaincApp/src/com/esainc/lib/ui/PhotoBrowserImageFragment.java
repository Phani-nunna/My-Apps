package com.esainc.lib.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.esainc.lib.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class PhotoBrowserImageFragment extends SherlockFragment {
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Photo Browser Image Fragment";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	public String photoURL;
	public String caption;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_photos_browser_row, container, false);
		
		// Set the image
		ImageView image = (ImageView) view.findViewById(R.id.imgPhoto);
		photoURL = getArguments().getString("photo").replaceAll(" ", "%20");
		imageLoader.displayImage(photoURL, image, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (PhotoBrowserFragment.canShare) {
					PhotoBrowserFragment.getInstance().saveImage(imageUri, loadedImage);
				}
			}
		});
		
		// Set the caption
		caption = getArguments().getString("caption");
		TextView txtCaption = (TextView) view.findViewById(R.id.txtCaption);
		txtCaption.setText(caption);
		return view;
	}
}
