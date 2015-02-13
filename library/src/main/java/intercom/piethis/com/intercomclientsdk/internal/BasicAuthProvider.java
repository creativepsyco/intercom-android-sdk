package intercom.piethis.com.intercomclientsdk.internal;

import android.text.TextUtils;
import android.util.Base64;

import retrofit.RequestInterceptor;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class BasicAuthProvider implements RequestInterceptor {

  public static final String AUTHORIZATION_HEADER_KEY = "Authorization";

  public static final String BASIC_KEY = "Basic ";

  private static final String ACCEPT_HEADER_KEY = "Accept";

  private String userName;

  private String password;

  public BasicAuthProvider(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  @Override
  public void intercept(RequestFacade request) {
    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
      final String authorizationValue = encodeCredentialsForBasicAuthorization();
      request.addHeader(AUTHORIZATION_HEADER_KEY, authorizationValue);
      request.addHeader(ACCEPT_HEADER_KEY, "application/json");
    }
  }

  private String encodeCredentialsForBasicAuthorization() {
    final String userAndPassword = userName + ":" + password;
    return BASIC_KEY + Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
  }
}
