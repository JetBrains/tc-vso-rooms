

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