package com.svaad.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.svaad.fragment.HomeSearchGridFragments;
import com.svaad.fragment.MeFragment;
import com.svaad.fragment.MebeferLoginFragment;
import com.svaad.fragment.Svaad_Fragment_Spinner;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class HomeTabAdapter extends FragmentStatePagerAdapter implements
		IconTabProvider {

	private String[] tabName;
	private int[] icons;

	public HomeTabAdapter(FragmentManager fm, String[] tabName, int[] icons) {
		super(fm);
		this.tabName = tabName;
		this.icons = icons;
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
//				fragment = new HomeFragments();
//				fragment = new HomeSearchFragments();
				fragment = new HomeSearchGridFragments();
			}

			if (menu.equalsIgnoreCase("Svaad")) {

				fragment = new Svaad_Fragment_Spinner();

			}
			if (menu.equalsIgnoreCase("Me")) {

				String userId = Utils
						.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

				if (userId != null && userId.length() > 0) {
					fragment = new MeFragment();
				} else {
					fragment = new MebeferLoginFragment();

					b.putInt(Constants.PAGER, Constants.PAGER_MODE_ME);
					fragment.setArguments(b);
				}

			}

		}
		return fragment;
	}

	@Override
	public int getPageIconResId(int position) {
		return icons[position];
	}

	
}
