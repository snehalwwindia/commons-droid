/**This class contains Network Utilities.
 * 
 * @author Melbourne Lopes.
 * */
package com.commonsdroid.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Formatter;

public class NetworkHelper {

	public static boolean isNetworkAvailable(Context ctx) {
		/** This method returns the network state */
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static String getLocalIpAddress(Context context) {
		/** This method returns the Local IP address */
		WifiManager wifiMgr = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		String address = Formatter.formatIpAddress(ip);

		return address;
	}

	public static String getNetworkIPAddress(boolean useIPv4) {
		/** This method returns the Network IP address */
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port
																// suffix
								return delim < 0 ? sAddr : sAddr.substring(0,
										delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
	}

	public static boolean isAirplaneModeOn(Context context) {
		/** This method returns true if Airplane mode is ON */
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	public static String getAvailableNetworkInfo(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = conMan.getNetworkInfo(0).getState();
		State wifi = conMan.getNetworkInfo(1).getState();
		if (mobile == NetworkInfo.State.CONNECTED
				|| mobile == NetworkInfo.State.CONNECTING) {
			return "mobile";
		} else if (wifi == NetworkInfo.State.CONNECTED
				|| wifi == NetworkInfo.State.CONNECTING) {
			return "wifi";
		}
		
		return null;
	}
}
