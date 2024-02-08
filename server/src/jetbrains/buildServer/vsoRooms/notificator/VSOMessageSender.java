

package jetbrains.buildServer.vsoRooms.notificator;

import com.intellij.openapi.util.Pair;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOMessageSender {

  private static final Logger LOG = Logger.getLogger(VSOMessageSender.class);

  private final VSOTeamRoomsAPI myAPI;
  private final VSOTeamRoomIdsCache myTeamRoomIdsCache = new VSOTeamRoomIdsCache();

  public VSOMessageSender(@NotNull final VSOTeamRoomsAPI api) {
    myAPI = api;
  }

  public void sendMessageOnBehalfOfUsers(@NotNull final Set<SUser> users, @NotNull final String message) {
    final Map<Pair<String, Long>, VSOTeamRoomsAPIConnection> connectionsToProcess = new HashMap<Pair<String, Long>, VSOTeamRoomsAPIConnection>();

    for(SUser user : users){
      final String account = VSOUserProperties.getAccount(user);
      final String teamRoomName = VSOUserProperties.getTeamRoomName(user);
      if(account == null || teamRoomName == null){
        LOG.debug(String.format("Skip sending notification on behalf of %s. Target team room wasn't configured properly.", user.getDescriptiveName()));
        continue;
      }
      final String username = VSOUserProperties.getUsername(user);
      final String password = VSOUserProperties.getPassword(user);
      if(username == null || password == null){
        LOG.debug(String.format("Skip sending notification on behalf of %s. Required credentials were not configured properly.", user.getDescriptiveName()));
        continue;
      }
      final VSOTeamRoomsAPIConnection connection = myAPI.createConnection(username, password);
      final Long roomId = myTeamRoomIdsCache.getOrResolveRoomId(account, teamRoomName, connection);
      if(roomId == null){
        LOG.debug(String.format("Skip sending notification on behalf of %s. Failed to resolve target team room ID.", user.getDescriptiveName()));
        continue;
      }
      connectionsToProcess.put(new Pair<String, Long>(account.toLowerCase(), roomId), connection);
    }

    for(Pair<String, Long> teamRoomUUID : connectionsToProcess.keySet()){
      final VSOTeamRoomsAPIConnection apiConnection = connectionsToProcess.get(teamRoomUUID);
      try{
        apiConnection.sendMessageToRoom(teamRoomUUID.first, teamRoomUUID.second, message);
      } catch (Exception ex){
        LOG.warn(String.format("Failed to send message to the team room (%s : %d) ", teamRoomUUID.first, teamRoomUUID.second), ex);
      }
    }
  }
}