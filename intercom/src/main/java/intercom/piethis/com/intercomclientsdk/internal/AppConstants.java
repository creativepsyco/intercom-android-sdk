package intercom.piethis.com.intercomclientsdk.internal;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class AppConstants {
  public final static String API_HOST = "https://api.intercom.io/";

  public static final String THREAD_PREFIX = "Pie-Intercom-";

  public static void setExternalIPHostUr(String externalIPHostUr) {
    AppConstants.externalIPHostUr = externalIPHostUr;
  }

  private static String externalIPHostUr = "https://api.pie.co/v2/info";

  public static String getExternalIPHostUrl() {
    return externalIPHostUr;
  }
}
