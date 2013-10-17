/**This class contains Network Utilities.
 * 
 * @author Melbourne Lopes.
 * */
package com.commonsdroid.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class NetworkHelper {

	public static boolean isNetworkAvailable(Context ctx) {
		/** This method returns the network state */
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static String getLocalIpAddress(Context context) {
		/** This method returns the IP address of networked device */
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		String address = Formatter.formatIpAddress(ip);

		return address;
	}
}
