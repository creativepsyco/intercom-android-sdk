Intercom Android SDK
====================
[![Download](https://api.bintray.com/packages/piethis/maven/Intercom-Android/images/download.svg) ](https://bintray.com/piethis/maven/Intercom-Android/_latestVersion)

Implements a very minimal subset of Intercom APIs generated on the model of the cURL variants. The original [intercom android SDK][1] is still under private beta. So for now, this repo should suffice.

##Integrating

* Put the following in your `local.properties` file:

  ```bash
  intercom.appId="YOUR_INTERCOM_APP_ID"
  intercom.apiKey="YOUR_INTERCOM_API_KEY"
  ```
* Put the following lines in `build.gradle` of your app:

  ```java
  compile 'com.piethis.intercom:intercom:0.1'
  ```

##Open a session

```java
IntercomConfig config = new IntercomConfig();
config.setAppKey(BuildConfig.AppKey);
config.setAppId(BuildConfig.AppId);
intercom = new Intercom(config);

UserRequest request = new UserRequest();
request.email = user.getEmail();
request.id = String.valueOf(user.getId());
request.name = user.getDisplayName();
intercom.newUserSignedUp(request, callback);
```

##License

    The MIT License (MIT)

    Copyright (c) 2015 Mohit Kanwal

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.


[1]: https://github.com/intercom/intercom-android
