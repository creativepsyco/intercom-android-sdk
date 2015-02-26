package intercom.piethis.com.intercomclientsdk.interfaces;

import intercom.piethis.com.intercomclientsdk.protocol.User;
import intercom.piethis.com.intercomclientsdk.protocol.UserListReponse;
import intercom.piethis.com.intercomclientsdk.protocol.UserRequest;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.*;

/**
 * User: msk
 * Date: 12/02/2015
 */
public interface Users {

  @GET("/users")
  public void getUsers(Callback<UserListReponse> objectCallback);

  @POST("/users")
  public Response createNewSession(@Body UserRequest user);

  @DELETE("/users")
  public void deleteUserByEmail(@Query("email") String email, Callback<User> callback);

  @DELETE("/users")
  public void deleteUserByUserId(@Query("user_id") String userId, Callback<User> callback);
}
