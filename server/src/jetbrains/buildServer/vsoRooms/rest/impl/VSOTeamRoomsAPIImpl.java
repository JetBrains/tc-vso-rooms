

package jetbrains.buildServer.vsoRooms.rest.impl;

import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOTeamRoomsAPIImpl implements VSOTeamRoomsAPI {

  private static final Logger LOG = Logger.getLogger(VSOTeamRoomsAPIImpl.class);

  @NotNull
  public VSOTeamRoomsAPIConnection createConnection(@NotNull String user, @NotNull String password){
    return new VSOTeamRoomsAPIConnectionImpl(user, password);
  }

  @Nullable
  public String testConnection(@NotNull String account, @NotNull String username, @NotNull String password) {
    final VSOTeamRoomsAPIConnection apiConnection = createConnection(username, password);
    try{
      if(apiConnection.getListOfRooms(account).isEmpty())
        return String.format("Found no team rooms for account %s", account);
    } catch (Exception ex){
      LOG.debug(String.format("Failed to list team rooms of account %s", account), ex);
      return ex.getMessage();
    }
    return null;
  }
}