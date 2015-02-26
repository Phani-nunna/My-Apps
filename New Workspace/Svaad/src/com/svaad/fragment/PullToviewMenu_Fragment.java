package com.svaad.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CatIdDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.activity.MenuViewer_Activity;
import com.svaad.asynctask.RestaurantAllDishesAsynctask;
import com.svaad.databaseDAO.DatabaseDAO;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.RestaurantAllDishesRequestDto;
import com.svaad.responseDto.RestaurantAllDishesResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.whereDto.RestaurantAllDishesWhereDto;

public class PullToviewMenu_Fragment extends Fragment implements
		SvaadFeedCallback, OnClickListener {

	View view;
	LinearLayout ll5;
	RestaurantAllDishesResponseDto restaurantAllDishesResponseDto;
	// private List<BranchMenuDto> categoriesList;
	private List<FeedDetailDto> categoriesList;

	LayoutInflater layoutInflater;
	private TextView tvCategories;
	String branchId;
	DatabaseDAO da;
	ArrayList<String> namesList = new ArrayList<String>();
	private RelativeLayout rlAc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// if (savedInstanceState == null)
		// {
		// {
		// if(this.getArguments()!=null)
		// {
		// String branchId=this.getArguments().getString(Constants.DATA);
		// if(branchId!=null && branchId.length()>0)
		// {
		// getRestaurantmenuList(branchId);
		// }
		//
		// }
		// }
		// }
	}

	private void getRestaurantmenuList(String branchId) {
		new RestaurantAllDishesAsynctask(getActivity(),
				PullToviewMenu_Fragment.this,
				getRestaurantAllDishesRequestDto(branchId)).execute();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.pulltoviewmenu_fragment, container,
				false);
	
		initUi(view);
		da = new DatabaseDAO(getActivity());
		if (this.getArguments() != null) {
			branchId = this.getArguments().getString(Constants.DATA);
			if (branchId != null && branchId.length() > 0) {
				int cursor = da.checkBranchIdRow(branchId);

				if (cursor == 1) {
					categoriesList = da.getgroupByCatNames(branchId);
					if (categoriesList != null && categoriesList.size() > 0) {
						setCategoriesToScrollview(categoriesList);
					}
				} else {

					getRestaurantmenuList(branchId);
				}
			}

		}

		return view;
	}

	private void initUi(View view) {
		ll5 = (LinearLayout) view.findViewById(R.id.ll5);
	}

	private RestaurantAllDishesRequestDto getRestaurantAllDishesRequestDto(
			String branchId) {
		RestaurantAllDishesRequestDto allDishesRequestDto = new RestaurantAllDishesRequestDto();
		RestaurantAllDishesWhereDto allDishesWhereDto = new RestaurantAllDishesWhereDto();
		BranchIdDto branchIdDto = new BranchIdDto();

		branchIdDto.setObjectId(branchId);
		branchIdDto.setClassName("Branches");
		branchIdDto.set__type("Pointer");
		allDishesWhereDto.setBranchId(branchIdDto);
		allDishesWhereDto.setPublish(true);
		allDishesRequestDto.set_method("GET");
		allDishesRequestDto.setInclude("dishId,catId,location");
		allDishesRequestDto.setLimit(1000);
		allDishesRequestDto.setOrder("-updatedAt");
		allDishesRequestDto.setWhere(allDishesWhereDto);
		return allDishesRequestDto;
	}

	@Override
	public void setResponse(Object object) {

		restaurantAllDishesResponseDto = (RestaurantAllDishesResponseDto) object;

		List<FeedDetailDto> dishDetailsLists = restaurantAllDishesResponseDto
				.getResults();
		try {

			if (dishDetailsLists != null && dishDetailsLists.size() > 0) {
				da.insertBranchMenu(dishDetailsLists);
				da.insertBranchIds(branchId);
				categoriesList = da.getgroupByCatNames(branchId);

				if (categoriesList != null && categoriesList.size() > 0) {

					setCategoriesToScrollview(categoriesList);
				} else {
					new SvaadDialogs()
							.showToast(getActivity(), "No categories");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setCategoriesToScrollview(List<FeedDetailDto> categorieslist) {
		layoutInflater = getActivity().getLayoutInflater();
		int i;
		String catName = null;
		// String catid=null;
		for (i = 0; i < categorieslist.size(); i++) {
			View view = layoutInflater.inflate(R.layout.categories_row, ll5,
					false);

			rlAc = (RelativeLayout) view.findViewById(R.id.rlAc);
			tvCategories = (TextView) view.findViewById(R.id.tvCategories);
			CatIdDto catId = categorieslist.get(i).getCatId();
			if (catId != null) {

				catName = catId.getName();
				// catid=catId.getObjectId();
				namesList.add(catName);
			}

			if (catName != null && catName.length() > 0) {
				tvCategories.setText(catName);
			}
			rlAc.setOnClickListener(this);
			rlAc.setTag(i);

			ll5.addView(view);

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlAc:

			Integer position = (Integer) v.getTag();

			Intent i = new Intent(getActivity(), MenuViewer_Activity.class);
			i.putExtra("val", position);
			Bundle args = new Bundle();
			args.putSerializable(Constants.DATA, (Serializable) categoriesList);
			i.putExtras(args);
			startActivity(i);

			break;

		default:
			break;
		}
	}
	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// TODO Auto-generated method stub
//		super.onCreateOptionsMenu(menu, inflater);
//		
//		inflater.inflate(R.menu.share_menu, menu);
//	}

}
