package com.svaad.activity;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.facebook.Session;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.fragment.PhotoCountFragment;
import com.svaad.fragment.WishesCountFragment;
import com.svaad.utils.Constants;

public class PhotoActivity extends BaseActivity {

	FragmentTransaction fragmentTransaction;

	private PhotoCountFragment photoCountFragment;

	String loginUserId, profileUserId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.pull_activity_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setProgressBarIndeterminateVisibility(false);
		getSupportActionBar().setTitle("Photos");

		// init();

		if (this.getIntent().getExtras() != null) {
			loginUserId = this.getIntent().getExtras().getString("loginUserId");
			profileUserId = this.getIntent().getExtras()
					.getString("profileuserId");

		}

		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		photoCountFragment = new PhotoCountFragment();
		Bundle args = new Bundle();
		// args.putSerializable(Constants.DATA, (Serializable) feedDetailDto);
		args.putString("loginUserId", loginUserId);
		args.putString("profileuserId", profileUserId);

		photoCountFragment.setArguments(args);
		fragmentTransaction.replace(R.id.pullLinearLayout, photoCountFragment);
		fragmentTransaction.commit();

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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}
}
