/**
 * Tweetoo Twitter API Wrapper
 *
 * @package    twity
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
package twity.twitter;

import oauth.signpost.OAuthConsumer;

import java.net.URLEncoder;
import java.net.URL;
import java.net.HttpURLConnection;

import com.google.appengine.api.urlfetch.*;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.Expiration;
import org.json.JSONObject;
import org.json.JSONArray;

public class Twitter {
  private static final String TWIT_FRIENDS_URL = "http://twitter.com/statuses/friends_timeline.json?count=20";  
  private static final String TWIT_VERIFY_URL = "http://twitter.com/account/verify_credentials.json";
  private static final String TWIT_SEARCH_URL = "http://search.twitter.com/search.json?q=";

  private static final MemcacheService cache = MemcacheServiceFactory.getMemcacheService();  
  private static final URLFetchService fetch = URLFetchServiceFactory.getURLFetchService();
  
  private OAuthConsumer consumer;
  private String guid;

  public Twitter(OAuthConsumer consumer, String guid) {
    this.consumer = consumer;
    this.guid = guid;
  }

  public JSONArray fetchUserFriendTweets() throws Exception {
    String key = guid + ":friends";
    if(cache.contains(key)) {
      try {
        return new JSONArray((String) cache.get(key));
      } catch (Exception e) {}        
    }

    String friendData = fetch(TWIT_FRIENDS_URL, consumer);
    cache.put(key, friendData, Expiration.byDeltaSeconds(60));
    return new JSONArray(friendData);
  }


  public JSONObject fetchUserData() throws Exception {
    String key = guid + ":metadata";
    if(cache.contains(key)) {
      try {
        return new JSONObject((String) cache.get(key));
      } catch (Exception e) {}
    }

    String userData = fetch(TWIT_VERIFY_URL, consumer);
    cache.put(key, userData);
    return new JSONObject(userData);
  }

  public void updateStatus(String status) {
    Long start = System.currentTimeMillis();
    try {
      //Why does sign-post require a HttpURLConnection?
      status = URLEncoder.encode(status);
      URL url = new URL("http://twitter.com/statuses/update.json?status="+status);
      HttpURLConnection request = (HttpURLConnection) url.openConnection();
      request.setRequestMethod("POST");
      consumer.sign(request);

      HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST);
      req.addHeader(new HTTPHeader("Authorization", request.getRequestProperty("Authorization")));
      fetch.fetch(req);
      cache.delete(guid + ":friends");
    } catch (Exception e) {
      System.err.println("unable to update status: " + e.getMessage());
    }

    Long stop = System.currentTimeMillis();
    System.err.println("Execution time for update status is = " + (stop - start));
  }

  public static String fetch(String urlString, OAuthConsumer consumer) throws Exception {
      Long start = System.currentTimeMillis();
      URL url = new URL(urlString);
      HttpURLConnection request = (HttpURLConnection) url.openConnection();
      consumer.sign(request);

      HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET);
      req.addHeader(new HTTPHeader("Authorization", request.getRequestProperty("Authorization")));

      HTTPResponse resp = fetch.fetch(req);

      Long stop = System.currentTimeMillis();
      System.err.println("Execution time for url:"+urlString+" is=" + (stop-start)+", code="+resp.getResponseCode());

      return new String(resp.getContent());
    }
}
