package com.esainc.lib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.esainc.lib.R;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HeadlinesRotatorFragment extends SherlockFragment implements OnClickListener {
	private static final String LOG_TAG = "Headlines Rotator Fragment";
	private String mNewsID;
	private int mPos;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_headlines_top_row, container, false);
		
		// Set the image
		ImageView image = (ImageView) view.findViewById(R.id.imgArticle);
		String photoURL = getArguments().getString("photoURL").replaceAll(" ", "%20");
		imageLoader.displayImage(photoURL, image);
		
		// Set title, category, and time
		TextView headlineTitle = (TextView) view.findViewById(R.id.txtTitle);
		TextView headlineCategory = (TextView) view.findViewById(R.id.txtCategory);
		headlineTitle.setText(getArguments().getString("title"));
		headlineCategory.setText(String.format("%s - %s", getArguments().getString("category"), getArguments().getString("time")));
		
		// Set article ID for onclick listener
		mNewsID = getArguments().getString("newsID");
		mPos = getArguments().getInt("pos");
		view.setOnClickListener(this);
		view.setSaveEnabled(false);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// Load article data
		Logger.i(LOG_TAG, "Loading Article");
		Intent dataIntent = new Intent(getActivity(), DataLoader.class);
		Messenger dataMessenger = new Messenger(HeadlinesFragment.mHandler);
		dataIntent.putExtra("MESSENGER", dataMessenger);
		dataIntent.putExtra("tabPos", mPos);
		dataIntent.putExtra("type", DataType.HeadlinesArticle.ordinal());
		String params[] = new String[] {mNewsID};
		dataIntent.putExtra("params", params);
		getActivity().startService(dataIntent);
	}
}
