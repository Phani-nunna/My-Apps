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
import com.esainc.lib.ui.HeadlinesRotatorFragment;

public class HeadlinesRotatorAdapter extends FragmentStatePagerAdapter {
	private static final String LOG_TAG = "Headlines Rotator Adapter";
	private Context mContext;
	private JSONArray mData;
	private int mPos;
	
	public HeadlinesRotatorAdapter(SherlockListFragment fragment, Context context, JSONArray data, int pos) {
		super(fragment.getChildFragmentManager());
		mContext = context;
		mData = data;
		mPos = pos;
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
			args.putString("title", hdata.getString(mContext.getResources().getString(R.string.tag_title)));
			args.putString("category", hdata.getString(mContext.getResources().getString(R.string.tag_category)));
			args.putString("time", hdata.getString(mContext.getResources().getString(R.string.tag_time)));
			args.putString("newsID", hdata.getString(mContext.getResources().getString(R.string.tag_newsID)));
			args.putString("photoURL", hdata.getString(mContext.getResources().getString(R.string.tag_photoURL)));
			args.putInt("pos", mPos);
			fragment = Fragment.instantiate(mContext, HeadlinesRotatorFragment.class.getName(), args);
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
