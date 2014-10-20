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

import jetbrains.buildServer.vsoRooms.rest.TeamRoom;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
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

  private final Map<String, Long> myNameToIdMap = new HashMap<String, Long>();
  private final VSOTeamRoomsAPIConnection myAPIConnection;
  private final VSONotificatorConfig myConfig;

  public VSOTeamRoomIdsCache(@NotNull final VSONotificatorConfigHolder configHolder) {
    myConfig = configHolder.getConfig();
    myAPIConnection = VSOTeamRoomsAPI.createConnection(myConfig.getUser(), myConfig.getPassword());
  }

  @Nullable
  public Long getOrResolveRoomId(@NotNull String roomName) {
    final String roomNameToLowerCase = roomName.toLowerCase();
    if(!myNameToIdMap.containsKey(roomNameToLowerCase)){
      synchronized (myNameToIdMap){
        if(!myNameToIdMap.containsKey(roomNameToLowerCase)){
          final String account = myConfig.getAccount();
          LOG.debug(String.format("Team room ID for room name %s is not resolved yet. Calling VSO API to list all Team Rooms for account %s", roomNameToLowerCase, account));
          try {
            for (TeamRoom teamRoom : myAPIConnection.getListOfRooms(account)) {
              final String resolvedRoomName = teamRoom.getName().toLowerCase();
              if(!myNameToIdMap.containsKey(resolvedRoomName)){
                myNameToIdMap.put(resolvedRoomName, teamRoom.getId());
              }
            }
          } catch (Exception e) {
            LOG.warn("Failed to list team rooms for account " + account, e);
          }
        }
      }
    }
    return myNameToIdMap.get(roomNameToLowerCase);
  }
}
