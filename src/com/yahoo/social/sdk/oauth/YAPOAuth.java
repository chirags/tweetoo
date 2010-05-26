package com.yahoo.social.sdk.oauth;

import javax.servlet.http.HttpServletRequest;
import oauth.signpost.signature.SignatureMethod;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuth;

public class YAPOAuth {
  public static Boolean verifySignature(HttpServletRequest req, String ck, String cks) {
    String incomingSignature = req.getParameter(OAuth.OAUTH_SIGNATURE);

    HttpServletRequestAdapter yapRequest = new HttpServletRequestAdapter(req);
    OAuthConsumer consumer = new YAPOAuthConsumer(ck, cks, SignatureMethod.HMAC_SHA1);
    try {
      consumer.sign(yapRequest);
    } catch (Exception e) {
      return false;
    }

    return(incomingSignature.equals(consumer.getSignature()));    
  }
}
