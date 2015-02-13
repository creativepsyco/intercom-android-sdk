package intercom.piethis.com.intercomclientsdk.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import intercom.piethis.com.intercomclientsdk.BuildConfig;
import intercom.piethis.com.intercomclientsdk.Intercom;
import intercom.piethis.com.intercomclientsdk.interfaces.Users;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class IntercomClient {
  static final int CONNECT_TIMEOUT_MILLIS = 20 * 1000; // 20s

  static final int READ_TIMEOUT_MILLIS = 50 * 1000; // 50s

  private final RestAdapter intercomApi;

  private final Users userService;

  public IntercomClient(Intercom intercom) {
    Gson gson = getAPIGsonRegistry();

    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    okHttpClient.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

    BasicAuthProvider authProvider = new BasicAuthProvider(intercom.getAPP_ID(), intercom.getAPI_KEY());

    intercomApi = new RestAdapter.Builder()
        .setEndpoint(AppConstants.API_HOST)
        .setClient(new OkClient(okHttpClient))
        .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
        .setConverter(new GsonConverter(gson))
        .setRequestInterceptor(authProvider)
        .build();

    userService = intercomApi.create(Users.class);
  }

  public Users getUserService() {
    return userService;
  }

  private Gson getAPIGsonRegistry() {
    return new GsonBuilder()
        .create();
  }
}
