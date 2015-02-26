package com.svaad.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.svaad.R;

//public class SearchAdapter extends ArrayAdapter<FeedDetailDto> {
public class SearchAdapter extends ArrayAdapter<String> {
	Context context;
	LayoutInflater inflater;

	List<String> feedList;

	public SearchAdapter(Context context, int gridviewId, List<String> feedList) {

		super(context, gridviewId, feedList);
		this.context = context;
		this.feedList = feedList;
	}

	@Override
	public int getCount() {
		return feedList.size();
	}

	@Override
	public String getItem(int arg0) {
		return feedList.get(arg0);
	}

	public List<String> getFeedDtos() {
		return feedList;
	}

	public void setFeedDtos(List<String> feedList) {
		this.feedList = feedList;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup group) {
		final ViewHolder holder;

		String feed = getItem(arg0);

		if (view == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.search_item_row, group, false);

			holder = new ViewHolder();

			holder.tvResname = (TextView) view.findViewById(R.id.tvResname);
			holder.tvResLocation = (TextView) view
					.findViewById(R.id.tvResLocation);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (feed != null && feed.length() > 0)

		{
			holder.tvResname.setText(feed);
			holder.tvResLocation.setText(feed);
		} else {
			holder.tvResname.setText(null);
			holder.tvResLocation.setText(null);
		}

		return view;
	}

	public class ViewHolder {
		private TextView tvResname, tvResLocation;
	}

}
