package com.commonsdroid.utils;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.SessionTracker;
import com.facebook.internal.Utility;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * This class has utility to post string or photo to Facebook timeline of user.
 * 
 * Pass instance of current activity to the constructor.
 * 
 * @author Nayanesh Gupte
 * 
 */

public class FacebookHelper {
	
	public static final String FACEBOOK_ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";

	public static final int MSGTYPE_FACEBOOK_NETWORK_ERROR = 0;

	public static final int MSGTYPE_FACEBOOK_LOGIN_ERROR = 1;

	public static final int MSGTYPE_FACEBOOK_USER_FOUND = 2;
	/**
	 * prefs
	 */
	
	private static final String PREF_FACEBOOK_ACCESS_TOKEN = "access_token";
	
	private static final String PREF_FACEBOOK_BIRTHDAY = "facebook_birthady";

	private static final String PREF_FACEBOOK_LOCALE = "facebook_locale";

	private static final String PREF_FACEBOOK_LINK = "facebook_link";

	private static final String PREF_FACEBOOK_UPDATED_TIME = "facebook_updatedtime";

	private static final String PREF_FACEBOOK_ID = "facebook_id";

	private static final String PREF_FACEBOOK_FIRST_NAME = "facebook_first_name";

	private static final String PREF_FACEBOOK_TIMEZONE = "facebook_timezone";

	private static final String PREF_FACEBOOK_USERNAME = "facebook_username";

	private static final String PREF_FACEBOOK_EMAIL = "facebook_email";

	private static final String PREF_FACEBOOK_VERIFIED = "facebook_verified";

	private static final String PREF_FACEBOOK_NAME = "facebook_name";

	private static final String PREF_FACEBOOK_LAST_NAME = "facebook_last_name";

	private static final String PREF_FACEBOOK_GENDER = "facebook_gender";

	private static final String PREF_FACEBOOK_PROFILE_URL = "facebook_profile_url";
	/**********************************************************************************/
	
	public static final int FACEBOOK = 87;
	public static final int TWITTER = 285;
	public static final int EMAIL = 535;
	public static final int SMS = 69;

	public static final String EMAILS = "emails";

	public static final String SOCIAL_TYPE = "socialType";

	public static final String AT_FACEBOOK = "@facebook.com";

	public static final int PROFILE_IMAGE_WIDTH = 60;

	public static final int PROFILE_IMAGE_HEIGHT = 60;

	public static final List<String> PERMISSIONS = Arrays.asList(
			"email", "read_friendlists", "read_stream",
			"user_online_presence", "user_about_me", "user_activities",
			"user_birthday", "user_checkins", "user_education_history",
			"friends_about_me", "friends_checkins", "friends_birthday",
			"user_events", "user_hometown", "user_interests",
			"user_likes", "user_location", "user_photos",
			"photo_upload", "user_groups", "user_notes",
			"user_photo_video_tags", "user_relationships",
			"user_relationship_details", "user_religion_politics",
			"user_status", "user_videos", "user_website",
			"user_work_history", "status_update");

	private static final String FQL = "fql";
	private static final String FQL_Q = "q";
	private static final String FQL_FRIENDS = "select uid, name, username, pic from user where uid in (select uid2 from friend where uid1=me()) order by name";
	private static final String JSON_DATA = "data";
	private Activity activity;
	private Fragment frag;
//	private String TAG = "FacebookHelper";
	public Session.StatusCallback statusCallback = new SessionStatusCallback();
	private SessionTracker mSessionTracker;
	public Session mCurrentSession;
	private FacebookLoginHelper loginHelper;
	private Context context;
	private Bitmap image = null;
	private ProgressDialog mProgressDialog;


	public FacebookHelper(Activity activity, Bundle savedInstanceState) {
		this.activity = activity;
		this.loginHelper = (FacebookLoginHelper) activity;
		this.context = activity;
		init(savedInstanceState);
	}

	public FacebookHelper(Fragment frag, Bundle savedInstanceState) {
		this.frag = frag;
		this.loginHelper = (FacebookLoginHelper) frag;
		this.context = frag.getActivity();
		init(savedInstanceState);
	}

	private void init(Bundle savedInstanceState) {
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(context, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(context);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				if (activity != null)
					session.openForRead(new Session.OpenRequest(activity)
							.setCallback(statusCallback));
				else
					session.openForRead(new Session.OpenRequest(frag)
							.setCallback(statusCallback));
			}
		}
	}

	/**
	 * Pass message to be posted on Facebook timeline
	 * 
	 * @param message
	 */
	public void postStatusUpdate(final String message) {

//		Session session = Session.getActiveSession();

		if (mCurrentSession != null) {
			Request request = Request.newStatusUpdateRequest(
					Session.getActiveSession(), message,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult(message,
									response.getGraphObject(),
									response.getError());
						}
					});

			request.executeAsync();
		} else {

//			Log.d(TAG, "postStatusUpdate: Session is null");
		}

	}
	
	/**
	 * Posts default image from drawable to Facebook timeline.
	 * 
	 * Change this method to accept parameter according to your need.
	 */
	public void postPhoto(String imageUrl, final String message) {

		if (!imageUrl.startsWith("http")) {

			imageUrl = "http://" + imageUrl;
		}

		final String url = imageUrl;

		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {

				showProgressDialog();

			};

			@Override
			protected Void doInBackground(Void... params) {
				try {
					InputStream in = HttpUtils.fetchStream(url);

					image = BitmapUtils.getBitmap(context, in);

//					Log.d(TAG, "image: " + image);

				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				publishPic(image, message);
			}
		}.execute();

	}

	public void publishPic(final Bitmap image, String message) {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Bitmap image = BitmapFactory.decodeResource(
			// activity.getResources(), R.drawable.user_thumbnail2);

			Request request = Request.newUploadPhotoRequest(
					Session.getActiveSession(), image, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {

							closeProgressDialog();

							showPublishResult("Photo Post",
									response.getGraphObject(),
									response.getError());
							// image = null;
						}
					});
			Bundle params = request.getParameters();
			params.putString("message", message);
			request.executeAsync();
		} else {

//			Log.d(TAG, "postPhoto: Session is null");
			// activity.log("postPhoto: Session is null");
		}

	}

	private void showPublishResult(String message, GraphObject result,
			FacebookRequestError error) {
//		String title = null;
		String alertMessage = null;
		if (error == null) {
//			title = "Success";
//			String id = result.cast(GraphObjectWithId.class).getId();
//			alertMessage = context.getString(R.string.successfully_posted_post,
//					message, id);
			alertMessage = "Successfully posted on your wall";

		} else {
//			title = "Error";
			alertMessage = error.getErrorMessage();

			alertMessage = "Error while posting";
		}

		Toast.makeText(context, alertMessage, Toast.LENGTH_SHORT).show();

		// new AlertDialog.Builder(context).setTitle(title)
		// .setMessage(alertMessage).setPositiveButton(R.string.ok, null)
		// .show();
	}

	/*private interface GraphObjectWithId extends GraphObject {
		String getId();
	}*/

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			// activity.log("session: " + session.getAccessToken() + " state: "
			// + state.name());

			if (null != exception) {

				// activity.log("exception: " + exception.getMessage());
			}
		}
	}

	/**
	 * Login and get Facebook user object
	 * 
	 * @return
	 */
	public void loginfacebook() {

		mSessionTracker = new SessionTracker(context, statusCallback, null,
				false);

		String applicationId = Utility.getMetadataApplicationId(context);
		mCurrentSession = mSessionTracker.getSession();

		if (mCurrentSession == null || mCurrentSession.getState().isClosed()) {

//			Log.d(TAG, "mCurrentSession is null or closed");

			mSessionTracker.setSession(null);
			Session session = new Session.Builder(context).setApplicationId(
					applicationId).build();
			Session.setActiveSession(session);
			mCurrentSession = session;
		}

		if (!mCurrentSession.isOpened()) {

//			Log.d(TAG, "mCurrentSession is closed");
			Session.OpenRequest openRequest;
			if (activity != null)
				openRequest = new Session.OpenRequest(activity);
			else
				openRequest = new Session.OpenRequest(frag);

			if (openRequest != null) {

//				Log.d(TAG, "openRequest is not null");

				openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
				openRequest.setPermissions(PERMISSIONS);
				openRequest
						.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO/* SSO_WITH_FALLBACK */);

				mCurrentSession.openForRead(openRequest);
			}
		} else {

			// activity.log("already logged in");

			getFacebookData();
		}

	}

	/**
	 * This will be called once user gets data from Facebook
	 * 
	 * @author Nayanesh Gupte
	 * 
	 */
	public static interface FacebookLoginHelper {
		public void onFacebookCallComplete(SocialUserModel fbUser);

		public void onFacebookLoginError();
	}

	public static interface OnFetchFriendsListener {
		public void onFetchFriends(List<SocialUserModel> users);
	}

	/**
	 * get Facebook user
	 */
	
	public void getFacebookData() {

		Request.newMeRequest(mCurrentSession != null ? mCurrentSession
				: Session.getActiveSession(), new Request.GraphUserCallback() {
			private SocialUserModel fbUser;

			@Override
			public void onCompleted(GraphUser user, Response response) {

				if (response.getError() == null) {

					String accesstoken = Session.getActiveSession()
							.getAccessToken();

//					Log.d(TAG,
//							"userId: " + user.getId() + " username: "
//									+ user.getName() + " data: "
//									+ user.getInnerJSONObject());

					fbUser = SocialUserModel.getFacebookUser(user
							.getInnerJSONObject().toString());

					/**
					 * retrieve FB user profile url and set in model
					 */
					String url = null;
					try {

						url = ImageRequest.getProfilePictureUrl(fbUser.getId(),
								PROFILE_IMAGE_WIDTH,
								PROFILE_IMAGE_HEIGHT)
								.toString();

					} catch (URISyntaxException e) {
						e.printStackTrace();
					}

					fbUser.setProfileImgUrl(url);

					String email = null;
					if (TextUtils.isEmpty(fbUser.getEmail())) {
						email = fbUser.getUserName()
								+ AT_FACEBOOK;
					} else {
						email = fbUser.getEmail();
					}

					fbUser.setEmail(email);

					setFBAccessToken(context, accesstoken);
//
					setFacebookProfile(context, fbUser);

					loginHelper.onFacebookCallComplete(fbUser);

				} else {

//					Log.d(TAG, "showFBLoginErrorMsg");

					// loginHelper.onFacebookCallComplete(fbUser);
					loginHelper.onFacebookLoginError();
					showFBLoginErrorMsg(MSGTYPE_FACEBOOK_NETWORK_ERROR);
				}
			}
		}).executeAsync();

	}

	public void showFBLoginErrorMsg(int msgType) {

		String strMsg = "";
		switch (msgType) {
		case MSGTYPE_FACEBOOK_NETWORK_ERROR:

			strMsg = "can't login to facebook.please check network and retry again.";

			break;

		case MSGTYPE_FACEBOOK_LOGIN_ERROR:

			strMsg = "An error occured please try again.";

			break;

		case MSGTYPE_FACEBOOK_USER_FOUND:

			strMsg = "User not available";

			break;

		default:
			break;
		}

		Toast.makeText(context, strMsg, Toast.LENGTH_LONG).show();
	}

	public void getFriends(final OnFetchFriendsListener fetchFriendsListener) {
		Session session = Session.getActiveSession();
		if (session != null) {

			Request request = Request.newGraphPathRequest(session, FQL,
					new Callback() {

						@Override
						public void onCompleted(Response response) {

							List<SocialUserModel> friendsList = null;

							if (null != response
									&& null != response.getGraphObject()) {
								GraphObject graphObject = response
										.getGraphObject();

								Object object = graphObject
										.getProperty(JSON_DATA);

								if (null != object) {
									try {
										Gson gson = new Gson();
										SocialUserModel[] friendsArray = gson.fromJson(
												object.toString(),
												SocialUserModel[].class);
										friendsList = Arrays
												.asList(friendsArray);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							fetchFriendsListener.onFetchFriends(friendsList);
						}
					});
			request.getParameters().putString(FQL_Q, FQL_FRIENDS);
			request.executeAsync();
		}
	}

	public String getProfilePicLink(String id) {
		try {
			return ImageRequest.getProfilePictureUrl(id,
					PROFILE_IMAGE_WIDTH,
					PROFILE_IMAGE_HEIGHT).toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected void showProgressDialog() {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.show();

	}

	protected void closeProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	public SocialUserModel callFacebookLogin(Context context) {

		// showProgressDialog();

		if (null != getFBAccessToken(context)) {

			SocialUserModel fbUser = getFacebookUser(context);

			return fbUser;

		} else {

			return null;
		}
	}
	//-----------------------fb constants--------------------------------------------------------
	
	//--------------------------------------------------------------------------------------------------
	//----------------------------fb model-------------------------------------------------------------
	public static class SocialUserModel {

		private boolean isChecked;

		private Uri photoUri;

		private String phoneNumber;

		@SerializedName("birthday")
		private String birthday;

		@SerializedName("locale")
		private String locale;

		@SerializedName("link")
		private String link;

		@SerializedName("updated_time")
		private String updated_time;

		@SerializedName("id")
		private String id;

		@SerializedName("first_name")
		private String firstName;

		@SerializedName("timezone")
		private String timezone;

		@SerializedName("username")
		private String username;

		@SerializedName("email")
		private String email;

		@SerializedName("verified")
		private String verified;

		@SerializedName("name")
		private String name;

		@SerializedName("last_name")
		private String lastName;

		@SerializedName("gender")
		private String gender;

		@SerializedName("pic")
		private String profileImgUrl;

		public boolean isChecked() {
			return isChecked;
		}

		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}

		public Uri getPhotoUri() {
			return photoUri;
		}

		public void setPhotoUri(Uri photoUri) {
			this.photoUri = photoUri;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getBirthday() {
			return birthday;
		}

		public String getLocale() {
			return locale;
		}

		public String getLink() {
			return link;
		}

		public String getUpdated_time() {
			return updated_time;
		}

		public String getId() {
			return id;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getTimezone() {
			return timezone;
		}

		public String getUserName() {
			return username;
		}

		public String getEmail() {
			return email;
		}

		public String getVerified() {
			return verified;
		}

		public String getName() {
			return name;
		}

		public String getLastName() {
			return lastName;
		}

		public String getGender() {
			return gender;
		}

		public String getProfileImgUrl() {
			return profileImgUrl;
		}

		public void setProfileImgUrl(String profileUrl) {
			this.profileImgUrl = profileUrl;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public void setUpdated_time(String updated_time) {
			this.updated_time = updated_time;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}

		public void setUserName(String username) {
			this.username = username;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public void setVerified(String verified) {
			this.verified = verified;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public Comparator<SocialUserModel> getUserNameComparator() {

			return new Comparator<SocialUserModel>() {

				public int compare(SocialUserModel user1, SocialUserModel user2) {

					String userName1 = user1.getName().toUpperCase(Locale.getDefault());
					String userName2 = user2.getName().toUpperCase(Locale.getDefault());

					// ascending order
					return userName1.compareTo(userName2);

					// descending order
					// return fruitName2.compareTo(fruitName1);
				}
			};
		}

		/**
		 * get Facebook user object
		 * 
		 * @param response
		 * @return
		 */
		public static SocialUserModel getFacebookUser(String response) {

			SocialUserModel obj = null;

			try {

				Gson gson = new Gson();
				obj = gson.fromJson(response, SocialUserModel.class);

			} catch (Exception e) {
				// TODO: handle exception
//				Log.e("FacebookUserDataModel",
//						"Error in getFacebookUser: " + e.getMessage());
				e.printStackTrace();
			}

			return obj;
		}

	}
	//----------------------------shared prefs-----------------------------------------------------
	public static String getFBAccessToken(Context context) {
		return getSharedPreferences(context).getString(
				PREF_FACEBOOK_ACCESS_TOKEN, null);
	}

	public static void setFBAccessToken(Context context, String value) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putString(PREF_FACEBOOK_ACCESS_TOKEN, value);
		editor.commit();
	}
	
	public static SocialUserModel getFacebookUser(Context context) {

		SocialUserModel dataModel = new SocialUserModel();

		dataModel.setBirthday(getSharedPreferences(context).getString(
				PREF_FACEBOOK_BIRTHDAY, null));
		dataModel.setLocale(getSharedPreferences(context).getString(
				PREF_FACEBOOK_LOCALE, null));
		dataModel.setLink(getSharedPreferences(context).getString(
				PREF_FACEBOOK_LINK, null));
		dataModel.setUpdated_time(getSharedPreferences(context).getString(
				PREF_FACEBOOK_UPDATED_TIME, null));
		dataModel.setId(getSharedPreferences(context).getString(
				PREF_FACEBOOK_ID, null));
		dataModel.setFirstName(getSharedPreferences(context).getString(
				PREF_FACEBOOK_FIRST_NAME, null));
		dataModel.setTimezone(getSharedPreferences(context).getString(
				PREF_FACEBOOK_TIMEZONE, null));
		dataModel.setUserName(getSharedPreferences(context).getString(
				PREF_FACEBOOK_USERNAME, null));
		dataModel.setEmail(getSharedPreferences(context).getString(
				PREF_FACEBOOK_EMAIL, null));
		dataModel.setVerified(getSharedPreferences(context).getString(
				PREF_FACEBOOK_VERIFIED, null));
		dataModel.setName(getSharedPreferences(context).getString(
				PREF_FACEBOOK_NAME, null));
		dataModel.setLastName(getSharedPreferences(context).getString(
				PREF_FACEBOOK_LAST_NAME, null));
		dataModel.setGender(getSharedPreferences(context).getString(
				PREF_FACEBOOK_GENDER, null));
		dataModel.setProfileImgUrl(getSharedPreferences(context).getString(
				PREF_FACEBOOK_PROFILE_URL, null));

		return dataModel;
	}
	
	public static void setFacebookProfile(Context context,
			SocialUserModel dataModel) {

		Editor editor = getSharedPreferences(context).edit();

		editor.putString(PREF_FACEBOOK_BIRTHDAY, dataModel.getBirthday());
		editor.putString(PREF_FACEBOOK_LOCALE, dataModel.getLocale());
		editor.putString(PREF_FACEBOOK_LINK, dataModel.getLink());
		editor.putString(PREF_FACEBOOK_UPDATED_TIME,
				dataModel.getUpdated_time());
		editor.putString(PREF_FACEBOOK_ID, dataModel.getId());
		editor.putString(PREF_FACEBOOK_FIRST_NAME, dataModel.getFirstName());
		editor.putString(PREF_FACEBOOK_TIMEZONE, dataModel.getTimezone());
		editor.putString(PREF_FACEBOOK_USERNAME, dataModel.getUserName());
		editor.putString(PREF_FACEBOOK_EMAIL, dataModel.getEmail());
		editor.putString(PREF_FACEBOOK_VERIFIED, dataModel.getVerified());
		editor.putString(PREF_FACEBOOK_NAME, dataModel.getName());
		editor.putString(PREF_FACEBOOK_LAST_NAME, dataModel.getLastName());
		editor.putString(PREF_FACEBOOK_GENDER, dataModel.getGender());
		editor.putString(PREF_FACEBOOK_PROFILE_URL,
				dataModel.getProfileImgUrl());

		editor.commit();

	}
	
	private static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
