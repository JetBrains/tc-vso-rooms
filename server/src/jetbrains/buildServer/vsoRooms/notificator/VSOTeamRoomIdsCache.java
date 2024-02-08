

package jetbrains.buildServer.vsoRooms.notificator;

import com.intellij.openapi.util.Pair;
import jetbrains.buildServer.vsoRooms.rest.TeamRoom;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOTeamRoomIdsCache {

  private static final Logger LOG = Logger.getLogger(VSOTeamRoomIdsCache.class);

  private final Map<Pair<String, String>, Long> myNameToIdMap = new HashMap<Pair<String, String>, Long>();

  @Nullable
  public Long getOrResolveRoomId(@NotNull String vsoAccount, @NotNull String teamRoomName, @NotNull VSOTeamRoomsAPIConnection apiConnection) {
    final String accountToLower = vsoAccount.toLowerCase();
    final String teamRoomNameToLower = teamRoomName.toLowerCase();
    final Pair<String, String> roomUUID = new Pair<String, String>(accountToLower, teamRoomNameToLower);
    if(!myNameToIdMap.containsKey(roomUUID)){
      synchronized (myNameToIdMap){
        if(!myNameToIdMap.containsKey(roomUUID)){
          LOG.debug(String.format("Team room ID for room name %s is not resolved yet. Calling VSO API to list all Team Rooms for account %s", teamRoomName, vsoAccount));
          try {
            for (TeamRoom teamRoom : apiConnection.getListOfRooms(vsoAccount)) {
              final Pair<String, String> resolvedRoomUUID = new Pair<String, String>(accountToLower, teamRoom.getName().toLowerCase());
              if(!myNameToIdMap.containsKey(resolvedRoomUUID)){
                myNameToIdMap.put(resolvedRoomUUID, teamRoom.getId());
              }
            }
          } catch (Exception e) {
            LOG.warn("Failed to list team rooms for account " + vsoAccount, e);
          }
        }
      }
    }
    return myNameToIdMap.get(roomUUID);
  }
}