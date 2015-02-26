package com.svaad.adapter;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.svaad.Dto.FeedDetailDto;
import com.svaad.fragment.WishesCountFragment;
import com.svaad.utils.Constants;

public class UserProfileTabsMenuAdapter extends FragmentStatePagerAdapter {

	String titles[];
	FeedDetailDto feedDetailDto;

	public UserProfileTabsMenuAdapter(FragmentManager fm, String titles[],FeedDetailDto feedDetailDto) {
		super(fm);
		this.titles = titles;
		this.feedDetailDto=feedDetailDto;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		return titles[position];

	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment f = null;

		String menu = titles[arg0];
		if (menu != null) {

			if (menu.length() > 0 && menu.equalsIgnoreCase("Wishlist")) {
				f = new WishesCountFragment();
				Bundle args = new Bundle();
				args.putSerializable(Constants.DATA, (Serializable) feedDetailDto);
				
				f.setArguments(args);
				
				
			}
//			if (menu.equalsIgnoreCase("Photos")) {
//
//				f = new BookMark_Fragment();
//				Bundle b = new Bundle();
//				b.putString("names", titles[arg0]);
//				f.setArguments(b);
//			}
//			if(menu.equalsIgnoreCase("Followers"))
//			{
//				f = new Call_Fragment();
////				Bundle b = new Bundle();
////				b.putString("names", titles[arg0]);
////				f.setArguments(b);
//			}
		}
		return f;

	}

}
