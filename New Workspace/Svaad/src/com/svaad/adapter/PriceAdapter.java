package com.svaad.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class PriceAdapter extends ArrayAdapter<String> {
	SharedPreferences sharedPreferences = PreferenceManager
			.getDefaultSharedPreferences(SvaadApplication.getInstance()
					.getApplicationContext());

	private int listViewId;

	String[] priceRange;
	Context context;
	boolean[] checkBoxState;

	private int selected = -1;

	public PriceAdapter(Context context, int resource, String[] priceRange) {
		super(context, resource, priceRange);
		this.context = context;
		this.priceRange = priceRange;
		listViewId = resource;
	}

	@Override
	public int getCount() {
		return priceRange.length;
	}

	@Override
	public String getItem(int arg0) {
		return priceRange[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public class ViewHolder {
		private TextView tvSiteName;
		// private CheckBox checkLocation;

		public RadioButton radio;
	}

	private ViewHolder initHolder(View convertView) {

		ViewHolder holder = new ViewHolder();

		holder.tvSiteName = (TextView) convertView
				.findViewById(R.id.tvSiteName);

		// holder.checkLocation = (CheckBox) convertView
		// .findViewById(R.id.checkLocation);

		return holder;
	}

	@Override
	public View getView(final int position, View view, ViewGroup group) {
		final List<String[]> priceRangeList = Constants.getPriceRange();
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.locations_row, group, false);

			holder = new ViewHolder();

			holder.tvSiteName = (TextView) view.findViewById(R.id.tvSiteName);

			holder.radio = (RadioButton) view.findViewById(R.id.radio);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.tvSiteName.setText(priceRange[position]);

		holder.radio.setTag(position);
		holder.radio.setChecked(position == selected);

		int userId = sharedPreferences.getInt("radioPrice", 0);
		if (userId == position) {
			holder.radio.setTag(position);
			holder.radio.setChecked(true);
			Utils.saveToSharedPreference("radioPriceValue",
					priceRange[position]);
		}

		return view;
	}

}
