package intercom.piethis.com.intercomclientsdk.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import intercom.piethis.com.intercomclientsdk.Intercom;

/**
 * User: msk
 * Date: 23/2/15
 */
public class VersionUtils {
  private static String versionString;

  private static Pattern PATTERN = Pattern.compile("Android (\\d+(?:\\.\\d+)+)");

  private VersionUtils() {
  }

  public static String sanitizeVersionString() {
    /**
     * Actual Dalvik/1.6.0 (Linux; U; Android 4.4.4; SM-N910G Build/KTU84P)
     * Expected Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 Chrome/40.0.2214.115
     * Android/Version (Linux; Android 4_4_4) ...
     */

    if (TextUtils.isEmpty(versionString))
      versionString = getWebSettingsUserAgent();
    return versionString;
  }

  /**
   * Returns a user-agent string. Returns either the U-A used by the web view settings or the
   * default http.agent system property.
   */
  public static String getWebSettingsUserAgent() {
    try {
      if (Build.VERSION.SDK_INT >= 17) {
        return WebSettings.getDefaultUserAgent(Intercom.getApplicationContext());
      }
      Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor(Context.class, WebView.class);
      constructor.setAccessible(true);
      try {
        WebSettings settings = constructor.newInstance(Intercom.getApplicationContext(), null);
        return settings.getUserAgentString();
      } finally {
        constructor.setAccessible(false);
      }
    } catch (Exception e) {
      return System.getProperty("http.agent");
    }
  }


  public static String getAndroidVersion() {
    String userAgentString = getWebSettingsUserAgent();
    Matcher matcher = PATTERN.matcher(userAgentString);
    if (matcher.find()) {
      return matcher.group(0);
    }
    return "";
  }
}
