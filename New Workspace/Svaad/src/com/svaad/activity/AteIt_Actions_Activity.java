package com.svaad.activity;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.asynctask.PhotoUploadFileAsyncTask;
import com.svaad.utils.Constants;
import com.svaad.utils.TextUtil;

public class AteIt_Actions_Activity extends BaseActivity {
	ProgressBar pb;
	EditText commentbox;
	GridView gv;
	ImageView image;
	TextView tag;
	FeedDetailDto feedDetailDto;
	String mediaPath;
	int height, width = 0;

	int lovedit, good, itsok, yuck;

	ActionBar actionbar;
	private String screen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ateit_actions_layout);
		init();

		pb.setMax(140);
		Intent b = getIntent();
		// String[] tags=b.getStringArrayExtra("tags");
		image.setImageResource(b.getIntExtra("image", 0));
		tag.setText(b.getStringExtra("tag"));

		actionbar = getActionBar();

		setSupportProgressBarIndeterminateVisibility(false);

		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(true);

		if (this.getIntent().getExtras() != null) {
			height = this.getIntent().getExtras().getInt(Constants.HEIGHT);
			width = this.getIntent().getExtras().getInt(Constants.WIDTH);
			feedDetailDto = (FeedDetailDto) getIntent().getExtras()
					.getSerializable(Constants.DATA);
			mediaPath = this.getIntent().getExtras()
					.getString(Constants.IMAGE_PATH);
				screen=this.getIntent().getExtras().getString(Constants.SVAAD);

			lovedit = this.getIntent().getExtras().getInt(Constants.LOVED_IT);
			good = this.getIntent().getExtras().getInt(Constants.GOOD);
			itsok = this.getIntent().getExtras().getInt(Constants.ITS_OK);
			yuck = this.getIntent().getExtras().getInt(Constants.YUCK);
		}

		gv.setAdapter(new ArrayAdapter<String>(this, R.layout.griditem,
				R.id.textView1, b.getStringArrayExtra("tags")));
		InputFilter[] filter = new InputFilter[1];
		filter[0] = new InputFilter.LengthFilter(140);
		commentbox.setFilters(filter);
		commentbox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				pb.setProgress(commentbox.getText().length());
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}
		});

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				commentbox.setText(commentbox.getText().toString()
						+ gv.getItemAtPosition(arg2).toString() + " ");
				commentbox.setSelection(commentbox.length());
				if (commentbox.getText().length() > 139)
					Toast.makeText(getApplicationContext(),
							"Oops!Reached 140 characters limit",
							Toast.LENGTH_SHORT).show();
			}

		});

	}

	public void init() {

		pb = (ProgressBar) findViewById(R.id.progressBar1);
		commentbox = (EditText) findViewById(R.id.editText1);
		gv = (GridView) findViewById(R.id.gridView1);
		image = (ImageView) findViewById(R.id.image);
		tag = (TextView) findViewById(R.id.tag);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.suggestion_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

//			Intent i = new Intent(AteIt_Actions_Activity.this,
//					AteIt_activity.class);
//			i.putExtra(Constants.DATA, feedDetailDto);
//			startActivity(i);
//			finish();
			break;

		case R.id.action_send:

			uploadFile();

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void uploadFile() {

		String commentText = commentbox.getText().toString();
		
		String screenname = null;
		if(screen!=null)
		{
			screenname=screen;
		}

		if (feedDetailDto != null) {

			//if (commentText != null && commentText.length() > 0) {

				List<String> usertags = TextUtil.getHashTags(commentText);

				feedDetailDto.setCommentText(commentText);

				if (lovedit != 0)
				{
					new PhotoUploadFileAsyncTask(this, feedDetailDto,
							mediaPath, usertags, height, width,lovedit,screenname).execute();

				} 
				else if (good != 0)

				{
					new PhotoUploadFileAsyncTask(this, feedDetailDto,
							mediaPath, usertags, height, width,good,screenname).execute();
				} 
				else if (itsok != 0) 
				{
					new PhotoUploadFileAsyncTask(this, feedDetailDto,
							mediaPath, usertags, height, width,itsok,screenname).execute();
				} 
				else 
				{
					new PhotoUploadFileAsyncTask(this, feedDetailDto,
							mediaPath, usertags, height, width,yuck,screenname).execute();
				}
			//}
//			else
//
//			{
//				Toast.makeText(AteIt_Actions_Activity.this,
//						"Please enter any suggestions", Toast.LENGTH_SHORT)
//						.show();
//			}
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(AteIt_Actions_Activity.this, AteIt_activity.class);
		i.putExtra(Constants.DATA, feedDetailDto);
		startActivity(i);
		finish();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
