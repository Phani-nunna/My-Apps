package com.svaad.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.svaad.R;
import com.svaad.activity.HomeSearchActivity;
import com.svaad.adapter.VegAdapter;
import com.svaad.adapter.VegAdapter.ViewHolder;
import com.svaad.utils.Utils;

public class VegOrNonvegFragemnt extends Fragment {

	ListView locationListview;
	String[] vegNonArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dish_detail_list_layout, container, false);
		locationListview = (ListView) view.findViewById(R.id.dishDetailListView);

		vegNonArray = getActivity().getResources().getStringArray(
				R.array.veg_non_array);

		if (vegNonArray != null && vegNonArray.length > 0) {
			VegAdapter adapter = new VegAdapter(getActivity(),
					R.id.dishDetailListView, vegNonArray);
			locationListview.setAdapter(adapter);
		}

		locationListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				VegAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				holder.radio.setChecked(true);
				((HomeSearchActivity) getActivity())
						.setAllText(vegNonArray[position]);

				Utils.saveToSharedPreference("radioVegValue",
						vegNonArray[position]);
				Utils.saveToSharedPreferenceInt("radioVeg", position);

				if (locationListview.getAdapter() != null)
					((VegAdapter) locationListview.getAdapter())
							.notifyDataSetChanged();
			}
		});

		return view;
	}

}
