/**
 * Yahoo! Java SDK
 *
 *  * Yahoo! Query Language
 *  * Yahoo! Social API
 *
 * @package    yos-social-java
 *
 * @author     Chirag Shah <chiragshah1@gmail.com>
 * @copyright  Copyrights for code authored by Chirag Shah. is licensed under the following terms:
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

public class YahooRequestToken {
  String requestToken;
  String requestTokenSecret;
  String authorizationUrl;

  public YahooRequestToken(String requestToken, String requestTokenSecret, String authorizationUrl) {
    this.requestToken = requestToken;
    this.requestTokenSecret = requestTokenSecret;
    this.authorizationUrl = authorizationUrl;
  }

  public String getRequestToken() {
    return requestToken;
  }

  public String getRequestTokenSecret() {
    return requestTokenSecret;
  }

  public String getAuthorizationUrl() {
    return authorizationUrl;
  }
}
