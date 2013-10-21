package com.commonsdroid.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;

/**
 * @author Raj and Ravikant
 *
 */
public class IntentUtils {

	/**
	 * @param context to start an activity
	 * @param recepients email ids to whom email should be send
	 * @param subject of the message
	 * @param message body of the message
	 * @param intentChooserTitle title of intent chooser
	 */
	public static void sendMail(Context context,String[] recepients,String subject,String message,String intentChooserTitle) {

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, recepients);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, message);
		emailIntent.setType("message/rfc822");
		context.startActivity(Intent.createChooser(emailIntent, intentChooserTitle));
	}

	/**
	 * @param context to start an activity
	 * @param semicolonSeparatedPhoneNos semicolon separated phone nos
	 * @param messageBody body of the message
	 */
	public static void sendSMS(Context context,String semicolonSeparatedPhoneNos,String messageBody) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.putExtra("address",semicolonSeparatedPhoneNos.trim());
		smsIntent.putExtra("sms_body", messageBody);
		smsIntent.setType("vnd.android-dir/mms-sms");
		context.startActivity(smsIntent);
	}

	/**
	 * @param context to start an activity
	 * @param phoneNo calling no
	 */
	public static void makeCall(Context context,String phoneNo) {
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNo));
		context.startActivity(callIntent);
	}

	/**
	 * @param context to start browser activity
	 * @param url to access the web
	 */
	public static void openBrowser(Context context,String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
		context.startActivity(browserIntent);
	}

	/*
	 * use it as calculator has different package for different devices
	 */
	
	public static void openCalculator(Context context) {

		ArrayList<HashMap<String,Object>> 	items 	= new ArrayList<HashMap<String,Object>>();
		final PackageManager 				pm 		= context.getPackageManager();
		List<PackageInfo> 					packs 	= pm.getInstalledPackages(0);

		for (PackageInfo pi : packs) {
			if( pi.packageName.toString().toLowerCase().contains("calcul")) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("appName", pi.applicationInfo.loadLabel(pm));
				map.put("packageName", pi.packageName);
				items.add(map);
			}
		}

		if(items.size() >= 1) {
			String packageName 	= (String) items.get(0).get("packageName");
			Intent i 			= pm.getLaunchIntentForPackage(packageName);
			if (i != null) {
				context.startActivity(i);
			}
		}
	}

	/**
	 * @param context to startActivityForResult
	 * @param requestCode send to startActivityForResult method
	 */
	public static void pickContact(Context context,int requestCode) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);  
		((Activity)context).startActivityForResult(contactPickerIntent, requestCode);  
	}

	/**
	 * @param context to startActivityForResult
	 * @param requestCode send to startActivityForResult method
	 */
	public static void pickImage(Context context,int requestCode) {
		Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
		imageIntent.setType("image/*");
		((Activity)context).startActivityForResult(imageIntent, requestCode);
	}
	
	/**
	 * @param context to startActivityForResult
	 * @param requestCode send to startActivityForResult method
	 */
	public static void pickVideo(Context context,int requestCode) {
		Intent videoIntent = new Intent(Intent.ACTION_GET_CONTENT);
		videoIntent.setType("video/*");
		((Activity)context).startActivityForResult(videoIntent, requestCode);
	}
	
	/**
	 * @param context to startActivityForResult
	 * @param imageUri Uri to display the captured image
	 * @param requestCode send to startActivityForResult method 
	 */
	public static void captureImage(Context context,Uri imageUri,int requestCode) {
		Intent 	intent 		= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File 	photoFile 	= new File(Environment.getExternalStorageDirectory(), "img_"+new Date().toString()+".jpg");
		imageUri 			= Uri.fromFile(photoFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
		((Activity)context).startActivityForResult(intent, requestCode);
	}
	
	/**
	 * @param context to startActivityForResult
	 * @param imageUri Uri to display the captured image
	 * @param requestCode send to startActivityForResult method
	 */
	public static void captureVideo(Context context,Uri imageUri,int requestCode) {
		Intent 	intent 		= new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		File 	videoFile 	= new File(Environment.getExternalStorageDirectory(), "vid_"+new Date().toString()+".mp4");
		imageUri 			= Uri.fromFile(videoFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
		((Activity)context).startActivityForResult(intent, requestCode);
	}
	
	/**
	 * @param context to start map activity
	 * @param latitude of location
	 * @param longitude of location
	 */
	public static void showMap(Context context,String latitude,String longitude) {
		Intent intent 		= new Intent(Intent.ACTION_VIEW,Uri.parse("geo:"+latitude+","+longitude));
		context.startActivity(intent);
	}
	
}
