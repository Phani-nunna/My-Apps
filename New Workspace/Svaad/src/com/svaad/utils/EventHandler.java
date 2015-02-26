package com.svaad.utils;

import android.content.Context;

import com.svaad.R;

public class EventHandler {

	private static boolean isTokenAvalable(Context context) {
		String token = Utils
				.getFromSharedPreference(Constants.SESSION_TOCKEN_KEY);
		if (token == null || token.length() == 0) {
			// new MyDailogs().showLoginDailog(context, context.getResources()
			// .getString(R.string.get_logedin));

			new SvaadDialogs().showsetLoginDailog(context, context
					.getResources().getString(R.string.get_logedin));

			return false;
		} else {
			return true;
		}
	}

}
