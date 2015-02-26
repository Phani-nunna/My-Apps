package com.svaad.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.CamDishBranchIdDto;
import com.svaad.Dto.CamDishNameDto;
import com.svaad.Dto.CamDishesDeatailDto;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.adapter.CamDishSearchAdapter;
import com.svaad.asynctask.CamSearchDishAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.CamDishRequestDto;
import com.svaad.responseDto.CamDishesResponseDto;
import com.svaad.utils.Constants;
import com.svaad.whereDto.CamDishWhereDto;

public class SearchDish_Activity extends BaseActivity implements
		OnClickListener, SvaadFeedCallback, SvaadProgressCallback {

	ActionBar actionBar;
	ListView listView;
	ImageButton btnback;
	private CamRestaurantDetailDto restaurantDetail;
	private CamDishesResponseDto camDishesResponseDto;
	private ProgressBar pbar;
	private EditText editText;

	CamDishSearchAdapter adapter;
	private TextView tvNoDishes;
	private String path;
	private RestaurantDetailsDto restaurantPageDetail;
	public static Activity mactivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_dish_layout);
		actionBar = getActionBar();

		mactivity = this;

		setSupportProgressBarIndeterminateVisibility(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setTitle("Choose a dish");

		// actionBar.hide();

		init();

		initializeList();

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			restaurantDetail = (CamRestaurantDetailDto) (this.getIntent()
					.getExtras() != null ? this.getIntent().getExtras()
					.getSerializable(Constants.DATA) : null);
			restaurantPageDetail = (RestaurantDetailsDto) (this.getIntent()
					.getExtras() != null ? this.getIntent().getExtras()
					.getSerializable(Constants.BRANCH_DETAILS) : null);
			path = this.getIntent().getExtras().getString("path");
			getSearchList();

			adapter = new CamDishSearchAdapter(SearchDish_Activity.this, 0,
					camDishesResponseDto.getResults(), restaurantPageDetail,
					restaurantDetail, path);
			listView.setAdapter(adapter);

			// listView.setAdapter(new CamDishSearchAdapter(
			// SearchDish_Activity.this, 0, camDishesResponseDto
			// .getResults()));

		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (camDishesResponseDto.getResults() != null) {
					CamDishesDeatailDto dishDetail = camDishesResponseDto
							.getResults().get(arg2);

					if (restaurantPageDetail != null) {

						if (dishDetail != null) {
							Intent i = new Intent(SearchDish_Activity.this,
									Ateit2_activity.class);
							// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.putExtra(Constants.CAM_DISHES_DATA, dishDetail);
							i.putExtra(Constants.BRANCH_DETAILS,
									restaurantPageDetail);
							if (path != null) {
								i.putExtra("path", path);
							}
							startActivity(i);

						}
					} else {
						if (dishDetail != null) {
							Intent i = new Intent(SearchDish_Activity.this,
									Ateit2_activity.class);
							// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.putExtra(Constants.CAM_DISHES_DATA, dishDetail);
							i.putExtra(Constants.CAM_RES_DATA, restaurantDetail);
							if (path != null) {
								i.putExtra("path", path);
							}
							startActivity(i);
						}
					}
				}

			}
		});

	}

	private void initializeList() {
		if (camDishesResponseDto == null) {
			camDishesResponseDto = new CamDishesResponseDto();
			camDishesResponseDto
					.setResults(new ArrayList<CamDishesDeatailDto>());
		} else if (camDishesResponseDto.getResults() == null) {
			camDishesResponseDto
					.setResults(new ArrayList<CamDishesDeatailDto>());
		}
	}

	private void getSearchList() {
		CamSearchDishAsynctask asy = new CamSearchDishAsynctask(
				SearchDish_Activity.this, null, getDishesRequestDtoFor());
		asy.execute();
	}

	private CamDishRequestDto getDishesRequestDtoFor() {

		CamDishRequestDto camDishRequestDto = new CamDishRequestDto();
		camDishRequestDto.setLimit(1000);
		camDishRequestDto.setKeys("dishName");
		camDishRequestDto.setOrder("dishName");
		camDishRequestDto.set_method("GET");

		CamDishBranchIdDto branchIdDto = new CamDishBranchIdDto();
		branchIdDto.set__type("Pointer");
		branchIdDto.setClassName("Branches");

		if (restaurantDetail != null) {
			String objectId = restaurantDetail.getObjectId();
			branchIdDto.setObjectId(objectId);
		} else if (restaurantPageDetail != null) {
			String objectId = restaurantPageDetail.getObjectId();
			branchIdDto.setObjectId(objectId);
		}

		CamDishNameDto dishNameDto = new CamDishNameDto();
		dishNameDto.set$exists(true);

		CamDishWhereDto whereDto = new CamDishWhereDto();
		whereDto.setBranchId(branchIdDto);
		whereDto.setDishName(dishNameDto);
		camDishRequestDto.setWhere(whereDto);
		return camDishRequestDto;
	}

	private void init() {

		listView = (ListView) findViewById(R.id.listView);
		pbar = (ProgressBar) findViewById(R.id.pbar);
		btnback = (ImageButton) findViewById(R.id.btnBack);
		tvNoDishes = (TextView) findViewById(R.id.tvNoDishes);
		editText = (EditText) findViewById(R.id.editText1);
		btnback.setOnClickListener(this);

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (camDishesResponseDto.getResults() != null
						&& camDishesResponseDto.getResults().size() > 0) {

					String text = editText.getText().toString()
							.toLowerCase(Locale.getDefault());
					adapter.filter(text);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:

			finish();

			break;

		default:
			break;
		}
	}

	@Override
	public void setResponse(Object object) {
		if (object == null && (object instanceof CamDishesResponseDto)) {
			return;
		}
		addResult((CamDishesResponseDto) object);
		CamDishSearchAdapter restaurantListAdapter = (CamDishSearchAdapter) listView
				.getAdapter();
		if (restaurantListAdapter != null) {
			restaurantListAdapter.setFeedDtos(this.camDishesResponseDto
					.getResults());
			restaurantListAdapter.notifyDataSetChanged();
		}
	}

	private void addResult(CamDishesResponseDto newRestaurantListResponseDto) {
		if (newRestaurantListResponseDto != null
				&& newRestaurantListResponseDto.getResults() != null
				&& newRestaurantListResponseDto.getResults().size() > 0) {
			List<CamDishesDeatailDto> newRestaurantDetailsDtos = newRestaurantListResponseDto
					.getResults();

			CamDishesDeatailDto cam = new CamDishesDeatailDto();
			cam.setDishName("AddDish");
			newRestaurantDetailsDtos.add(cam);

			this.camDishesResponseDto.getResults().addAll(
					newRestaurantDetailsDtos);
		} else {
//			tvNoDishes.setVisibility(View.VISIBLE);
			
			List<CamDishesDeatailDto> newRestaurantDetailsDtos = new ArrayList<CamDishesDeatailDto>();
			CamDishesDeatailDto cam = new CamDishesDeatailDto();
			cam.setDishName("AddDish");
			newRestaurantDetailsDtos.add(cam);
			this.camDishesResponseDto.getResults().addAll(
					newRestaurantDetailsDtos);
		}

	}

	@Override
	public void progressOn() {

		pbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void progressOff() {
		pbar.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
