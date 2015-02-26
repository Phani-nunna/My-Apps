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
import com.svaad.adapter.PriceAdapter;
import com.svaad.adapter.PriceAdapter.ViewHolder;
import com.svaad.utils.Utils;

public class NewPriceFragemnt extends Fragment {

	ListView locationListview;
	String[] priceRange;

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.dish_detail_list_layout, container, false);
		locationListview = (ListView) view.findViewById(R.id.dishDetailListView);

		priceRange = getActivity().getResources().getStringArray(
				R.array.price_range_array_new);

		// final List<String[]> priceRangeList = Constants.getPriceRange();

		if (priceRange != null && priceRange.length > 0) {
			PriceAdapter adapter = new PriceAdapter(getActivity(),
					R.id.dishDetailListView, priceRange);
			locationListview.setAdapter(adapter);
		}
		locationListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				PriceAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				holder.radio.setChecked(true);
				// ((NewHomeSearchActivity)getActivity()).setLocationText(locationsLists.get(position).getName());

				Utils.saveToSharedPreference("radioPriceValue",
						priceRange[position]);
				Utils.saveToSharedPreferenceInt("radioPrice",position);

				if (locationListview.getAdapter() != null)
					((PriceAdapter) locationListview.getAdapter())
							.notifyDataSetChanged();
			}
		});

		return view;
	}

}
