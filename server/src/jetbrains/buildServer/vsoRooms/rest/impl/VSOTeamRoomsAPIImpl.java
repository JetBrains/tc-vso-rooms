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
