package com.paradigmcreatives.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class PageExpireReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		System.out.println("received");
		Toast.makeText(context, "alaramfired", Toast.LENGTH_SHORT).show();
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);

	}

}
