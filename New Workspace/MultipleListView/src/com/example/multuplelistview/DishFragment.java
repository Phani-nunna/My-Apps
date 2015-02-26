package com.example.multuplelistview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DishFragment extends Fragment {
	ListView dishlListView;
	DishesAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dish_layout, container,false);
		dishlListView = (ListView)rootView.findViewById(R.id.dish_list);
		
		return rootView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
