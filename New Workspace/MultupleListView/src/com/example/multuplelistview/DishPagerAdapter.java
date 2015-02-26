package com.example.multuplelistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DishPagerAdapter extends FragmentStatePagerAdapter{
	
    private String [] data;
    private int pos;
    private Context ctx;
	public DishPagerAdapter(FragmentManager fragmentManager,Context context, String[] items, int position) {
		super(fragmentManager);
		data = items;
		pos = position;
		ctx = context;
		System.out.println("celkon pager "+pos);
		
	}

	@Override
	public Fragment getItem(int poss) {
		System.out.println("celkon getItem pos = "+poss);
		Fragment fragment = null;
		
		Bundle args = new Bundle();
		args.putString("resName", data[poss]);
		
		fragment = Fragment.instantiate(ctx, DishFragment.class.getName(), args);
		return fragment;
	}

	@Override
	public int getCount() {
		return data.length;
	}
	@Override 
    public float getPageWidth(int position) {
        return(0.9f); 
    }

}
