/**
 * Yahoo! Java SDK
 *
 *  * Yahoo! Query Language
 *  * Yahoo! Social API
 *
 * @package    yos-social-java
 *
 * @author     Chirag Shah <chiragshah1@gmail.com>
 * @copyright  Copyrights for code authored by Chirag Shah. Licensed under the following terms:
 * @license    BSD Open Source License
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 **/
package com.yahoo.social.sdk.samples;

import oauth.signpost.OAuth;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.yahoo.social.sdk.oauth.*;
import com.yahoo.social.sdk.pojo.Contacts;

public class YahooSample {
  private static final String CK = "";
  private static final String CKS = "";
  
  public static void main(String[] args) {
    try {
      YahooRequestToken requestToken = YahooAuthorization.createAuthorizationUrl(CK, CKS, OAuth.OUT_OF_BAND);
      System.out.println("Now visit:\n" + requestToken.getAuthorizationUrl() + "\n... and grant this app authorization");
      System.out.println("Enter the verification code and hit ENTER when you're done:");

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String verificationCode = br.readLine();

      // Exchange the request token for an access token
      YahooAccessToken accessToken = YahooAuthorization.getAccessToken(CK, CKS, requestToken, verificationCode);

      // We're authorizated to hit services
      YahooApplication app = new YahooApplication(CK, CKS, accessToken);
      String resp = app.yqlQuery("select%20message%20from%20social.presence%20where%20guid%3Dme&format=json");

      System.out.println("Response: " + resp);

      Contacts contacts = app.getContacts(0, 10).contacts;
      System.out.println(contacts.created);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
