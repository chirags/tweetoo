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

import com.google.appengine.api.urlfetch.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.signature.SignatureMethod;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;

import com.yahoo.social.sdk.pojo.ContactsWrapper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class YahooApplication {

  //Yahoo! Social APIs
  private static final String YAHOO_SOCIAL_API = "http://social.yahooapis.com/v1";
  private static final String YQL_QUERY_API = "http://query.yahooapis.com/v1/yql";

  private YahooAccessToken token;

  private String consumerKey;
  private String consumerSecret;

  private OAuthConsumer consumer;
  private OAuthProvider oAuthProvider;

  private static final URLFetchService fetch = URLFetchServiceFactory.getURLFetchService();
  private static final ObjectMapper mapper = new ObjectMapper();
  
  public YahooApplication(String consumerKey, String consumerSecret, YahooAccessToken token) {
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.token = token;

    this.consumer = new DefaultOAuthConsumer(this.consumerKey, this.consumerSecret, SignatureMethod.HMAC_SHA1);
    this.consumer.setTokenWithSecret(token.getAccessToken(), token.getAccessTokenSecret());

    this.oAuthProvider = new DefaultOAuthProvider(this.consumer, null, null, null);
    this.oAuthProvider.setOAuth10a(true);

    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);    
  }
  
  /**
   * Executes the given YQL query.
   *
   * @param query The query to execute.
   * @return The response or NULL if the request fails..
   * @throws Exception
   */
  public String yqlQuery(String query) throws Exception {
    String uri = YQL_QUERY_API + "?q=" + query;
    return fetch(uri, "GET");
  }

  /**
   * Executes the given YQL insert query.
   *
   * @param query The query to execute.
   * @return The response or NULL if the request fails..
   * @throws Exception
   */
  public String yqlInsertQuery(String query) throws Exception {
    String uri = YQL_QUERY_API + "?q=" + query;
    return fetch(uri, "PUT");
  }

  /**
   * Gets a list of contacts for the current user.
   *
   * @param start The starting offset.
   * @param count The number of contacts to fetch.
   * @return List of contacts for the current user.
   */
  public ContactsWrapper getContacts(int start, int count) throws JsonMappingException {
    StringBuilder sb = new StringBuilder()
            .append(YAHOO_SOCIAL_API).append("/user/").append(token.getGuid())
            .append("/contacts;start=").append(start).append(";count=").append(count)
            .append("?format=json");

    String resp = fetch(sb.toString(), "GET");
    try {
      return mapper.readValue(resp, ContactsWrapper.class);
    } catch (Exception e) {
      return null;
    }
  }

  public String fetch(String uri, String method) {
    try {
      HttpURLConnection request = (HttpURLConnection) new URL(uri).openConnection();
      request.setRequestMethod(method);

      consumer.sign(request);
      String auth = request.getRequestProperty("Authorization")
            .trim()
            .replace("OAuth ", "")
            .replace(",", "&")
            .replace("\"","");

      HTTPRequest req = new HTTPRequest(
              new URL(uri + "&" + auth),
              "PUT".equals(method) ? HTTPMethod.PUT : HTTPMethod.GET);
      return new String(fetch.fetch(req).getContent());
    } catch(Throwable t) {
      return "";
    }
  }


  public String getConsumerKey() {
    return consumerKey;
  }

  public String getConsumerSecret() {
    return consumerSecret;
  }

  public OAuthConsumer getOAuthConsumer() {
    return consumer;
  }

  public OAuthProvider getOAuthProvider() {
    return oAuthProvider;
  }

}
