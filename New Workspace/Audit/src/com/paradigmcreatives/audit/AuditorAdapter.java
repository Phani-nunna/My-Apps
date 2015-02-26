package com.paradigmcreatives.audit;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paradigmcreatives.audit.beans.Auditor_Schedules;

public class AuditorAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Auditor_Schedules> schedules;
	private LayoutInflater mLayoutInflater;

	public AuditorAdapter(
			AuditorScheduleListActivity auditorScheduleListActivity,
			ArrayList<Auditor_Schedules> schedules) {
		mContext = auditorScheduleListActivity;
		this.schedules = schedules;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {

		return schedules.size();
	}

	@Override
	public String getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.audit_list_child,
					null);
			holder = new ViewHolder();
			holder.slno = (TextView) convertView.findViewById(R.id.roll_no);
			holder.serialno = (TextView) convertView
					.findViewById(R.id.rserial_number);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.location = (TextView) convertView
					.findViewById(R.id.location);
			holder.info = (TextView) convertView.findViewById(R.id.info);
			holder.sync = (TextView) convertView.findViewById(R.id.sync);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if ((position % 2) == 0) {
			convertView.setBackgroundColor(Color.parseColor("#EEEEEE"));
		} else {
			convertView.setBackgroundColor(Color.WHITE);
		}
		holder.slno.setText(position + 1 + "");
		holder.serialno.setText(String.valueOf(schedules.get(position)
				.getAuditor_Emp_ID()));
		if (schedules.get(position).getSchedule_Date() != null) {
			if (schedules.get(position).getSchedule_Date().length() > 0) {
				holder.date.setText(schedules.get(position).getSchedule_Date());
			} else {
				holder.date.setText("not yet created");
			}
		} else
		holder.date.setText("not yet created");
		holder.location.setText(schedules.get(position).getState());
		holder.info.setText(schedules.get(position).getStatus());
		/*
		 * if(audit_sync[position].equals("yes")){
		 * holder.sync.setTextColor(Color.parseColor("#669900")); }else
		 * if(audit_sync[position].equals("no")){
		 * holder.sync.setTextColor(Color.parseColor("#CC0000")); }else{
		 * holder.sync.setTextColor(Color.BLACK); }
		 */
		holder.sync.setText("yes");

		return convertView;
	}

	static class ViewHolder {

		public TextView slno;
		public TextView serialno;
		public TextView date;
		public TextView location;
		public TextView info;
		public TextView sync;

	}

}
