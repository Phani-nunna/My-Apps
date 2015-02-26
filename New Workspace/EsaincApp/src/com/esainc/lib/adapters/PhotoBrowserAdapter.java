package com.esainc.lib.adapters;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.ui.PhotoBrowserImageFragment;

public class PhotoBrowserAdapter extends FragmentStatePagerAdapter {
	private static final String LOG_TAG = "Photo Browser Adapter";
	private Context mContext;
	private JSONArray mData;
	public static HashMap<String, String> paths = new HashMap<String, String>();
	public static int pathCount = 0;
	
	public PhotoBrowserAdapter(SherlockFragment fragment, Context context, JSONArray data) {
		super(fragment.getChildFragmentManager());
		mContext = context;
		mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.length();
	}
	
	@Override
	public Fragment getItem(int pos) {
		Fragment fragment = null;
		Bundle args = new Bundle();
		try {
			JSONObject hdata = mData.getJSONObject(pos);
			String caption = hdata.getString(mContext.getResources().getString(R.string.tag_caption));
			String photoURL = hdata.getString(mContext.getResources().getString(R.string.tag_image));
			args.putString("caption", caption);
			args.putString("photo", photoURL);
			fragment = Fragment.instantiate(mContext, PhotoBrowserImageFragment.class.getName(), args);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		
		return fragment;
	}
	
	public void setData(JSONArray data) {
		mData = data;
		notifyDataSetChanged();
	}
}
