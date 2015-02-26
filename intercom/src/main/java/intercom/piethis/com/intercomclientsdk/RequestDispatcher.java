package intercom.piethis.com.intercomclientsdk;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import intercom.piethis.com.intercomclientsdk.internal.AppConstants;
import intercom.piethis.com.intercomclientsdk.internal.IntercomClient;
import intercom.piethis.com.intercomclientsdk.protocol.CustomAttributes;
import intercom.piethis.com.intercomclientsdk.protocol.User;
import intercom.piethis.com.intercomclientsdk.protocol.UserRequest;
import intercom.piethis.com.intercomclientsdk.utils.NetworkUtils;
import intercom.piethis.com.intercomclientsdk.utils.VersionUtils;
import retrofit.client.Response;
import timber.log.Timber;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * User: msk
 * Date: 26/2/15
 */
public class RequestDispatcher {
  private static final String SENDER_THREAD_NAME = AppConstants.THREAD_PREFIX + "Sender";

  private static final int SENDING_USER_ACTION = 0;

  final HandlerThread dispatcherThread;

  final Handler handler;

  private final Intercom intercom;

  private final IntercomClient intercomClient;

  RequestDispatcher(Intercom intercom, IntercomClient intercomClient) {
    this.intercom = intercom;
    this.intercomClient = intercomClient;
    this.dispatcherThread = new HandlerThread(SENDER_THREAD_NAME, THREAD_PRIORITY_BACKGROUND);
    this.dispatcherThread.start();
    this.handler = new DispatcherHandler(dispatcherThread.getLooper(), this);
    Timber.d("Starting Thread for Dispatcher requests.");
  }

  void shutdown() {
    dispatcherThread.quit();
  }

  public void dispatchSendingAction(String uuid) {
    handler.sendMessage(handler.obtainMessage(SENDING_USER_ACTION, uuid));
  }

  public void dispatchUpdate(UserRequest user) {
    handler.sendMessage(handler.obtainMessage(SENDING_USER_ACTION, user));
  }

  private static class DispatcherHandler extends Handler {

    private final RequestDispatcher queueDispatcher;

    public DispatcherHandler(Looper looper, RequestDispatcher queueDispatcher) {
      super(looper);
      this.queueDispatcher = queueDispatcher;
    }

    @Override
    public void handleMessage(final Message msg) {
      Timber.d("Handle Message %s", msg);

      switch (msg.what) {
        case SENDING_USER_ACTION:
          queueDispatcher.handleUserUpdate((UserRequest) msg.obj);
          break;
      }
    }
  }

  // Custom method to convert stream from request to string
  public static String fromStream(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    StringBuilder out = new StringBuilder();
    String newLine = System.getProperty("line.separator");
    String line;
    while ((line = reader.readLine()) != null) {
      out.append(line);
      out.append(newLine);
    }
    return out.toString();
  }

  Gson gson = new Gson();

  private void handleUserUpdate(UserRequest user) {
    try {
      user.newSession = true;
      user.lastSeenIPAddress = NetworkUtils.getExternalIP();
      user.updateLastSeen = true;
      if (user.customAttributes == null) {
        user.customAttributes = new CustomAttributes();
      }
      user.customAttributes.androidVersion = VersionUtils.getAndroidVersion();
      Response response = this.intercomClient.getUserService().createNewSession(user);
      User userResponse = gson.fromJson(fromStream(response.getBody().in()), User.class);
      this.intercom.dispatchCallback(user.requestId, response, userResponse);
    } catch (Exception e) {
      Timber.e("Error Processing request %s\n%s", user.requestId, e);
      this.intercom.dispatchCallback(user.requestId, null, null, e);
    }
  }
}
