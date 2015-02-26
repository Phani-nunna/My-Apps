package com.esainc.lib.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;

@SuppressLint("UseSparseArrays")
public class TeamsAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Teams Adapter";
	private Context mContext;
	private JSONArray mData;
	private SparseArray<Integer> mOffsets = new SparseArray<Integer>();
	private SparseArray<String> mHeaders = new SparseArray<String>();
	private String mSorter;
	
	public TeamsAdapter(Context context, JSONArray data, String sorter) {
		mContext = context;
		mData = data;
		mSorter = sorter;
		createHeaders();
	}
	
	private void createHeaders() {
		int currentOffset = 0;
		String currentHeader = "";
		
		// Loop through each item to find where data changes
		for (int i = 0; i < mData.length(); i++) {
			try {
				JSONObject hdata = mData.getJSONObject(i);
				String sortValue = hdata.getString(mSorter);
				if (!sortValue.equals(currentHeader)) {
					currentHeader = sortValue;
					if (mSorter.equalsIgnoreCase("gender")) {
						if (currentHeader.equalsIgnoreCase("Male")) {
							mHeaders.put(Integer.valueOf(i + currentOffset), mContext.getResources().getString(R.string.MaleSports));
						} else if (currentHeader.equalsIgnoreCase("Female")) {
							mHeaders.put(Integer.valueOf(i + currentOffset), mContext.getResources().getString(R.string.FemaleSports));
						} else {
							mHeaders.put(Integer.valueOf(i + currentOffset), mContext.getResources().getString(R.string.CoedSports));
						}
					} else if (mSorter.equalsIgnoreCase("season")) {
						if (currentHeader.equalsIgnoreCase("Fall")) {
							mHeaders.put(Integer.valueOf(i + currentOffset), mContext.getResources().getString(R.string.FallSports));
						} else if (currentHeader.equalsIgnoreCase("Winter")) {
							mHeaders.put(Integer.valueOf(i + currentOffset), mContext.getResources().getString(R.string.WinterSports));
						} else if (currentHeader.equalsIgnoreCase("Spring")) {
							mHeaders.put(Integer.valueOf(i + currentOffset), mContext.getResources().getString(R.string.SpringSports));
						} else {
							mHeaders.put(Integer.valueOf(i + currentOffset), mContext.getResources().getString(R.string.SummerSports));
						}
					}
					currentOffset++;
				}
				mOffsets.put(i + currentOffset, i);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error creating headers", e);
			}
			
		}
	}
	
	@Override
	public int getCount() {
		return mData.length() + mHeaders.size();
	}
	
	@Override
	public Object getItem(int pos) {
		try {
			if (mHeaders.get(pos) != null) {
				return mHeaders.get(pos);
			}
			int offset = mOffsets.get(pos);
			return mData.getJSONObject(offset);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting item", e);
		}
		return null;
	}
	
	@Override
	public long getItemId(int pos) {
		try {
			if (mHeaders.get(pos) != null) {
				return pos;
			}
			int offset = mOffsets.get(pos);
			return Long.valueOf(mData.getJSONObject(offset).getString(mContext.getResources().getString(R.string.tag_sportID))).longValue();
		} catch (NumberFormatException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		} catch (NotFoundException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		}
		return 0;
	}
	
	public void setData(JSONArray data) {
		mData = data;
		createHeaders();
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		boolean header = false;
		
		if (mHeaders.get(pos) != null) {
			header = true;
			if (convertView == null || convertView.findViewById(R.id.txtSportName) != null) {
				view = new View(mContext);
				view = inflater.inflate(R.layout.activity_teams_header, null);
			} else {
				view = convertView;
			}
		} else {
			if (convertView == null || convertView.findViewById(R.id.subheader) != null) {
				view = new View(mContext);
				view = inflater.inflate(R.layout.activity_teams_row, null);
			} else {
				view = convertView;
			}
		}
		
		if (header) {
			TextView subHeader = (TextView) view.findViewById(R.id.subheader);
			subHeader.setText(mHeaders.get(pos));
			view.setTag(mContext.getResources().getString(R.string.header));
		} else {
			// Load Team data
			try {
				int offset = mOffsets.get(pos);
				JSONObject hdata = mData.getJSONObject(offset);
				
				TextView txtSportName = (TextView) view.findViewById(R.id.txtSportName);
				txtSportName.setText(hdata.getString(mContext.getResources().getString(R.string.tag_sportName)));
				view.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_sportID)));
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error getting data", e);
			}
		}
		
		return view;
	}
	
}
