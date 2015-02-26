package com.paradigmcreatives.audit;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.paradigmcreatives.audit.database.AuditQuestions;

public class CheckBoxAdaptor extends BaseAdapter {

	String[] check_optoins;
	public static ArrayList<String> selectedItem = new ArrayList<String>();
	WeakReference<AuditQuestions> insert;

	public CheckBoxAdaptor(AuditQuestions insert, String[] checkOptns) {
		this.insert = new WeakReference<AuditQuestions>(insert);
		check_optoins = checkOptns;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return check_optoins.length;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(insert.get());
			convertView = (View) mInflater
					.inflate(R.layout.checkbox_list, null);
		}
		final CheckBox chkbox = (CheckBox) convertView
				.findViewById(R.id.checkBox1);
		TextView txt = (TextView) convertView.findViewById(R.id.textView1);
		txt.setText(check_optoins[position]);
		chkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (chkbox.isChecked()) {
					selectedItem.add(check_optoins[position].toString());
					System.out.println("chled"
							+ check_optoins[position].toString());
				} else if (!chkbox.isChecked()) {
					System.out.println("unchled" + selectedItem.get(position));
					selectedItem.remove(check_optoins[position].toString());

				}

			}
		});

		return convertView;
	}

}
