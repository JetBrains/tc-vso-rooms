

package jetbrains.buildServer.vsoRooms.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Evgeniy.Koshkin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRoom {
  private long myId;
  private String myName;
  private String myDescription;

  public TeamRoom() {
  }

  public TeamRoom(long id, String name, String description) {
    myId = id;
    myName = name;
    myDescription = description;
  }

  public long getId() {
    return myId;
  }

  public String getName() {
    return myName;
  }

  public String getDescription() {
    return myDescription;
  }

  public void setId(long id) {
    myId = id;
  }

  public void setName(String name) {
    myName = name;
  }

  public void setDescription(String description) {
    myDescription = description;
  }
}