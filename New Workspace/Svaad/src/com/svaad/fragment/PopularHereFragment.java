package com.svaad.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.activity.RestaurantProfilesActivity;
import com.svaad.adapter.PopularHereAdapterR;
import com.svaad.databaseDAO.DatabaseDAO;
import com.svaad.utils.Constants;

public class PopularHereFragment extends Fragment {

	GridView gv;
	
	String catId=null;
	List<FeedDetailDto> feedList;
	TextView tvNoResults;
	List<FeedDetailDto> mostLovedLists;
	DatabaseDAO da;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.view_fullmenu, container, false);
		
		init(view);

		
		if (getArguments().getString(Constants.DATA) != null) {
			catId=getArguments().getString(Constants.DATA).toString();
			
			if(catId.equalsIgnoreCase("1"))
			{
				if (getArguments().getSerializable(Constants.DATA_mostloved) != null) {
					mostLovedLists=(List<FeedDetailDto>) getArguments().getSerializable(Constants.DATA_mostloved);
					if(mostLovedLists!=null && mostLovedLists.size()>0)
					{	
//						gv.setAdapter(new PopularHereAdapter(getActivity(), R.id.gridView,mostLovedLists));
						gv.setAdapter(new PopularHereAdapterR(getActivity(), R.id.gridView,mostLovedLists));
					}
					else
					{
						tvNoResults.setVisibility(View.VISIBLE);
						tvNoResults.setText("No mostloved dishes");
					}
					
				}
				
			}
			
			else if(catId!=null && catId.length()>0)
			{
				da=new DatabaseDAO(getActivity());
				feedList=da.getOrderByCatName(catId);
				if(feedList!=null && feedList.size()>0)
				{				
//					gv.setAdapter(new PopularHereAdapter(getActivity(), R.id.gridView,feedList));
					gv.setAdapter(new PopularHereAdapterR(getActivity(), R.id.gridView,feedList));
				}
			}
		}
		
		
		
		


		return view;

	}

	private void init(View view) {
		gv = (GridView) view.findViewById(R.id.gridView);	
		tvNoResults=(TextView)view.findViewById(R.id.tvNoResults);
	}
	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// TODO Auto-generated method stub
//		super.onCreateOptionsMenu(menu, inflater);
//		
//		inflater.inflate(R.menu.refresh_menu, menu);
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menu_load:
			
			
			
//			da = new DatabaseDAO(getActivity());
//
//			if (branchId != null & branchId.length() > 0) {
//
//				int cursor = da.checkBranchIdInBranchMenu(branchId);
//
//				if (cursor > 0) {
//					da.deleteBranchid(branchId);
//					da.deleteBranchMenu(branchId);
//
//					fragmentTransaction = getSupportFragmentManager()
//							.beginTransaction();
//					PullToviewMenu_Fragment pulltoviewfragment = new PullToviewMenu_Fragment();
//					Bundle b = new Bundle();
//					b.putString(Constants.DATA, branchId);
//					pulltoviewfragment.setArguments(b);
//					fragmentTransaction.replace(R.id.resContainer1,
//							pulltoviewfragment);
//					fragmentTransaction.commit();
//
//				}
//			}
//			
			
			
			break;

		default:
			break;
		}
		return true;
	}


}
