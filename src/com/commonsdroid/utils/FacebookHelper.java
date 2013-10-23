package com.commonsdroid.utils;

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

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * This class has utility to post string or photo to Facebook timeline of user.
 * Pass instance of current activity to the constructor.
 * 
 * @author Siddhesh Shetye
 */

public class FacebookHelper {

    /** The Constant FACEBOOK_ACCESS_TOKEN_KEY. */
    public static final String FACEBOOK_ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";

    /** The Constant MSGTYPE_FACEBOOK_NETWORK_ERROR. */
    public static final int MSGTYPE_FACEBOOK_NETWORK_ERROR = 0;

    /** The Constant MSGTYPE_FACEBOOK_LOGIN_ERROR. */
    public static final int MSGTYPE_FACEBOOK_LOGIN_ERROR = 1;

    /** The Constant MSGTYPE_FACEBOOK_USER_FOUND. */
    public static final int MSGTYPE_FACEBOOK_USER_FOUND = 2;

    /** prefs. */

    private static final String PREF_FACEBOOK_ACCESS_TOKEN = "access_token";

    /** The Constant PREF_FACEBOOK_BIRTHDAY. */
    private static final String PREF_FACEBOOK_BIRTHDAY = "facebook_birthady";

    /** The Constant PREF_FACEBOOK_LOCALE. */
    private static final String PREF_FACEBOOK_LOCALE = "facebook_locale";

    /** The Constant PREF_FACEBOOK_LINK. */
    private static final String PREF_FACEBOOK_LINK = "facebook_link";

    /** The Constant PREF_FACEBOOK_UPDATED_TIME. */
    private static final String PREF_FACEBOOK_UPDATED_TIME = "facebook_updatedtime";

    /** The Constant PREF_FACEBOOK_ID. */
    private static final String PREF_FACEBOOK_ID = "facebook_id";

    /** The Constant PREF_FACEBOOK_FIRST_NAME. */
    private static final String PREF_FACEBOOK_FIRST_NAME = "facebook_first_name";

    /** The Constant PREF_FACEBOOK_TIMEZONE. */
    private static final String PREF_FACEBOOK_TIMEZONE = "facebook_timezone";

    /** The Constant PREF_FACEBOOK_USERNAME. */
    private static final String PREF_FACEBOOK_USERNAME = "facebook_username";

    /** The Constant PREF_FACEBOOK_EMAIL. */
    private static final String PREF_FACEBOOK_EMAIL = "facebook_email";

    /** The Constant PREF_FACEBOOK_VERIFIED. */
    private static final String PREF_FACEBOOK_VERIFIED = "facebook_verified";

    /** The Constant PREF_FACEBOOK_NAME. */
    private static final String PREF_FACEBOOK_NAME = "facebook_name";

    /** The Constant PREF_FACEBOOK_LAST_NAME. */
    private static final String PREF_FACEBOOK_LAST_NAME = "facebook_last_name";

    /** The Constant PREF_FACEBOOK_GENDER. */
    private static final String PREF_FACEBOOK_GENDER = "facebook_gender";

    /** The Constant PREF_FACEBOOK_PROFILE_URL. */
    private static final String PREF_FACEBOOK_PROFILE_URL = "facebook_profile_url";

    /**
     * *************************************************************************
     * ******.
     */

    public static final int FACEBOOK = 87;

    /** The Constant TWITTER. */
    public static final int TWITTER = 285;

    /** The Constant EMAIL. */
    public static final int EMAIL = 535;

    /** The Constant SMS. */
    public static final int SMS = 69;

    /** The Constant EMAILS. */
    public static final String EMAILS = "emails";

    /** The Constant SOCIAL_TYPE. */
    public static final String SOCIAL_TYPE = "socialType";

    /** The Constant AT_FACEBOOK. */
    public static final String AT_FACEBOOK = "@facebook.com";

    /** The Constant PROFILE_IMAGE_WIDTH. */
    public static final int PROFILE_IMAGE_WIDTH = 60;

    /** The Constant PROFILE_IMAGE_HEIGHT. */
    public static final int PROFILE_IMAGE_HEIGHT = 60;

    /** The Constant PERMISSIONS. */
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

    /** The Constant FQL. */
    private static final String FQL = "fql";

    /** The Constant FQL_Q. */
    private static final String FQL_Q = "q";

    /** The Constant FQL_FRIENDS. */
    private static final String FQL_FRIENDS = "select uid, name, username, pic from user where uid in (select uid2 from friend where uid1=me()) order by name";

    /** The Constant JSON_DATA. */
    private static final String JSON_DATA = "data";

    /** The activity. */
    private Activity activity;

    /** The frag. */
    private Fragment frag;

    /** The status callback. */
    public Session.StatusCallback statusCallback = new SessionStatusCallback();

    /** The m session tracker. */
    private SessionTracker mSessionTracker;

    /** The m current session. */
    public Session mCurrentSession;

    /** The login helper. */
    private FacebookLoginHelper loginHelper;

    /** The context. */
    private Context context;

    /** The image. */
    private Bitmap image = null;

    /** The m progress dialog. */
    private ProgressDialog mProgressDialog;

    /**
     * Instantiates a new facebook helper.
     * 
     * @param activity the activity
     * @param savedInstanceState the saved instance state
     */
    public FacebookHelper(Activity activity, Bundle savedInstanceState) {
        this.activity = activity;
        this.loginHelper = (FacebookLoginHelper) activity;
        this.context = activity;
        init(savedInstanceState);
    }

    /**
     * Instantiates a new facebook helper.
     * 
     * @param frag the frag
     * @param savedInstanceState the saved instance state
     */
    public FacebookHelper(Fragment frag, Bundle savedInstanceState) {
        this.frag = frag;
        this.loginHelper = (FacebookLoginHelper) frag;
        this.context = frag.getActivity();
        init(savedInstanceState);
    }

    /**
     * Inits the.
     * 
     * @param savedInstanceState the saved instance state
     */
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
     * Pass message to be posted on Facebook timeline.
     * 
     * @param message the message
     */
    public void postStatusUpdate(final String message) {

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

            // Log.d(TAG, "postStatusUpdate: Session is null");
        }

    }

    /**
     * Posts default image from drawable to Facebook timeline. Change this
     * method to accept parameter according to your need.
     * 
     * @param imageUrl the image url
     * @param message the message
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

                    // Log.d(TAG, "image: " + image);

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

    /**
     * Publish pic.
     * 
     * @param image the image
     * @param message the message
     */
    public void publishPic(final Bitmap image, String message) {
        Session session = Session.getActiveSession();

        if (session != null) {
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

            // Log.d(TAG, "postPhoto: Session is null");
            // activity.log("postPhoto: Session is null");
        }

    }

    /**
     * Show publish result.
     * 
     * @param message the message
     * @param result the result
     * @param error the error
     */
    private void showPublishResult(String message, GraphObject result,
            FacebookRequestError error) {
        String alertMessage = null;
        if (error == null) {
            alertMessage = "Successfully posted on your wall";

        } else {
            alertMessage = error.getErrorMessage();

            alertMessage = "Error while posting";
        }

        Toast.makeText(context, alertMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * The Class SessionStatusCallback.
     */
    private class SessionStatusCallback implements Session.StatusCallback {

        /*
         * (non-Javadoc)
         * @see com.facebook.Session.StatusCallback#call(com.facebook.Session,
         * com.facebook.SessionState, java.lang.Exception)
         */
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
     * Login and get Facebook user object.
     */
    public void loginfacebook() {

        mSessionTracker = new SessionTracker(context, statusCallback, null,
                false);

        String applicationId = Utility.getMetadataApplicationId(context);
        mCurrentSession = mSessionTracker.getSession();

        if (mCurrentSession == null || mCurrentSession.getState().isClosed()) {

            mSessionTracker.setSession(null);
            Session session = new Session.Builder(context).setApplicationId(
                    applicationId).build();
            Session.setActiveSession(session);
            mCurrentSession = session;
        }

        if (!mCurrentSession.isOpened()) {
            Session.OpenRequest openRequest;
            if (activity != null)
                openRequest = new Session.OpenRequest(activity);
            else
                openRequest = new Session.OpenRequest(frag);

            if (openRequest != null) {
                openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
                openRequest.setPermissions(PERMISSIONS);
                openRequest
                        .setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO/* SSO_WITH_FALLBACK */);

                mCurrentSession.openForRead(openRequest);
            }
        } else {
            getFacebookData();
        }

    }

    /**
     * This will be called once user gets data from Facebook.
     * 
     * @author Siddhesh
     */
    public static interface FacebookLoginHelper {

        /**
         * On facebook call complete.
         * 
         * @param fbUser the fb user
         */
        public void onFacebookCallComplete(SocialUserModel fbUser);

        /**
         * On facebook login error.
         */
        public void onFacebookLoginError();
    }

    /**
     * The listener interface for receiving onFetchFriends events. The class
     * that is interested in processing a onFetchFriends event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addOnFetchFriendsListener<code> method. When
     * the onFetchFriends event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see OnFetchFriendsEvent
     */
    public static interface OnFetchFriendsListener {

        /**
         * On fetch friends.
         * 
         * @param users the users
         */
        public void onFetchFriends(List<SocialUserModel> users);
    }

    /**
     * get Facebook user.
     * 
     * @return the facebook data
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
                    setFacebookProfile(context, fbUser);

                    loginHelper.onFacebookCallComplete(fbUser);

                } else {
                    loginHelper.onFacebookLoginError();
                    showFBLoginErrorMsg(MSGTYPE_FACEBOOK_NETWORK_ERROR);
                }
            }
        }).executeAsync();

    }

    /**
     * Show fb login error msg.
     * 
     * @param msgType the msg type
     */
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

    /**
     * Gets the friends.
     * 
     * @param fetchFriendsListener the fetch friends listener
     * @return the friends
     */
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

    /**
     * Gets the profile pic link.
     * 
     * @param id the id
     * @return the profile pic link
     */
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

    /**
     * Show progress dialog.
     */
    protected void showProgressDialog() {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

    }

    /**
     * Close progress dialog.
     */
    protected void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Call facebook login.
     * 
     * @param context the context
     * @return the social user model
     */
    public SocialUserModel callFacebookLogin(Context context) {

        if (null != getFBAccessToken(context)) {

            SocialUserModel fbUser = getFacebookUser(context);

            return fbUser;

        } else {

            return null;
        }
    }

    // ----------------------------fb
    // model-------------------------------------------------------------
    /**
     * The Class SocialUserModel.
     */
    public static class SocialUserModel {

        /** The is checked. */
        private boolean isChecked;

        /** The photo uri. */
        private Uri photoUri;

        /** The phone number. */
        private String phoneNumber;

        /** The birthday. */
        @SerializedName("birthday")
        private String birthday;

        /** The locale. */
        @SerializedName("locale")
        private String locale;

        /** The link. */
        @SerializedName("link")
        private String link;

        /** The updated_time. */
        @SerializedName("updated_time")
        private String updated_time;

        /** The id. */
        @SerializedName("id")
        private String id;

        /** The first name. */
        @SerializedName("first_name")
        private String firstName;

        /** The timezone. */
        @SerializedName("timezone")
        private String timezone;

        /** The username. */
        @SerializedName("username")
        private String username;

        /** The email. */
        @SerializedName("email")
        private String email;

        /** The verified. */
        @SerializedName("verified")
        private String verified;

        /** The name. */
        @SerializedName("name")
        private String name;

        /** The last name. */
        @SerializedName("last_name")
        private String lastName;

        /** The gender. */
        @SerializedName("gender")
        private String gender;

        /** The profile img url. */
        @SerializedName("pic")
        private String profileImgUrl;

        /**
         * Checks if is checked.
         * 
         * @return true, if is checked
         */
        public boolean isChecked() {
            return isChecked;
        }

        /**
         * Sets the checked.
         * 
         * @param isChecked the new checked
         */
        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

        /**
         * Gets the photo uri.
         * 
         * @return the photo uri
         */
        public Uri getPhotoUri() {
            return photoUri;
        }

        /**
         * Sets the photo uri.
         * 
         * @param photoUri the new photo uri
         */
        public void setPhotoUri(Uri photoUri) {
            this.photoUri = photoUri;
        }

        /**
         * Gets the phone number.
         * 
         * @return the phone number
         */
        public String getPhoneNumber() {
            return phoneNumber;
        }

        /**
         * Sets the phone number.
         * 
         * @param phoneNumber the new phone number
         */
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        /**
         * Gets the birthday.
         * 
         * @return the birthday
         */
        public String getBirthday() {
            return birthday;
        }

        /**
         * Gets the locale.
         * 
         * @return the locale
         */
        public String getLocale() {
            return locale;
        }

        /**
         * Gets the link.
         * 
         * @return the link
         */
        public String getLink() {
            return link;
        }

        /**
         * Gets the updated_time.
         * 
         * @return the updated_time
         */
        public String getUpdated_time() {
            return updated_time;
        }

        /**
         * Gets the id.
         * 
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the first name.
         * 
         * @return the first name
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * Gets the timezone.
         * 
         * @return the timezone
         */
        public String getTimezone() {
            return timezone;
        }

        /**
         * Gets the user name.
         * 
         * @return the user name
         */
        public String getUserName() {
            return username;
        }

        /**
         * Gets the email.
         * 
         * @return the email
         */
        public String getEmail() {
            return email;
        }

        /**
         * Gets the verified.
         * 
         * @return the verified
         */
        public String getVerified() {
            return verified;
        }

        /**
         * Gets the name.
         * 
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the last name.
         * 
         * @return the last name
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * Gets the gender.
         * 
         * @return the gender
         */
        public String getGender() {
            return gender;
        }

        /**
         * Gets the profile img url.
         * 
         * @return the profile img url
         */
        public String getProfileImgUrl() {
            return profileImgUrl;
        }

        /**
         * Sets the profile img url.
         * 
         * @param profileUrl the new profile img url
         */
        public void setProfileImgUrl(String profileUrl) {
            this.profileImgUrl = profileUrl;
        }

        /**
         * Sets the birthday.
         * 
         * @param birthday the new birthday
         */
        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        /**
         * Sets the locale.
         * 
         * @param locale the new locale
         */
        public void setLocale(String locale) {
            this.locale = locale;
        }

        /**
         * Sets the link.
         * 
         * @param link the new link
         */
        public void setLink(String link) {
            this.link = link;
        }

        /**
         * Sets the updated_time.
         * 
         * @param updated_time the new updated_time
         */
        public void setUpdated_time(String updated_time) {
            this.updated_time = updated_time;
        }

        /**
         * Sets the id.
         * 
         * @param id the new id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Sets the first name.
         * 
         * @param firstName the new first name
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         * Sets the timezone.
         * 
         * @param timezone the new timezone
         */
        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        /**
         * Sets the user name.
         * 
         * @param username the new user name
         */
        public void setUserName(String username) {
            this.username = username;
        }

        /**
         * Sets the email.
         * 
         * @param email the new email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * Sets the verified.
         * 
         * @param verified the new verified
         */
        public void setVerified(String verified) {
            this.verified = verified;
        }

        /**
         * Sets the name.
         * 
         * @param name the new name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Sets the last name.
         * 
         * @param lastName the new last name
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        /**
         * Sets the gender.
         * 
         * @param gender the new gender
         */
        public void setGender(String gender) {
            this.gender = gender;
        }

        /**
         * Gets the user name comparator.
         * 
         * @return the user name comparator
         */
        public Comparator<SocialUserModel> getUserNameComparator() {

            return new Comparator<SocialUserModel>() {

                public int compare(SocialUserModel user1, SocialUserModel user2) {

                    String userName1 = user1.getName().toUpperCase(Locale.getDefault());
                    String userName2 = user2.getName().toUpperCase(Locale.getDefault());

                    return userName1.compareTo(userName2);

                }
            };
        }

        /**
         * get Facebook user object.
         * 
         * @param response the response
         * @return the facebook user
         */
        public static SocialUserModel getFacebookUser(String response) {

            SocialUserModel obj = null;

            try {

                Gson gson = new Gson();
                obj = gson.fromJson(response, SocialUserModel.class);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            return obj;
        }

    }

    // ----------------------------shared
    // prefs-----------------------------------------------------
    /**
     * Gets the fB access token.
     * 
     * @param context the context
     * @return the fB access token
     */
    public static String getFBAccessToken(Context context) {
        return getSharedPreferences(context).getString(
                PREF_FACEBOOK_ACCESS_TOKEN, null);
    }

    /**
     * Sets the fb access token.
     * 
     * @param context the context
     * @param value the value
     */
    public static void setFBAccessToken(Context context, String value) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_FACEBOOK_ACCESS_TOKEN, value);
        editor.commit();
    }

    /**
     * Gets the facebook user.
     * 
     * @param context the context
     * @return the facebook user
     */
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

    /**
     * Sets the facebook profile.
     * 
     * @param context the context
     * @param dataModel the data model
     */
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

    /**
     * Gets the shared preferences.
     * 
     * @param context the context
     * @return the shared preferences
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
