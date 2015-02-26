package com.esainc.lib.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.ui.VideosRotatorFragment;

public class VideosRotatorAdapter extends FragmentStatePagerAdapter{
	private static final String LOG_TAG = "Videos Rotator Adapter";
	private Context mContext;
	private JSONArray mData;
	private boolean mCrop;
	
	public VideosRotatorAdapter(SherlockListFragment fragment, Context context, JSONArray data, boolean crop) {
		super(fragment.getChildFragmentManager());
		mContext = context;
		mData = data;
		mCrop = crop;
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
			JSONObject hdata = mData.getJSONObject(pos).getJSONObject(mContext.getResources().getString(R.string.tag_video));

			int totalSecs = Integer.parseInt(hdata.getString(mContext.getResources().getString(R.string.tag_duration)));
			int hours = totalSecs / 3600;
			int minutes = (totalSecs % 3600) / 60;
			int seconds = totalSecs % 60;
			String duration = "";
			if (hours > 0) duration = duration.concat(String.valueOf(hours) + ":");
			duration = duration.concat(String.format("%02d", minutes) + ":");
			duration = duration.concat(String.format("%02d", seconds));
			args.putString("duration", duration);
			args.putString("title", hdata.getString(mContext.getResources().getString(R.string.tag_title)));
			args.putString("uploader", mContext.getResources().getString(R.string.uploaded_by) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_uploader)));
			args.putString("description", hdata.getString(mContext.getResources().getString(R.string.tag_description)));
			args.putString("photoURL", hdata.getJSONObject(mContext.getResources().getString(R.string.tag_thumbnail)).getString(mContext.getResources().getString(R.string.tag_hqDefault)));
			args.putString("videoID", hdata.getString(mContext.getResources().getString(R.string.tag_id)));
			args.putBoolean("crop", mCrop);
			fragment = Fragment.instantiate(mContext, VideosRotatorFragment.class.getName(), args);
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
