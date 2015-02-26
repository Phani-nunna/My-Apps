package com.esainc.lib.ui;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.esainc.lib.R;
import com.esainc.lib.adapters.ThumbnailAdapter;
import com.esainc.lib.uc.Logger;

public class ThumbnailFragment extends SherlockFragment implements OnItemClickListener {
	private static final String LOG_TAG = "Thumbnail Fragment";
	private static int mPos;
	private JSONArray mThumbnails;
	private GridView gvThumbnails;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			getSherlockActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mPos = getArguments().getInt("pos");
		try {
			mThumbnails = new JSONArray(getArguments().getString("data"));
			getSherlockActivity().getSupportActionBar().setTitle(mThumbnails.getJSONObject(0).getString(getResources().getString(R.string.tag_name)));
		} catch (NotFoundException e) {
			Logger.e(LOG_TAG, "Error loading data", e);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error loading data", e);
		}
		
		return inflater.inflate(R.layout.activity_thumbnailview, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		GridView gvThumbnails = (GridView) view.findViewById(R.id.gvThumbnails);
		gvThumbnails.setAdapter(new ThumbnailAdapter(getSherlockActivity(), mThumbnails));
		gvThumbnails.setOnItemClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	
	public void setData(String data) {
		try {
			mThumbnails = new JSONArray(data);
			((ThumbnailAdapter) gvThumbnails.getAdapter()).setData(mThumbnails);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Setting Data", e);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		final FragmentTransaction ft = getFragmentManager().beginTransaction();
		SherlockFragment galleryPhotos = new PhotoBrowserFragment();
		Bundle args = new Bundle();
		args.putInt("pos", mPos);
		args.putString("data", mThumbnails.toString());
		args.putInt("photoPos", pos);
		galleryPhotos.setArguments(args);
		ft.replace(android.R.id.content, galleryPhotos, "PhotoBrowser");
		ft.addToBackStack(null);
		ft.commit();
	}
	
}
