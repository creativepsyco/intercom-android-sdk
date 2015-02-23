package intercom.piethis.com.intercomclientsdk.protocol;

import com.google.gson.annotations.SerializedName;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class Company {
  @SerializedName("company_id")
  public String companyId;

  @SerializedName("name")
  public String name;
}
