package intercom.piethis.com.intercomclientsdk;

import android.content.Context;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import intercom.piethis.com.intercomclientsdk.internal.IntercomClient;
import intercom.piethis.com.intercomclientsdk.protocol.Company;
import intercom.piethis.com.intercomclientsdk.protocol.User;
import intercom.piethis.com.intercomclientsdk.protocol.UserListReponse;
import intercom.piethis.com.intercomclientsdk.protocol.UserRequest;
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
    user.updateLastSeen = true;
    user.userId = userId;
    user.lastSeenUserAgent = VersionUtils.sanitizeVersionString();
    this.intercomClient.getUserService().createNewSession(user, callback);
  }

  public void updateUser(String userId, String email, String name, Company company, Callback<User> callback) {
    UserRequest user = new UserRequest();
    user.newSession = true;
    user.updateLastSeen = true;
    user.userId = userId;
    user.name = name;
    user.email = email;
    user.lastSeenUserAgent = VersionUtils.sanitizeVersionString();
    user.companies = new ArrayList<>();
    user.companies.add(company);
    this.intercomClient.getUserService().createNewSession(user, callback);
  }

  public void updateUser(UserRequest user, Callback<User> callback) {
    user.lastSeenUserAgent = VersionUtils.sanitizeVersionString();
    user.newSession = true;
    user.updateLastSeen = true;
    this.intercomClient.getUserService().createNewSession(user, callback);
  }

  public void newUserSignedUp(UserRequest user, Callback<User> callback) {
    user.lastSeenUserAgent = VersionUtils.sanitizeVersionString();
    user.newSession = true;
    user.updateLastSeen = true;
    user.signUpTime = System.currentTimeMillis() / 1000L;
    this.intercomClient.getUserService().createNewSession(user, callback);
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
