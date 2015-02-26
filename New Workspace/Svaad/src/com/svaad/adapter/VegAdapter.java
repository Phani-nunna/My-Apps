package com.svaad.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.utils.Utils;

public class VegAdapter extends ArrayAdapter<String> {

	SharedPreferences sharedPreferences = PreferenceManager
			.getDefaultSharedPreferences(SvaadApplication.getInstance()
					.getApplicationContext());
	private int listViewId;

	boolean[] checkBoxState;
	private int selected = -1;

	String[] priceRange;
	Context context;

	public VegAdapter(Context context, int resource, String[] priceRange) {
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
		private CheckBox checkLocation;
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

		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.locations_row, group, false);

			// checkBoxState = new boolean[priceRange.length];
			// holder = initHolder(view);
			holder = new ViewHolder();

			holder.tvSiteName = (TextView) view.findViewById(R.id.tvSiteName);
			// holder.checkLocation = (CheckBox) view
			// .findViewById(R.id.checkLocation);
			holder.radio = (RadioButton) view.findViewById(R.id.radio);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		// LocationDto location = locationsLists.get(arg0);
		holder.tvSiteName.setText(priceRange[position]);
		holder.radio.setTag(position);
		holder.radio.setChecked(position == selected);
		int userId = sharedPreferences.getInt("radioVeg", 0);
		if (userId == position) {
			holder.radio.setTag(position);
			holder.radio.setChecked(true);
			Utils.saveToSharedPreference("radioVegValue", priceRange[position]);
		}

//		holder.radio.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				selected = (Integer) arg0.getTag();
//				SharedPreferences.Editor editor = sharedPreferences.edit();
//				editor.putInt("radioVeg", position);
//				editor.putString("radioVegValue", priceRange[position]);
//				editor.commit();
//				notifyDataSetChanged();
//			}
//		});

		// holder.checkLocation.setChecked(checkBoxState[arg0]);
		//
		// holder.checkLocation.setOnClickListener(new View.OnClickListener() {
		//
		// public void onClick(View v) {
		// if (((CheckBox) v).isChecked()) {
		// checkBoxState[arg0] = true;
		// } else {
		// checkBoxState[arg0] = false;
		// }
		//
		// }
		// });

		return view;
	}

}
