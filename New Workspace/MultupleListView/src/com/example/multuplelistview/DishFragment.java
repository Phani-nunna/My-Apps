package com.example.multuplelistview;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DishFragment extends Fragment {
	ListView dishlListView;
	DishesAdapter adapter;
	TextView resNameTxt ;
	String resname;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dish_item_layout, container,false);
		resNameTxt = (TextView)rootView.findViewById(R.id.tvListName);
		resname = getArguments().getString("resName");
		resNameTxt.setText(resname);
		return rootView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*ActionBar actionBar = getActivity().getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(R.color.actionbarColor));
		actionBar.setDisplayHomeAsUpEnabled(true);*/
	}

	
}
