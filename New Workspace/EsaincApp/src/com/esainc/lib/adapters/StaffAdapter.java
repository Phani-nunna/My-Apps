package com.esainc.lib.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Messenger;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.ui.StaffFragment;

@SuppressLint("UseSparseArrays")
public class StaffAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Staff Adapter";
	private Context mContext;
	private JSONArray mData;
	private SparseArray<Integer> mOffsets = new SparseArray<Integer>();
	private SparseArray<String> mHeaders = new SparseArray<String>();
	
	public StaffAdapter(Context context, JSONArray data) {
		mContext = context;
		mData = data;
		createHeaders();
	}
	
	private void createHeaders() {
		int currentOffset = 0;
		String currentHeader = "";
		
		// Loop through each item to find where data changes
		for (int i = 0; i < mData.length(); i++) {
			try {
				JSONObject hdata = mData.getJSONObject(i);
				String type = hdata.getString(mContext.getResources().getString(R.string.tag_type));
				String sport = hdata.getString(mContext.getResources().getString(R.string.tag_sport));
				// If sport or type changed, add new header
				if ((!TextUtils.isEmpty(sport) && !sport.equals(currentHeader)) || (TextUtils.isEmpty(sport) && !type.equals(currentHeader))) {
					currentHeader = (!TextUtils.isEmpty(sport)) ? sport : type;
					mHeaders.put(Integer.valueOf(i + currentOffset), currentHeader);
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
			return Long.valueOf(mData.getJSONObject(offset).getString(mContext.getResources().getString(R.string.tag_staffID))).longValue();
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
			if (convertView == null || convertView.findViewById(R.id.txtName) != null) {
				view = new View(mContext);
				view = inflater.inflate(R.layout.activity_staff_header, null);
			} else {
				view = convertView;
			}
		} else {
			if (convertView == null || convertView.findViewById(R.id.subheader) != null) {
				view = new View(mContext);
				view = inflater.inflate(R.layout.activity_staff_row, null);
			} else {
				view = convertView;
			}
		}
		
		if (header) {
			// Load Header data
			TextView subHeader = (TextView) view.findViewById(R.id.subheader);
			subHeader.setText(mHeaders.get(pos));
		} else {
			// Load Staff data
			try {
				int offset = mOffsets.get(pos);
				JSONObject hdata = mData.getJSONObject(offset);
				
				TextView txtName = (TextView) view.findViewById(R.id.txtName);
				TextView txtPosition = (TextView) view.findViewById(R.id.txtPosition);
				TextView txtPhoneNumber = (TextView) view.findViewById(R.id.txtPhoneNumber);
				ImageView btnBio = (ImageView) view.findViewById(R.id.btnBio);
				ImageView btnEmail = (ImageView) view.findViewById(R.id.btnEmail);
				
				txtName.setText(hdata.getString(mContext.getResources().getString(R.string.tag_firstName)) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_lastName)));
				txtPosition.setText(hdata.getString(mContext.getResources().getString(R.string.tag_position)));
				String phoneNumber = hdata.getString(mContext.getResources().getString(R.string.tag_phone));
				txtPhoneNumber.setText(phoneNumber);
				if (!TextUtils.isEmpty(phoneNumber) && !((SIDHelpApplication) mContext.getApplicationContext()).isTablet()) {
					txtPhoneNumber.setTag(phoneNumber);
					txtPhoneNumber.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_DIAL);
							intent.setData(Uri.parse("tel:" + v.getTag().toString().trim()));
							mContext.startActivity(intent);
						}
					});
				}
				if (((SIDHelpApplication) mContext.getApplicationContext()).isTablet()) txtPhoneNumber.setTextColor(mContext.getResources().getColor(android.R.color.black));
				
				if (hdata.getString(mContext.getResources().getString(R.string.tag_hasBio)).equals("0")) {
					btnBio.setVisibility(View.INVISIBLE);
				} else {
					btnBio.setVisibility(View.VISIBLE);
					btnBio.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_staffID)));
					btnBio.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// Load staff bio data
							Logger.i(LOG_TAG, "Loading Article");
							Intent dataIntent = new Intent(StaffFragment.getInstance().getActivity(), DataLoader.class);
							Messenger dataMessenger = new Messenger(StaffFragment.mHandler);
							dataIntent.putExtra("MESSENGER", dataMessenger);
							dataIntent.putExtra("tabPos", StaffFragment.mPos);
							dataIntent.putExtra("type", DataType.StaffBio.ordinal());
							String params[] = new String[] {v.getTag().toString()};
							dataIntent.putExtra("params", params);
							StaffFragment.getInstance().getActivity().startService(dataIntent);
						}
					});
				}
				
				btnEmail.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_email)));
				btnEmail.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_SENDTO);
						intent.setData(Uri.parse("mailto:" + v.getTag()));
						mContext.startActivity(intent);
					}
				});
				
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error getting data", e);
			}
		}
		return view;
	}
}
