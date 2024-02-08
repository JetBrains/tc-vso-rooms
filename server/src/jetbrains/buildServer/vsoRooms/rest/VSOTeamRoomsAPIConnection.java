

package jetbrains.buildServer.vsoRooms.rest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author: Evgeniy.Koshkin
 */
public interface VSOTeamRoomsAPIConnection {
  @Nullable
  TeamRoomMessage sendMessageToRoom(@NotNull String account, @NotNull Long roomId, @NotNull String messageContent);

  @NotNull
  Collection<TeamRoom> getListOfRooms(@NotNull String account);
}