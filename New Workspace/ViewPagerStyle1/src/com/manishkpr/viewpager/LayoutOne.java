
package com.manishkpr.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LayoutOne extends Fragment implements Update {
private String data;

public static Fragment newInstance(String string) {
		LayoutOne f = new LayoutOne();	
		System.out.println("phan one");
		// Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("key", string);
        f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("phani one frg");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.layout_one, null);		
		return root;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		data = getArguments().getString("key");
		Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void update(String xyzData) {
		System.out.println("phani one frg update");
		
	}
	
}
