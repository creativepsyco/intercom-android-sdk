package intercom.piethis.com.intercomclientsdk.protocol;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class UserRequest {
  @SerializedName("app_id")
  public String appId;

  @SerializedName("created_at")
  public Long createTimestamp;

  @SerializedName("id")
  public String id;

  @SerializedName("email")
  public String email;

  @SerializedName("name")
  public String name;

  @SerializedName("new_session")
  public boolean newSession;

  @SerializedName("user_id")
  public String userId;

  @SerializedName("signed_up_at")
  public Long signUpTime;

  @SerializedName("last_seen_user_agent")
  public String lastSeenUserAgent;

  @SerializedName("update_last_request_at")
  public boolean updateLastSeen;

  @SerializedName("companies")
  public List<Company> companies;

  @SerializedName("last_seen_ip")
  public String lastSeenIPAddress;

  @SerializedName("custom_attributes")
  public CustomAttributes customAttributes;
}
