package com.esainc.lib.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;

public class LinksAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Links Adapter";
	private Context mContext;
	private JSONArray mData;
	
	public LinksAdapter(Context context, JSONArray data) {
		mContext = context;
		mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.length();
	}
	
	@Override
	public Object getItem(int pos) {
		try {
			return mData.getJSONObject(pos);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting item", e);
		}
		return null;
	}
	
	@Override
	public long getItemId(int pos) {
		return pos;
	}
	
	public void setData(JSONArray data) {
		mData = data;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			view = new View(mContext);
			view = inflater.inflate(R.layout.activity_additionallinks_row, null);
		} else {
			view = convertView;
		}
		
		try {
			JSONObject hdata = mData.getJSONObject(pos);
			TextView txtLinksName = (TextView) view.findViewById(R.id.txtLinksName);
			txtLinksName.setText(hdata.getString(mContext.getResources().getString(R.string.tag_LinkName)));
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		return view;
	}
}
