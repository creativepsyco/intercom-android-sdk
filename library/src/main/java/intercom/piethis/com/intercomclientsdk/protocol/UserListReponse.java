package intercom.piethis.com.intercomclientsdk.protocol;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class UserListReponse {

  @SerializedName("users")
  public List<User> users;
}
