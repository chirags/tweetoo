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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.Parameter;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class YahooOAuthProvider extends DefaultOAuthProvider {

  //Yahoo! OAuth APIs  
	protected static final String requestTokenApi = "https://api.login.yahoo.com/oauth/v2/get_request_token";
	protected static final String accessTokenApi = "https://api.login.yahoo.com/oauth/v2/get_token";
	protected static final String authorizationApi = "https://api.login.yahoo.com/oauth/v2/request_auth";

	protected OAuthConsumer consumer;
	protected Map<String, String> responseParameters;

  public YahooOAuthProvider(OAuthConsumer consumer) {
    super(consumer, requestTokenApi, accessTokenApi, authorizationApi);
    this.consumer = consumer;
  }

  @Override
  public String retrieveRequestToken(String callback) throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException {
    
    String currentTime = Long.toString(System.currentTimeMillis() / 1000L);
    Set<String> reqParams = new HashSet<String>(7);
    reqParams.add("oauth_nonce="+currentTime);
    reqParams.add("oauth_timestamp="+currentTime);
    reqParams.add("oauth_consumer_key="+consumer.getConsumerKey());
    reqParams.add("oauth_signature_method=plaintext");
    reqParams.add("oauth_signature="+consumer.getConsumerSecret() + "%26");
    reqParams.add("oauth_version=1.0");
    reqParams.add("oauth_callback="+callback);

    responseParameters = fetchToken(requestTokenApi, reqParams);

    String token = responseParameters.get(OAuth.OAUTH_TOKEN);
    String secret = responseParameters.get(OAuth.OAUTH_TOKEN_SECRET);
    consumer.setTokenWithSecret(token, secret);
    
		return OAuth.addQueryParameters(authorizationApi, OAuth.OAUTH_TOKEN, consumer.getToken());
	}

  @Override
	public void retrieveAccessToken(String verifier) throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException {
		if (consumer.getToken() == null || consumer.getTokenSecret() == null) {
			throw new OAuthExpectationFailedException("Need a valid request token and secret for an access token");
		}

    String currentTime = Long.toString(System.currentTimeMillis() / 1000L);
    Set<String> reqParams = new HashSet<String>(7);
    reqParams.add("oauth_nonce="+currentTime);
    reqParams.add("oauth_timestamp="+currentTime);
    reqParams.add("oauth_consumer_key="+consumer.getConsumerKey());
    reqParams.add("oauth_signature_method=plaintext");
    reqParams.add("oauth_signature="+consumer.getConsumerSecret() + "%26" + consumer.getTokenSecret());
    reqParams.add("oauth_version=1.0");
    reqParams.add("oauth_token="+consumer.getToken());
    reqParams.add("oauth_verifier="+verifier);

		fetchToken(accessTokenApi, reqParams);
	}

  @Override
  public Map<String, String> getResponseParameters() {
    return responseParameters;
  }

  private Map<String, String> fetchToken(String api, Set<String> reqParams) throws OAuthCommunicationException {
    HttpURLConnection connection = null;
    try {
      URL req = new URL(api + "?" + StringUtils.join(reqParams, "&"));
      connection = (HttpURLConnection) req.openConnection();

      List<Parameter> params = OAuth.decodeForm(connection.getInputStream());
      responseParameters = OAuth.toMap(params);
      return OAuth.toMap(params);
    }
    catch(Exception e) {
      throw new OAuthCommunicationException(e);
    }
    finally {
      if(connection != null) {
        connection.disconnect();
      }
    }
  }
}
