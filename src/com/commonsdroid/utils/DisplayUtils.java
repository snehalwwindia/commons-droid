package com.commonsdroid.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

public class DisplayUtils {

	public static int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static String getModelName() {
		return android.os.Build.MODEL;
	}

	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	public static String getAndroidVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * Determine if the device is running API level 8 or higher.
	 */
	public static boolean isFroyo() {
		return getSdkVersion() >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Determine if the device is running API level 11 or higher.
	 */
	public static boolean isHoneycomb() {
		return getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * Determine if the device is a tablet (i.e. it has a large screen).
	 * 
	 * @param context
	 *            The calling context.
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * Determine if the device is a HoneyComb tablet.
	 * 
	 * @param context
	 *            The calling context.
	 */
	public static boolean isHoneycombTablet(Context context) {
		return isHoneycomb() && isTablet(context);
	}

	/**
	 * @param context
	 * @return 0.75 - ldpi, 1.0 - mdpi, 1.5 - hdpi, 2.0 - xhdpi, 3.0 - xxhdpi,
	 *         4.0 - xxxhdpi.
	 */
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * @param context
	 * @return width in integer of the device
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * @param context
	 * @return height in integer of the device
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 
	 * @param context
	 * @return 1 for ORIENTATION_PORTRAIT 2 for ORIENTATION_LANDSCAPE
	 */
	public static int getScreenOrientation(Context context) {
		return context.getResources().getConfiguration().orientation;
	}

	/**
	 * 
	 * @param context
	 * @return PIXELS for the given DP
	 */
	public static int getDptoPixels(Context context, int dp) {
		return Math
				.round(dp
						* (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
		// return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
		// context.getResources().getDisplayMetrics());
	}

	/**
	 * 
	 * @param context
	 * @return DP for the given PIXELS
	 */
	public static int getPixelsToDP(Context context, int pixels) {
		return Math
				.round(pixels
						/ (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}
}
