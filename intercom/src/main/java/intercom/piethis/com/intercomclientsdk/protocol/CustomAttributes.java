package intercom.piethis.com.intercomclientsdk.protocol;

import com.google.gson.annotations.SerializedName;

/**
 * User: msk
 * Date: 24/2/15
 */
public class CustomAttributes {
  @SerializedName("android_version")
  public String androidVersion;

  @SerializedName("android_app_version")
  public String androidAppVersion;
}
