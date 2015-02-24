package intercom.piethis.com.intercomclientsdk;

import android.test.AndroidTestCase;
import android.util.Log;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import intercom.piethis.com.intercomclientsdk.protocol.Company;
import intercom.piethis.com.intercomclientsdk.protocol.User;
import intercom.piethis.com.intercomclientsdk.protocol.UserListReponse;
import intercom.piethis.com.intercomclientsdk.protocol.UserRequest;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * User: msk
 * Date: 12/02/2015
 */
public class IntercomTest extends AndroidTestCase {
  private static final String TAG = IntercomTest.class.getSimpleName();

  IntercomConfig config;

  Intercom intercom;

  String userId;

  String randomEmail;

  private SecureRandom random = new SecureRandom();

  @Override
  public void setUp() throws Exception {
    super.setUp();
    userId = String.valueOf(Math.abs(new Random().nextInt()));
    randomEmail = new BigInteger(130, random).toString(32) + "@piethis.com";

    config = new IntercomConfig();
    config.setAppId(BuildConfig.AppId);
    config.setAppKey(BuildConfig.AppKey);
    intercom = new Intercom(config, getContext());
    Log.d(TAG, "Setup called");
  }

  public void testIntercomGetUsers() throws Exception {
    final CountDownLatch signal = new CountDownLatch(1);
    intercom.getUsers(new Callback<UserListReponse>() {
      @Override
      public void success(UserListReponse userListReponse, Response response) {
        signal.countDown();
        assertNotNull(userListReponse);
        assertTrue(userListReponse.users != null);
        assertTrue(userListReponse.users.size() > 0);
      }

      @Override
      public void failure(RetrofitError error) {
        signal.countDown();
        /* Should check for network errors */
        fail(error.toString());
      }
    });
    signal.await();
  }


  public void testNewSession() throws Exception {
    final CountDownLatch signal = new CountDownLatch(1);
    randomEmail = "";
    intercom.beginNewSession(userId, new Callback<User>() {
      @Override
      public void success(User user, Response response) {
        signal.countDown();
        assertNotNull(user);
        assertEquals("User Ids dont match", userId, user.userId);
      }

      @Override
      public void failure(RetrofitError error) {
        signal.countDown();
        fail(error.toString());
      }
    });
    signal.await();
    _testDeleteUser();
  }

  public void testNewSessionWithCompany() throws Exception {
    final CountDownLatch signal = new CountDownLatch(1);
    Company company = new Company();
    company.companyId = "1";
    company.name = "Piethis Pte. Ltd";
    intercom.updateUser(userId, randomEmail, "Deep Singh", company, new Callback<User>() {
      @Override
      public void success(User user, Response response) {
        signal.countDown();
        assertEquals("Name not same", user.name, "Deep Singh");
        assertEquals("Email not same", user.email, randomEmail);
        assertNotNull(user.companies);
        assertNotNull(user.companies.companies);
        assertTrue(user.companies.companies.size() > 0);
        assertTrue(user.companies.companies.get(0).companyId.equals("1"));
      }

      @Override
      public void failure(RetrofitError error) {
        signal.countDown();
        fail(error.toString());
      }
    });
    signal.await();
    _testDeleteUser();
  }

  private void _testDeleteUser() throws Exception {
    final CountDownLatch signal = new CountDownLatch(1);
    UserRequest userRequest = new UserRequest();
    userRequest.userId = this.userId;
    userRequest.email = randomEmail;

    intercom.deleteUser(userRequest, new Callback<User>() {
      @Override
      public void success(User user, Response response) {
        signal.countDown();
        assertEquals("User id not same", user.userId, IntercomTest.this.userId);
        assertEquals("Email not same", user.email, randomEmail);
      }

      @Override
      public void failure(RetrofitError error) {
        signal.countDown();
        fail(error.toString());
      }
    });
    signal.await();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    /*intercom.deleteUser()*/
  }
}
