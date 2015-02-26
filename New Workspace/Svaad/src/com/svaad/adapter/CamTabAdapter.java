package com.svaad.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.svaad.fragment.History_Fragment;
import com.svaad.fragment.Search_Fragment;
import com.svaad.fragment.Search_Res_Fragment;

public class CamTabAdapter extends FragmentStatePagerAdapter {

	private String[] tabName;
	private String searchText;
	private String path;

	public CamTabAdapter(FragmentManager fm, String[] tabName,String searchText,String path) {
		super(fm);
		this.tabName = tabName;
		this.searchText=searchText;
		this.path=path;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabName[position];
	}

	@Override
	public int getCount() {
		return tabName.length;
	}

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;
		Bundle b = new Bundle();
		String menu = tabName[position];
		if (menu != null) {
			
			if (menu.equalsIgnoreCase("Search")) {

				fragment = new Search_Res_Fragment();
				b.putString("path", path);
				fragment.setArguments(b);

			}

			if (menu.equalsIgnoreCase("Near by")) {

				fragment = new Search_Fragment();
				b.putString("searchtags", searchText);
				b.putString("path", path);
				fragment.setArguments(b);

			}
			if (menu.equalsIgnoreCase("Recent")) {
				
				fragment = new History_Fragment();
				b.putString("path", path);
				fragment.setArguments(b);
//				fragment = new Search_Fragment();
			}

		}
		return fragment;
	}

}
