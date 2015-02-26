package com.svaad.adapter;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.svaad.Dto.FeedDetailDto;
import com.svaad.fragment.PopularHereFragment;
import com.svaad.utils.Constants;

public class ViewerMenuAdapter extends FragmentStatePagerAdapter {

	List<FeedDetailDto> feedList;
	List<FeedDetailDto> mostLovedLists;
	String catId = null;
	String name = null;
	int val;

	public ViewerMenuAdapter(FragmentManager fm,
			int val, List<FeedDetailDto> feeedList,List<FeedDetailDto>mostLovedLists ) {
		super(fm);
		this.val = val;
		this.feedList = feeedList;
		this.mostLovedLists=mostLovedLists;
	}
	


	@Override
	public CharSequence getPageTitle(int position) {

		if (feedList.get(position).getCatId() != null) {
			name = feedList.get(position).getCatId().getName();
			catId = feedList.get(position).getCatId().getObjectId();
			if (name != null && name.length() > 0) {
				return name;
			}

		}
		return name;

	}

	@Override
	public int getCount() {
		return feedList.size();
	}

	@Override
	public Fragment getItem(int arg0) {

		Fragment fragment = null;

		String menu = name;
		if (menu != null) {
			
			if (feedList.get(arg0).getCatId() != null)
			{
				catId = feedList.get(arg0).getCatId().getObjectId();
				if (catId != null && catId.length() > 0) 
				{
					
					fragment = new PopularHereFragment();

					Bundle args = new Bundle();
					
					args.putString(Constants.DATA, catId);
					args.putSerializable(Constants.DATA_mostloved, (Serializable) mostLovedLists);
					fragment.setArguments(args);
				}

			}



		}
		return fragment;

	}

}
