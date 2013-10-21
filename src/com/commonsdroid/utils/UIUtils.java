package com.commonsdroid.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class UIUtils {


	/**
	 * ToggleVisibility.
	 * 
	 * @param view whose visibility need to change
	 * @param setInvisible to set invisible or gone, true to set INVISIBLE; false to set GONE
	 */
	public static void toggleVisibility(View view,boolean setInvisible) {

		switch (view.getVisibility()) {
		case View.VISIBLE :
			if(setInvisible) {
				view.setVisibility(View.INVISIBLE);
			} else {
				view.setVisibility(View.GONE);
			}
			break;
		case View.INVISIBLE :
		case View.GONE:
			view.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * ShowNotification.
	 * 
	 * @param context to get system's notification service
	 * @param contentTitle notification title
	 * @param message notification message
	 * @param intent pending intent on click of notification
	 * @param icon notification icon
	 * @param setAutoCancel is notification cancellable
	 * @param tickerText ticker text on receiving notification
	 * @param notificationId 	unique notification id. If a notification with the same id has already been 
 								posted by your application and has not yet been canceled, it will be replaced by the updated 
 								information.	
	 */
	public static void showNotification(Context context,String contentTitle,String message,PendingIntent intent,int icon, boolean setAutoCancel,String tickerText,int notificationId) {

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		builder
		.setContentTitle(contentTitle)
		.setContentText(message)
		.setTicker(tickerText)
		.setSmallIcon(icon)
		.setContentIntent(intent)
		.setAutoCancel(setAutoCancel);

		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(notificationId, builder.build());
	}

	/**
	 * ToogleShowPassword.
	 * 
	 * @param setPassVisible
	 * @param txtPass EditText 
	 */
	public static void toggleShowPassword(boolean setPassVisible,EditText txtPass) {
		
		if(setPassVisible) {
			txtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else {
			txtPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		txtPass.setSelection(txtPass.getText().length());
	}

}
