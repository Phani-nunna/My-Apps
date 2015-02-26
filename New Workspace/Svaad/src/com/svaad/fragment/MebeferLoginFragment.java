package com.svaad.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.svaad.R;
import com.svaad.activity.HomeActivity;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class MebeferLoginFragment extends Fragment implements OnClickListener {

	Button btnLogin, btnJoin;
	int mode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mebefore_fragement, container,
				false);
		init(view);

		if (this.getArguments() != null) {
			mode = this.getArguments().getInt(Constants.PAGER);
		}

		return view;
	}

	public void init(View view) {
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnJoin = (Button) view.findViewById(R.id.btnJoin);

		btnLogin.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnJoin:

			// new SvaadDialogs().showSvaadSignUpDialog(getActivity(),
			// null,mode,Constants.ME,Constants.ME_LOGIN);

			String userId = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

			if (userId == null) {

				new SvaadDialogs().showSvaadSignUpDialog(getActivity(), null,
						mode, Constants.ME, Constants.ME_LOGIN);
			} else {
				Intent intent = new Intent(getActivity(), HomeActivity.class);
				intent.putExtra(Constants.PAGER, mode);
				getActivity().startActivity(intent);
				((Activity) getActivity()).finish();
			}

			break;

		case R.id.btnLogin:

			// new SvaadDialogs().showSvaadLoginDialog(getActivity(), null,
			// mode,
			// Constants.ME, Constants.Me_JOIN);

			String userId1 = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

			if (userId1 == null) {

				new SvaadDialogs().showSvaadLoginDialog(getActivity(), null,
						mode, Constants.ME, Constants.Me_JOIN);
			} else {
				Intent intent = new Intent(getActivity(), HomeActivity.class);
				// intent.putExtra(Constants.PAGER, Constants.PAGER_MODE_FEED);
				intent.putExtra(Constants.PAGER, mode);
				getActivity().startActivity(intent);
				((Activity) getActivity()).finish();
			}

			break;

		default:
			break;
		}

	}

}
