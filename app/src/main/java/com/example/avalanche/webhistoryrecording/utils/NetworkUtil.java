package com.example.avalanche.webhistoryrecording.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class NetworkUtil {
	
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	
	public static int getConnectivityStatus(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		} 
		return TYPE_NOT_CONNECTED;
	}
	
	public static String getConnectivityStatusString(Context context) {
		int conn = NetworkUtil.getConnectivityStatus(context);
		String status = null;
		if (conn == NetworkUtil.TYPE_WIFI) {
			status = "Wifi enabled";
		} else if (conn == NetworkUtil.TYPE_MOBILE) {
			status = "Mobile data enabled";
		} else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
		}
		return status;
	}

	public static void snakeBar(String s, CoordinatorLayout coordinatorLayout) {
    int time=4000;
		Snackbar snackbar = Snackbar
				.make(coordinatorLayout, s, Snackbar.LENGTH_SHORT);


	//	snackbar.setActionTextColor(Color.RED);

		View sbView = snackbar.getView();
		TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.YELLOW);
		snackbar.show();

	}


	public static void snakeBarRetry(String s, CoordinatorLayout coordinatorLayout, final Context context, final Intent getIntent) {
		Snackbar snackbar = Snackbar
				.make(coordinatorLayout, "You don't have internet connection!", Snackbar.LENGTH_LONG)
				.setAction("RETRY", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = getIntent;
						((Activity)context).finish();
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						context.startActivity(intent);
					}
				}) ;

		snackbar.setActionTextColor(Color.RED);

// Changing action button text color
		View sbView = snackbar.getView();
		TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.YELLOW);
		snackbar.show();
	}
}
