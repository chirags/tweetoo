/**
 * Yahoo! Java SDK
 *
 *  * Yahoo! Query Language
 *  * Yahoo! Social API
 *
 * @package    yos-social-java
 * @subpackage yahoo
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
package com.yahoo.social.sdk.pojo;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

public class Contacts {
    public int start, count, total;
    public String created, updated, uri;
    public ArrayList<Contact> contact;

    @JsonCreator
    public Contacts(@JsonProperty("start") int start,
                    @JsonProperty("count") int count,
                    @JsonProperty("total") int total,
                    @JsonProperty("uri") String uri,
                    @JsonProperty("created") String created,
                    @JsonProperty("updated") String updated,
                    @JsonProperty("contact") ArrayList<Contact> contact) {
      this.start = start; this.count = count; this.total = total;
      this.uri = uri; this.created = created; this.updated = updated;
      this.contact = contact;
    }

    public static class Contact {
      public int id;
      public String created, updated, uri;
      public Boolean isConnection;
      public ArrayList<Field> fields;
      public ArrayList<Category> categories;

      @JsonCreator      
      public Contact(@JsonProperty("created") String created,
                     @JsonProperty("updated") String updated,
                     @JsonProperty("uri") String uri,
                     @JsonProperty("isConnection") Boolean isConnection,
                     @JsonProperty("id") int id,
                     @JsonProperty("fields") ArrayList<Field> fields,
                     @JsonProperty("categories")ArrayList<Category> categories) {
        this.created = created;
        this.updated = updated;
        this.uri = uri;
        this.isConnection = isConnection;
        this.id = id;
        this.fields = fields;
        this.categories = categories;
      }
    }
  }
