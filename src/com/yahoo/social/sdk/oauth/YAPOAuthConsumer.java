package com.yahoo.social.sdk.oauth;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.HashMap;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.basic.HttpRequestAdapter;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.SignatureMethod;

@SuppressWarnings("serial")
public class YAPOAuthConsumer extends AbstractOAuthConsumer {
    public YAPOAuthConsumer(String consumerKey, String consumerSecret, SignatureMethod signatureMethod) {
        super(consumerKey, consumerSecret, signatureMethod);
    }

    @Override
    protected HttpRequest wrap(Object request) {
        if (!(request instanceof HttpURLConnection)) {
            throw new IllegalArgumentException("The default consumer expects requests of type java.net.HttpURLConnection");
        }
        return new HttpRequestAdapter((HttpURLConnection) request);
    }

    @Override
    protected Map<String, String> buildOAuthParameterMap() {
      return new HashMap<String, String>();
    }

}
