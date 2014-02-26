package com.commonsdroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsdroid.utils.DeviceUtils;
import com.commonsdroid.utils.NetworkUtil;

public class MainActivity extends Activity {

	TextView lblInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();

	}

	void initData() {
		Context context = getApplicationContext();

		lblInfo = (TextView) findViewById(R.id.lblInfo);
		lblInfo.setText(null);
		lblInfo.append("\n Local Ip : "
				+ NetworkUtil.getLocalIpAddress(context));
		lblInfo.append("\n Network Ip : "
				+ NetworkUtil.getNetworkIPAddress(true));
		lblInfo.append("\n isConnectedMobile : "
				+ NetworkUtil.isConnectedMobile(context));
		lblInfo.append("\n isConnectedWifi : "
				+ NetworkUtil.isConnectedWifi(context));
		lblInfo.append("\n isConnectedFast : "
				+ NetworkUtil.isConnectedFast(context));
		lblInfo.append("\n Network Available : "
				+ NetworkUtil.isConnected(context));
		lblInfo.append("\n Airplane Mode : "
				+ NetworkUtil.isAirplaneModeOn(context));

		lblInfo.append("\n\n ---------------------- ");

		lblInfo.append("\n AndroidVersion : " + DeviceUtils.getAndroidVersion());
		lblInfo.append("\n Manufacturer : " + DeviceUtils.getManufacturer());
		lblInfo.append("\n getModelName : " + DeviceUtils.getModelName());
		lblInfo.append("\n getScreenDensity : "
				+ DeviceUtils.getScreenDensity(context));
		lblInfo.append("\n getScreenHeight : "
				+ DeviceUtils.getScreenHeight(context));
		lblInfo.append("\n getScreenOrientation : "
				+ DeviceUtils.getScreenOrientation(context));
		lblInfo.append("\n getScreenWidth : "
				+ DeviceUtils.getScreenWidth(context));
		lblInfo.append("\n getSdkVersion : " + DeviceUtils.getSdkVersion());

		lblInfo.append("\n\n ---------------------- ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "reloading", Toast.LENGTH_SHORT)
				.show();
		initData();
		return super.onOptionsItemSelected(item);
	}

}