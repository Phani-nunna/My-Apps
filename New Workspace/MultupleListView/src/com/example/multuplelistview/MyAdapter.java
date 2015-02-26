package com.example.multuplelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private Context ctx;
	String []items;
	public MyAdapter(Context context, String[] list1Items) {
		ctx = context;
		items = list1Items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View)inflater.inflate(R.layout.row, null);
			mViewHolder = new ViewHolder();
			mViewHolder.txt = (TextView)convertView.findViewById(R.id.rowtxt);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		mViewHolder.txt.setText(items[position]);
		return convertView;
	}

	public class ViewHolder{
		TextView txt;
	}
}
