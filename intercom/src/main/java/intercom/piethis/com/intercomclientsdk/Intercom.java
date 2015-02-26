package intercom.piethis.com.intercomclientsdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import intercom.piethis.com.intercomclientsdk.internal.IntercomClient;
import intercom.piethis.com.intercomclientsdk.protocol.*;
import intercom.piethis.com.intercomclientsdk.utils.NetworkUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class Intercom {
  private final String APP_ID;

  private final String API_KEY;

  private final RequestDispatcher requestDispatcher;

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
    Timber.plant(new Timber.DebugTree());
    this.intercomClient = new IntercomClient(this);
    this.requestDispatcher = new RequestDispatcher(this, this.intercomClient);
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

  Map<String, WeakReference<Callback<?>>> callbackMap = new ConcurrentHashMap<>();

  public void beginNewSession(String userId, Callback<User> callback) {
    UserRequest user = new UserRequest();
    user.newSession = true;
    user.userId = userId;
    registerCallback(user, callback);
    this.requestDispatcher.dispatchUpdate(user);
  }

  void registerCallback(UserRequest user, Callback<User> callback) {
    String requestId = UUID.randomUUID().toString();
    user.requestId = requestId;
    callbackMap.put(requestId, new WeakReference<Callback<?>>(callback));
  }

  public void updateUser(String userId, String email, String name, Company company,
                         CustomAttributes customAttrs,
                         Callback<User> callback) throws JSONException {
    UserRequest user = new UserRequest();
    user.newSession = true;
    user.userId = userId;
    user.name = name;
    user.email = email;
    user.companies = new ArrayList<>();
    user.companies.add(company);
    user.lastSeenIPAddress = NetworkUtils.getExternalIP();
    user.customAttributes = customAttrs;
    registerCallback(user, callback);
    this.requestDispatcher.dispatchUpdate(user);
  }

  public void newUserSignedUp(UserRequest user, Callback<User> callback) {
    user.newSession = true;
    user.signUpTime = System.currentTimeMillis() / 1000L;
    registerCallback(user, callback);
    this.requestDispatcher.dispatchUpdate(user);
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

  public <T> void dispatchCallback(final String requestId, final Response rawResponse, final T typedResponse, final Exception... args) {
    if (!callbackMap.containsKey(requestId) || callbackMap.get(requestId).get() == null) {
      /**
       * Clean up the key from the callback map
       */
      if (callbackMap.containsKey(requestId)) {
        callbackMap.remove(requestId);
      }
      return;
    }

    final Callback<T> callback = (Callback<T>) callbackMap.get(requestId).get();
    if (args != null && args.length > 0) {
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          callback.failure(RetrofitError.unexpectedError(requestId, args[0]));
          callbackMap.remove(requestId);
        }
      });
    } else {
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          callback.success(typedResponse, rawResponse);
          callbackMap.remove(requestId);
        }
      });
    }
  }

  /**
   * Shuts down the update thread.
   */
  public void shutdown() {
    this.requestDispatcher.shutdown();
  }
}
