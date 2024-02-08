

package jetbrains.buildServer.vsoRooms.rest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Evgeniy.Koshkin
 */
public interface VSOTeamRoomsAPI {
  @NotNull
  VSOTeamRoomsAPIConnection createConnection(@NotNull String user, @NotNull String password);

  @Nullable
  String testConnection(@NotNull String account, @NotNull String username, @NotNull String password);
}