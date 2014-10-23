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

package jetbrains.buildServer.vsoRooms.notificator;

import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOMessageSender {

  private static final Logger LOG = Logger.getLogger(VSOMessageSender.class);

  private final VSOTeamRoomIdsCache myTeamRoomIdsCache = new VSOTeamRoomIdsCache();

  public void sendMessageOnBehalfOfUsers(@NotNull final Set<SUser> users, @NotNull final String message) {
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

      final VSOTeamRoomsAPIConnection connection = VSOTeamRoomsAPI.createConnection(username, password);

      final Long roomId = myTeamRoomIdsCache.getOrResolveRoomId(account, teamRoomName, connection);
      if(roomId == null){
        LOG.debug(String.format("Skip sending notification on behalf of %s. Failed to resolve target team room ID.", user.getDescriptiveName()));
        continue;
      }

      try{
        connection.sendMessageToRoom(account, roomId, message);
      } catch (Exception ex){
        LOG.warn("Failed to send message to the team room with ID " + roomId, ex);
      }
    }
  }
}
