/**
 * Tweetoo Servlet
 *
 * @package    twity
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
package twity;

import com.yahoo.social.sdk.oauth.YAPOAuth;
import com.yahoo.social.sdk.oauth.YahooAccessToken;
import com.yahoo.social.sdk.oauth.YahooApplication;
import oauth.signpost.basic.*;
import oauth.signpost.*;
import oauth.signpost.signature.SignatureMethod;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import javax.servlet.http.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.appengine.api.memcache.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import twity.datastore.EMF;
import twity.datastore.User;
import twity.twitter.Twitter;

public class TwityServlet extends HttpServlet {
    private static final String YAP_CK = "TODO: INSERT_YAHOO_CONSUMER_KEY";
    private static final String YAP_CKS = "TODO: INSERT_YAHOO_CONSUMER_SECRET";
    private static final String YAP_APPID = "TODO: INSERT_YAHOO_APPID";
  
    private static final String TWIT_CK = "TODO: TWITTER_CONSUMER_KEY";
    private static final String TWIT_CKS = "TODO: TWITTER_CONSUMER_SECRET";
    private static final String TWIT_RT_URL = "http://twitter.com/oauth/request_token";
    private static final String TWIT_AT_URL = "http://twitter.com/oauth/access_token";
    private static final String TWIT_AUTH_URL = "http://twitter.com/oauth/authorize";

    private static final Pattern pattern = Pattern.compile("https?://\\S+");  
    private static final MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
  
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      resp.setContentType("text/html;charset=UTF-8");
      if (!YAPOAuth.verifySignature(req, YAP_CK, YAP_CKS)) {
        return;
      }
      
      String guid = req.getParameter("yap_viewer_guid");
      if (StringUtils.isBlank(guid)) {
        return;
      }

      OAuthConsumer consumer = new DefaultOAuthConsumer(TWIT_CK, TWIT_CKS, SignatureMethod.HMAC_SHA1);
      OAuthProvider provider = new DefaultOAuthProvider(consumer, TWIT_RT_URL, TWIT_AT_URL, TWIT_AUTH_URL);
      provider.setOAuth10a(true);

      try {
        if(cache.contains(guid+":RT")) {
          JSONObject token = new JSONObject(((String) cache.get(guid+":RT")));
          cache.delete(guid+":RT");
          consumer.setTokenWithSecret(token.getString("token"), token.getString("secret"));

          String verifier = req.getParameter("verifier");
          if(null != verifier) {
            provider.retrieveAccessToken(verifier);
            persistUser(guid, consumer);
          }
        }

        User u = fetchUser(guid);
        if(null != u) {
          consumer.setTokenWithSecret(u.getAccessToken(), u.getAccessTokenSecret());
          Twitter t = new Twitter(consumer, guid);
          
          String status = req.getParameter("status");
          String rCrumb = req.getParameter("crumb");
          if(null != status && !"".equals(status)) {
            String storedCrumb = (String) cache.get(guid+":CRUMB");
            cache.delete(guid+":CRUMB");

            if(storedCrumb.equals(rCrumb)) {
              status = status.trim();
              t.updateStatus(status);
            }
          }
          
          String id = req.getParameter("id");
          if(null != id && "insertTweets".equals(id)) {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().print(generateMarkup(t.fetchUserFriendTweets()));
            return;
          }
          
          JSONObject data = t.fetchUserData();
          req.setAttribute("screen_name", data.optString("screen_name"));
          req.setAttribute("profile_text_color", data.optString("profile_text_color"));
          req.setAttribute("profile_background_color", data.optString("profile_background_color"));
          req.setAttribute("profile_background_image_url", data.optString("profile_background_image_url"));

          String tweetMarkup = generateMarkup(t.fetchUserFriendTweets());
          req.setAttribute("tweets", tweetMarkup);

          // Generate session crumb
          String sessionCrumb = Integer.toString(new Random().nextInt());
          req.setAttribute("crumb", sessionCrumb);
          cache.put(guid+":CRUMB", sessionCrumb);

          getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);
          return;
        }

        //The callback is a proxy that simply rewrites the oauth_verifier to verifier (YAP Hack)
        String authUrl = provider.retrieveRequestToken("http://chiarg.com/redirect.php");
        cache.put(guid+":RT", new JSONObject().put("token", consumer.getToken()).put("secret", consumer.getTokenSecret()).toString());
        req.setAttribute("authUrl", authUrl);
        getServletContext().getRequestDispatcher("/auth.jsp").forward(req,resp);
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error: " + e.getMessage());
      }
    }

    private String generateMarkup(JSONArray statuses) throws JSONException {
      JSONObject status;
      String sn, text, pic, fn;
      StringBuilder sb = new StringBuilder();
      for(int i=0; i<statuses.length(); i++) {
        status = statuses.optJSONObject(i);
        text = status.optString("text");
        Matcher urlMatcher = pattern.matcher(text);
        String match;

        try {
          while (urlMatcher.find()) {
            match = urlMatcher.group(0);
            text = text.replace(match, "<a href=\""+match+"\">"+match+"</a>");
          }
        } catch(Exception e) {}
        
        
        sn = status.optJSONObject("user").optString("screen_name");
        fn = status.optJSONObject("user").optString("name");
        pic = status.optJSONObject("user").optString("profile_image_url");

        sb.append("<ul style='margin:0;padding:0'>");
        sb.append("<ol style='margin:0;padding:0;list-style-type:none;'>");
        sb.append("<li style='margin: 0 0 .2em 0; padding: .3em .9em;'>");
        sb.append("<span style='display:block;float:left;height:50px;width:50px;'>" +
                  "<a style='border-style:none;text-decoration:none;color:white;' href='http://twitter.com/'"+sn+"'><img width='48' height='48' src='"+pic+"' class='photo fn' alt='"+fn+"'/></a></span>");
        sb.append("<span style='display:block;margin-left:56px;min-height:50px;overflow:hidden;'><strong><a href='http://twitter.com/");
        sb.append(sn);
        sb.append("'> ");
        sb.append(sn);
        sb.append("</a></strong> ");
        sb.append(text);
        sb.append("</span></li>");
      }
      sb.append("</ul>");
      return sb.toString();   
    }


    private void persistUser(String guid, OAuthConsumer consumer) {
      EntityManager em = EMF.get().createEntityManager();
      User u = new User(guid, consumer.getToken(), consumer.getTokenSecret());      
      try {
        em.persist(u);
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("unable to persist user: " + guid);
      } finally {
        em.close();
      }
    }

    private User fetchUser(String uid) {
      try {
        EntityManager em = EMF.get().createEntityManager();
        Query jpaQuery = em.createQuery("SELECT u FROM User u WHERE u.userId = :uid");
        jpaQuery.setParameter("uid", uid);
        return (User) jpaQuery.getSingleResult();
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
}
