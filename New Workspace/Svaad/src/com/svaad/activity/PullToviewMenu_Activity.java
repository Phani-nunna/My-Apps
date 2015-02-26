package com.svaad.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CatIdDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.asynctask.RestaurantAllDishesAsynctask;
import com.svaad.databaseDAO.DatabaseDAO;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.RestaurantAllDishesRequestDto;
import com.svaad.responseDto.RestaurantAllDishesResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.whereDto.RestaurantAllDishesWhereDto;

public class PullToviewMenu_Activity extends BaseActivity implements
		SvaadFeedCallback {

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
	private FragmentTransaction fragmentTransaction;
	private MenuViewer_Activity homeFragment;

	private List<FeedDetailDto> mostLovedLists;
	private String resName;
	private ProgressBar pbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.pulltoviewmenu_fragment);
		setContentView(R.layout.home_activity_layout);
		final ActionBar actionBar = getActionBar();
		setSupportProgressBarIndeterminateVisibility(false);

		actionBar.setDisplayUseLogoEnabled(true);

		actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setDisplayShowHomeEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);

		// initUi(view);

		try {

			da = new DatabaseDAO(PullToviewMenu_Activity.this);
			if (this.getIntent().getExtras() != null) {
				branchId = this.getIntent().getExtras()
						.getString(Constants.DATA);

				resName = this.getIntent().getExtras().getString("resName");

				if (resName != null && resName.length() > 0) {
					actionBar.setTitle(resName);
				}

				if (branchId != null && branchId.length() > 0) {
					int cursor = da.checkBranchIdRow(branchId);

					if (cursor == 1) {

						// getting mostloved dishes from db (only 10 dishes with
						// descending order)

						mostLovedLists = da.getBranchMenusForOneTag(branchId);
						// int count=mostLovedLists.size();

						categoriesList = da.getgroupByCatNames(branchId);

						// Adding mostloved name first in view pager
						FeedDetailDto feed = new FeedDetailDto();
						CatIdDto catId = new CatIdDto();
						catId.setObjectId("1");
						catId.setName("Most Loved");
						feed.setCatId(catId);
						categoriesList.add(0, feed);

						if (categoriesList != null && categoriesList.size() > 0) {

							setCategoriesToScrollview(categoriesList,
									mostLovedLists);
						}
					} else {

						getRestaurantmenuList(branchId);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// da.close();//CLOSING THE DATABASE
		}

	}

	private void getRestaurantmenuList(String branchId) {

		try {
			new RestaurantAllDishesAsynctask(PullToviewMenu_Activity.this,
					null, getRestaurantAllDishesRequestDto(branchId)).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initUi(View view) {
		// ll5 = (LinearLayout) view.findViewById(R.id.ll5);

		pbar = (ProgressBar) view.findViewById(R.id.pbar);
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

				// adding mostloved name first in view pager
				FeedDetailDto feed = new FeedDetailDto();
				CatIdDto catId = new CatIdDto();
				catId.setObjectId("1");
				catId.setName("Most Loved");
				feed.setCatId(catId);
				categoriesList.add(0, feed);
				// geting mostloved dishes list in db
				mostLovedLists = da.getBranchMenusForOneTag(branchId);

				if (categoriesList != null && categoriesList.size() > 0) {

					setCategoriesToScrollview(categoriesList, mostLovedLists);
				} else {
					new SvaadDialogs().showToast(PullToviewMenu_Activity.this,
							"No categories");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setCategoriesToScrollview(List<FeedDetailDto> categorieslist,
			List<FeedDetailDto> mostlovedlists) {
		// layoutInflater =PullToviewMenu_Activity.this.getLayoutInflater();
		// int i;
		// String catName = null;
		// // String catid=null;
		// for (i = 0; i < categorieslist.size(); i++) {
		// View view = layoutInflater.inflate(R.layout.categories_row, ll5,
		// false);
		//
		// rlAc = (RelativeLayout) view.findViewById(R.id.rlAc);
		// tvCategories = (TextView) view.findViewById(R.id.tvCategories);
		// CatIdDto catId = categorieslist.get(i).getCatId();
		// if (catId != null) {
		//
		// catName = catId.getName();
		// // catid=catId.getObjectId();
		// namesList.add(catName);
		// }
		//
		// if (catName != null && catName.length() > 0) {
		// tvCategories.setText(catName);
		// }
		// rlAc.setOnClickListener(this);
		// rlAc.setTag(i);
		//
		// ll5.addView(view);

		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		// fragmentTransaction.replace(R.id.content_frame, new
		// HomeTabsFragment());
		homeFragment = new MenuViewer_Activity();
		Bundle args = new Bundle();
		args.putSerializable(Constants.DATA, (Serializable) categorieslist);
		args.putSerializable(Constants.DATA_mostloved,
				(Serializable) mostlovedlists);
		homeFragment.setArguments(args);
		fragmentTransaction.replace(R.id.homeLinearLayout, homeFragment);
		fragmentTransaction.commit();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

			break;

//		case R.id.menu_load:
//
//			da = new DatabaseDAO(PullToviewMenu_Activity.this);
//
//			if (branchId != null & branchId.length() > 0) {
//
//				int cursor = da.checkBranchIdInBranchMenu(branchId);
//
//				if (cursor > 0) {
//					da.deleteBranchid(branchId);
//					da.deleteBranchMenu(branchId);
//
//					// getRestaurantmenuList(branchId);
//
//				}
//			}
//
//			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.refresh_menu, menu);
//
//		return super.onCreateOptionsMenu(menu);
//
//	}

	// @Override
	// public void progressOn() {
	//
	// pbar.setVisibility(View.VISIBLE);
	//
	// }
	//
	// @Override
	// public void progressOff() {
	//
	// pbar.setVisibility(View.GONE);
	//
	// }
}
