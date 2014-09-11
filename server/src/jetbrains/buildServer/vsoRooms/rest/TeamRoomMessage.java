/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.vsoRooms.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Evgeniy.Koshkin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRoomMessage {
  private long myId;
  private String myContent;
  private String myMessageType;
  private long myPostedRoomId;

  public long getId() {
    return myId;
  }

  public String getContent() {
    return myContent;
  }

  public String getMessageType() {
    return myMessageType;
  }

  public long getPostedRoomId() {
    return myPostedRoomId;
  }

  public void setId(long id) {
    myId = id;
  }

  public void setContent(String content) {
    myContent = content;
  }

  public void setMessageType(String messageType) {
    myMessageType = messageType;
  }

  public void setPostedRoomId(long postedRoomId) {
    myPostedRoomId = postedRoomId;
  }
}
