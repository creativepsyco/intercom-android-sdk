package intercom.piethis.com.intercomclientsdk.utils;

import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import intercom.piethis.com.intercomclientsdk.internal.AppConstants;

/**
 * User: msk
 * Date: 24/2/15
 */
public class NetworkUtils {
  private NetworkUtils() {
  }

  /**
   * Get IP address from first non-localhost interface
   *
   * @param useIPv4 true=return ipv4, false=return ipv6
   * @return address or empty string
   */
  public static String getIPAddress(boolean useIPv4) {
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress().toUpperCase();
            boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
            if (useIPv4) {
              if (isIPv4)
                return sAddr;
            } else {
              if (!isIPv4) {
                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                return delim < 0 ? sAddr : sAddr.substring(0, delim);
              }
            }
          }
        }
      }
    } catch (Exception ignored) {
    } // for now eat exceptions
    return "";
  }

  /**
   * Gets the external IP of the device
   * Don't use this method on the network thread
   *
   * @return a String composed of the IP
   */
  public static String getExternalIP() {
    String useurl = TextUtils.isEmpty(AppConstants.getExternalIPHostUrl())
        ? "http://ipinfo.io/ip"
        : AppConstants.getExternalIPHostUrl();
    try {
      HttpClient httpclient = new DefaultHttpClient();
      HttpGet httpget = new HttpGet(useurl);
      HttpResponse response;

      response = httpclient.execute(httpget);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        long len = entity.getContentLength();
        if (len != -1 && len < 1024) {
          return EntityUtils.toString(entity);
        }
      }
    } catch (Exception ignored) {
    }

    return "";
  }
}
