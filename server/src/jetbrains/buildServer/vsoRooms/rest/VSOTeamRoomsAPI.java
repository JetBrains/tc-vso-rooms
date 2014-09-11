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

package jetbrains.buildServer.vsoRooms.rest;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOTeamRoomsAPI {

  private static final Logger LOG = Logger.getLogger(VSOTeamRoomsAPI.class);

  @NotNull
  public static VSOTeamRoomsAPIConnection connect(@NotNull String user, @NotNull String password){
    return new VSOTeamRoomsAPIConnection(user, password);
  }

  @Nullable
  public static String testConnection(@NotNull String account, @NotNull String username, @NotNull String password) {
    final VSOTeamRoomsAPIConnection apiConnection = connect(username, password);
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
