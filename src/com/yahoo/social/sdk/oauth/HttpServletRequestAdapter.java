package com.yahoo.social.sdk.oauth;

import oauth.signpost.http.HttpRequest;
import oauth.signpost.OAuth;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: chirags
 * Date: Oct 22, 2009
 * Time: 2:42:24 PM
 */
public class HttpServletRequestAdapter implements HttpRequest {

    protected HttpServletRequest request;

    public HttpServletRequestAdapter(HttpServletRequest request) {
        this.request = request;
    }

    public String getMethod() {
        return "POST";
    }

    public String getRequestUrl() {
        StringBuffer url = request.getRequestURL();
        Enumeration e = request.getParameterNames();
        String key = (String) e.nextElement();
        String val = request.getParameter(key);
        url.append("?" + key + "=" + val);
        while (e.hasMoreElements()) {
          key = (String) e.nextElement();
          val = request.getParameter(key);
          if(!key.equalsIgnoreCase(OAuth.OAUTH_SIGNATURE)) {
            url.append("&" + key + "=" + val);
            
          }

        }

      return url.toString();
    }

    public void setHeader(String name, String value) {
        request.setAttribute(name, value);
    }

    public String getHeader(String name) {
        return (String) request.getAttribute(name);
    }

    public InputStream getMessagePayload() throws IOException {
        return request.getInputStream();
    }

    public String getContentType() {
        return request.getContentType();
    }
}