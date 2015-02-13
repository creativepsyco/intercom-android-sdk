package intercom.piethis.com.intercomclientsdk.interfaces;

import intercom.piethis.com.intercomclientsdk.protocol.User;
import intercom.piethis.com.intercomclientsdk.protocol.UserListReponse;
import intercom.piethis.com.intercomclientsdk.protocol.UserRequest;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * User: msk
 * Date: 12/02/2015
 */
public interface Users {

  @GET("/users")
  public void getUsers(Callback<UserListReponse> objectCallback);

  @POST("/users")
  public void createNewSession(@Body UserRequest user, Callback<User> callback);
}
