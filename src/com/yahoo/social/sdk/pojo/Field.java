/**
 * Yahoo! Java SDK
 *  * Yahoo! Query Language
 *  * Yahoo! Social API
 *
 * @package    yos-social-java
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

public class Field {
  public int id;
  public String uri, created, updated, editedBy, type;
  public String title, nickname, image;
  public Boolean isConnection;

  @JsonCreator
  public Field(@JsonProperty("id") int id,
               @JsonProperty("uri") String uri,
               @JsonProperty("created") String created,
               @JsonProperty("updated") String updated,
               @JsonProperty("editedBy") String editedBy,
               @JsonProperty("type") String type,
               //@JsonProperty("value") String value,
               //@JsonProperty("flags") String flags,
               @JsonProperty("title") String title,
               @JsonProperty("nickname") String nickname,
               @JsonProperty("image") String image,
               @JsonProperty("isConnection") Boolean isConnection) {
    this.id = id; this.uri = uri;
    this.created = created;
    this.updated = updated;
    this.editedBy = editedBy;
    this.type = type;
    this.title = title; this.nickname = nickname; this.image = image;
    this.isConnection = isConnection;
  }
}
