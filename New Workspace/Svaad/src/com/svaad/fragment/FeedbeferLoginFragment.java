package com.svaad.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.svaad.R;
import com.svaad.utils.Constants;

public class FeedbeferLoginFragment extends Fragment implements OnClickListener {

	Button btnLogin, btnJoin;
	int mode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.feedbefore_fragement, container,
				false);
		init(view);
		
		if(this.getArguments()!=null)
		{
			mode=this.getArguments().getInt(Constants.PAGER);
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

//			new SvaadDialogs().showSvaadSignUpDialog(getActivity(), null,mode,Constants);

			break;

		case R.id.btnLogin:

//			new SvaadDialogs().showSvaadLoginDialog(getActivity(), null,mode);

			break;

		default:
			break;
		}

	}

}
