

package jetbrains.buildServer.vsoRooms.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Evgeniy.Koshkin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRoomList {

  private List<TeamRoom> myRooms;

  public List<TeamRoom> getRooms() {
    return myRooms;
  }

  public void setValue(List<TeamRoom> value) {
    myRooms = value;
  }
}