package com.example.multuplelistview;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rajan on 24-08-2014.
 */
public class DishAdapter extends ArrayAdapter<String> {
	Context context;
	String[] items;
	int[] type;

	public DishAdapter(Context context, String[] items, int[] icons) {
		super(context, R.layout.svaad_list_item, items);
		this.context = context;
		this.items = items;
		this.type = type;
		System.out.println("phani dish adapter");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		// The convertView argument is essentially a "ScrapView" as described is
		// Lucas post
		// http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
		// It will have a non-null value when ListView is asking you recycle the
		// row layout.
		// So, when convertView is not null, you should simply update its
		// contents instead of inflating a new row layout.

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = (LayoutInflater) context
					.getApplicationContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.dish_item_layout, parent,
					false);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tvListName);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.listDishImageButton);
			// store the holder with the view.
			convertView.setTag(viewHolder);

		} else {
			// we've just avoided calling findViewById() on resource everytime
			// just use the viewHolder
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(items[position]);
		viewHolder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		return convertView;
	}

	public static class ViewHolder {
		TextView name;
		ImageView image;
	}

}
