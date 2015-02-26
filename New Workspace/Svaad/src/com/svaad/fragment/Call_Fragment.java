package com.svaad.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.utils.Constants;

public class Call_Fragment extends Fragment implements OnClickListener {
	View view;
	RelativeLayout ll1, ll2, ll3, ll4;
	TextView tvPhoneNo1, tvPhoneNo2, tvPhoneNo3, tvPhoneNo4,tvCall;
	RestaurantDetailsDto restaurantDetailsDto;
	ImageView im3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		restaurantDetailsDto = (RestaurantDetailsDto) (getArguments() != null ? getArguments()
				.getSerializable(Constants.DATA) : null);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.call_fragment, container, false);
		initUi(view);

		if (restaurantDetailsDto != null) {
			String phoneno1 = restaurantDetailsDto.getPhone1();
			String phoneno2 = restaurantDetailsDto.getPhone2();
			String phoneno3 = restaurantDetailsDto.getPhone3();
			String phoneno4 = restaurantDetailsDto.getPhone4();
			
			String resName=restaurantDetailsDto.getBranchName();
			if(resName!=null&& resName.length()>0)
			{
				tvCall.setText("Call "+resName);
			}
			
			
			if (phoneno1 != null && phoneno1.length() > 0) {
				tvPhoneNo1.setText(phoneno1);
			} else {
				ll1.setVisibility(View.GONE);
			}

			if (phoneno2 != null && phoneno2.length() > 0) {
				tvPhoneNo2.setText(phoneno2);
			} else {
				ll2.setVisibility(View.GONE);
			}

			if (phoneno3 != null && phoneno3.length() > 0) {
				tvPhoneNo3.setText(phoneno3);
			} else {
				ll3.setVisibility(View.GONE);
			}

			if (phoneno4 != null && phoneno4.length() > 0) {
				tvPhoneNo4.setText(phoneno4);
			} else {
				ll4.setVisibility(View.GONE);
			}

			if (phoneno1 == null && phoneno2 == null && phoneno3 == null
					&& phoneno4 == null) {
				ll3.setVisibility(View.VISIBLE);
				im3.setVisibility(View.GONE);
				tvPhoneNo3.setText("No information available");
			}

		}

		return view;
	}

	private void initUi(View view) {

		ll1 = (RelativeLayout) view.findViewById(R.id.ll1);
		ll2 = (RelativeLayout) view.findViewById(R.id.ll2);
		ll3 = (RelativeLayout) view.findViewById(R.id.ll3);
		ll4 = (RelativeLayout) view.findViewById(R.id.ll4);

		tvPhoneNo1 = (TextView) view.findViewById(R.id.tvPhoneNo1);
		tvPhoneNo2 = (TextView) view.findViewById(R.id.tvPhoneNo2);
		tvPhoneNo3 = (TextView) view.findViewById(R.id.tvPhoneNo3);
		tvPhoneNo4 = (TextView) view.findViewById(R.id.tvPhoneNo4);
		tvCall=(TextView)view.findViewById(R.id.tvCall);

		im3 = (ImageView) view.findViewById(R.id.imageView3);
		
		ll1.setOnClickListener(this);
		ll2.setOnClickListener(this);
		ll3.setOnClickListener(this);
		ll4.setOnClickListener(this);

	}

	public void selectedPhoneNo(String phoneNo) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNo));
		startActivity(callIntent);
	}

	@Override
	public void onClick(View v) {
		String text;
		switch (v.getId()) {
		case R.id.ll1:

			text = tvPhoneNo1.getText().toString();

			if (text != null && text.length() > 0) {
				selectedPhoneNo(text);
			}

			break;
		case R.id.ll2:

			text = tvPhoneNo2.getText().toString();

			if (text != null && text.length() > 0) {
				selectedPhoneNo(text);
			}

			break;
		case R.id.ll3:

			text = tvPhoneNo3.getText().toString();

			if (text != null && text.length() > 0) {
				selectedPhoneNo(text);
			}

			break;
		case R.id.ll4:

			text = tvPhoneNo4.getText().toString();

			if (text != null && text.length() > 0) {
				selectedPhoneNo(text);
			}

			break;

		default:
			break;
		}

	}

}
