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
package com.yahoo.social.sdk.oauth;

public class YahooAccessToken {
  
  /*
   * The introspective GUID of the currently logged in User.
   * For more information of the GUID, visit
   * http://developer.yahoo.com/social/rest_api_guide/introspective-guid-resource.html
   */
  private final String guid;

  /*
   * The Access Token provides access to protected resources
   * accessible through Yahoo! Web services.
   */
  private final String accessToken;

  /*
   * The secret associated with the Access Token provided in hexstring format.
   */
  private final String accessTokenSecret;

  /*
   * The persistent credential used by Yahoo! to identify the Consumer after a
   * User has authorized access to their private data. This credential is included
   * in the request to refresh the Access Token once it expires.
   */
  private final String sessionHandle;

  /*
   * Lifetime of the Access Token in seconds (3600, or 1 hour).
   */
  private final long oauthExpiresIn;

  

  public YahooAccessToken(String guid, String at, String ats, String sessionHandle, long oauthExpiresIn) {
    this.guid = guid;
    this.accessToken = at;
    this.accessTokenSecret = ats;
    this.sessionHandle = sessionHandle;
    this.oauthExpiresIn = oauthExpiresIn;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getAccessTokenSecret() {
    return accessTokenSecret;
  }

  public String getGuid() {
    return guid;
  }

  public String getSessionHandle() {
    return sessionHandle;
  }

  public long getOAuthExpiresIn() {
    return oauthExpiresIn;
  }
}
