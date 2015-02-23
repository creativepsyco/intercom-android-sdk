package intercom.piethis.com.intercomclientsdk.protocol;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class CompanyList {
  @SerializedName("companies")
  public List<Company> companies;

  public CompanyList() {
    this.companies = new ArrayList<>();
  }
}
