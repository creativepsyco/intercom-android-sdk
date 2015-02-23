package intercom.piethis.com.intercomclientsdk.utils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Constructor;

import intercom.piethis.com.intercomclientsdk.Intercom;

/**
 * User: msk
 * Date: 23/2/15
 */
public class VersionUtils {
  private static String versionString;

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

  public static String getWebSettingsUserAgent() {
    try {
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
}
