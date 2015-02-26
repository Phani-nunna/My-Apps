package com.svaad.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.CamDishesDeatailDto;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class AddDishActivity extends BaseActivity implements OnClickListener {
	private ActionBar actionbar;
	private ImageView imageview;
	private EditText editText;
	private CheckBox checkBox;
	private Button btnAddDish;
	
	private boolean check=false;
	
	String path;
	
	CamRestaurantDetailDto restaurantDetail;
	
	RestaurantDetailsDto resaurantPageDetail;
	
	CamDishesDeatailDto dishDetail;
	
	 public static Activity mactivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.adddish_layout);
		actionbar = getActionBar();
		
		mactivity=this;

		setSupportProgressBarIndeterminateVisibility(false);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		
		actionbar.setTitle("Add a Dish");
		
		path = getIntent().getExtras().getString("path");
		restaurantDetail = (CamRestaurantDetailDto) (this.getIntent()
				.getExtras() != null ? this.getIntent().getExtras()
				.getSerializable(Constants.CAM_RES_DATA) : null);

		resaurantPageDetail = (RestaurantDetailsDto) (this.getIntent()
				.getExtras() != null ? this.getIntent().getExtras()
				.getSerializable(Constants.BRANCH_DETAILS) : null);

		initUi();
		
		if (path != null) {
			Bitmap bitmap = Utils.compressImage(path, this);
			imageview.setImageBitmap(bitmap);
		}
	}

	private void initUi() {
		imageview = (ImageView) findViewById(R.id.imageView1);
		editText = (EditText) findViewById(R.id.editText);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		btnAddDish = (Button) findViewById(R.id.btnAddDish);

		btnAddDish.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnAddDish:
			
			String text=editText.getText().toString();
			if(text!=null && text.length()>0 && !text.equalsIgnoreCase("")){
				
				if(checkBox.isChecked())
				{
					check=true;
				}
				else
				{
					check=false;
				}
				
				dishDetail=new CamDishesDeatailDto();
				
				dishDetail.setDishName(text);
				
				Intent i = new Intent(AddDishActivity.this,
						Ateit2_activity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra(Constants.CAM_DISHES_DATA, dishDetail);
				i.putExtra("nonveg", check);
				i.putExtra(Constants.ADD_DISH, Constants.ADD_DISH);
				i.putExtra(Constants.BRANCH_DETAILS,resaurantPageDetail);
				i.putExtra(Constants.CAM_RES_DATA,restaurantDetail);
				if (path != null) {
					i.putExtra("path", path);
				}
				startActivity(i);
				
				
			}
			else
			{
				new SvaadDialogs().showToast(AddDishActivity.this, "Add dish name");
			}
			
			break;

		default:
			break;
		}
	}

}
