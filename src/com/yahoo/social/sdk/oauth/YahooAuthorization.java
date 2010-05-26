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

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.signature.SignatureMethod;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import java.util.Map;

public class YahooAuthorization {
  private static final String XOAUTH_YAHOO_GUID = "xoauth_yahoo_guid";
  private static final String OAUTH_SESSION_HANDLE = "oauth_session_handle";
  private static final String OAUTH_EXPIRES_IN = "oauth_expires_in";

  public static YahooRequestToken createAuthorizationUrl(String ck, String cks, String callback)
  throws Exception {
    OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ck, cks, SignatureMethod.PLAINTEXT);
    OAuthProvider provider = new YahooOAuthProvider(consumer);
    provider.setOAuth10a(true);
    
    String authUrl = provider.retrieveRequestToken(callback);
    return new YahooRequestToken(consumer.getToken(), consumer.getTokenSecret(), authUrl);
  }

  public static YahooAccessToken getAccessToken(String ck, String cks, YahooRequestToken token, String verifier)
  throws Exception {
    OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ck, cks, SignatureMethod.PLAINTEXT);
    consumer.setTokenWithSecret(token.getRequestToken(), token.getRequestTokenSecret());

    OAuthProvider provider = new YahooOAuthProvider(consumer);
    provider.setOAuth10a(true);
    
    provider.retrieveAccessToken(verifier);

    Map<String, String> params = provider.getResponseParameters();
    return new YahooAccessToken(
            params.get(XOAUTH_YAHOO_GUID),
            consumer.getToken(),
            consumer.getTokenSecret(),
            params.get(OAUTH_SESSION_HANDLE),
            new Long(params.get(OAUTH_EXPIRES_IN)));
  }


}
