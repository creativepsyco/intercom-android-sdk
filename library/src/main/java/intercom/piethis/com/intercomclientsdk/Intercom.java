package intercom.piethis.com.intercomclientsdk;

import java.util.ArrayList;

import intercom.piethis.com.intercomclientsdk.internal.IntercomClient;
import intercom.piethis.com.intercomclientsdk.protocol.Company;
import intercom.piethis.com.intercomclientsdk.protocol.User;
import intercom.piethis.com.intercomclientsdk.protocol.UserListReponse;
import intercom.piethis.com.intercomclientsdk.protocol.UserRequest;
import retrofit.Callback;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class Intercom {
  private final String APP_ID;

  private final String API_KEY;

  private final IntercomClient intercomClient;

  public Intercom(IntercomConfig config) {
    this.API_KEY = config.getAppKey();
    this.APP_ID = config.getAppId();
    this.intercomClient = new IntercomClient(this);
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
    user.lastSeenUserAgent = System.getProperty("http.agent");
    this.intercomClient.getUserService().createNewSession(user, callback);
  }

  public void updateUser(String userId, String email, String name, Company company, Callback<User> callback) {
    UserRequest user = new UserRequest();
    user.newSession = true;
    user.updateLastSeen = true;
    user.userId = userId;
    user.name = name;
    user.email = email;
    user.lastSeenUserAgent = System.getProperty("http.agent");
    user.companies = new ArrayList<>();
    user.companies.add(company);
    this.intercomClient.getUserService().createNewSession(user, callback);
  }

  public void updateUser(UserRequest user, Callback<User> callback) {
    user.lastSeenUserAgent = System.getProperty("http.agent");
    user.newSession = true;
    user.updateLastSeen = true;
    this.intercomClient.getUserService().createNewSession(user, callback);
  }

  public void newUserSignedUp(UserRequest user, Callback<User> callback) {
    user.lastSeenUserAgent = System.getProperty("http.agent");
    user.newSession = true;
    user.updateLastSeen = true;
    user.signUpTime = System.currentTimeMillis() / 1000L;
    this.intercomClient.getUserService().createNewSession(user, callback);
  }
}
