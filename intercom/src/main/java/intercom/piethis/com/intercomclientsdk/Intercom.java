package intercom.piethis.com.intercomclientsdk;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import intercom.piethis.com.intercomclientsdk.internal.IntercomClient;
import intercom.piethis.com.intercomclientsdk.protocol.*;
import intercom.piethis.com.intercomclientsdk.utils.NetworkUtils;
import intercom.piethis.com.intercomclientsdk.utils.VersionUtils;
import retrofit.Callback;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class Intercom {
  private final String APP_ID;

  private final String API_KEY;

  private IntercomClient intercomClient;

  public static Context getApplicationContext() {
    if (applicationContext != null)
      return applicationContext.get();
    return null;
  }

  private static WeakReference<Context> applicationContext;

  public Intercom(IntercomConfig config, Context context) {
    this.API_KEY = config.getAppKey();
    this.APP_ID = config.getAppId();
    this.intercomClient = new IntercomClient(this);
    applicationContext = new WeakReference<>(context.getApplicationContext());
  }

  public String getAPP_ID() {
    return APP_ID;
  }

  public String getAPI_KEY() {
    return API_KEY;
  }

  public void getUsers(Callback<UserListReponse> callback) {
    this.intercomClient.getUserService().getUsers(callback);
  }

  public void beginNewSession(String userId, Callback<User> callback) {
    UserRequest user = new UserRequest();
    user.newSession = true;
    user.userId = userId;
    updateUser(user, callback);
  }

  public void updateUser(String userId, String email, String name, Company company, CustomAttributes customAttrs, Callback<User> callback) throws JSONException {
    UserRequest user = new UserRequest();
    user.newSession = true;
    user.userId = userId;
    user.name = name;
    user.email = email;
    user.companies = new ArrayList<>();
    user.companies.add(company);
    user.lastSeenIPAddress = NetworkUtils.getExternalIP();
    user.customAttributes = customAttrs;
    updateUser(user, callback);
  }

  public void updateUser(UserRequest user, Callback<User> callback) {
    user.newSession = true;
    user.lastSeenIPAddress = NetworkUtils.getExternalIP();
    user.updateLastSeen = true;
    if (user.customAttributes == null) {
      user.customAttributes = new CustomAttributes();
    }
    user.customAttributes.androidVersion = VersionUtils.getAndroidVersion();
    this.intercomClient.getUserService().createNewSession(user, callback);
  }

  public void newUserSignedUp(UserRequest user, Callback<User> callback) {
    user.newSession = true;
    user.signUpTime = System.currentTimeMillis() / 1000L;
    updateUser(user, callback);
  }

  public void deleteUser(UserRequest user, Callback<User> callback) {
    if (TextUtils.isEmpty(user.email) && TextUtils.isEmpty(user.userId)) {
      throw new IllegalArgumentException("Email and userId cannot be both empty.");
    }
    if (TextUtils.isEmpty(user.email)) {
      this.intercomClient.getUserService().deleteUserByUserId(user.userId, callback);
    } else {
      this.intercomClient.getUserService().deleteUserByEmail(user.email, callback);
    }
  }
}
