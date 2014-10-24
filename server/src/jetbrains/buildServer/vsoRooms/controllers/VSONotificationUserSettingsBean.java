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

package jetbrains.buildServer.vsoRooms.controllers;

import jetbrains.buildServer.users.User;
import jetbrains.buildServer.vsoRooms.notificator.VSOUserProperties;
import org.jetbrains.annotations.NotNull;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificationUserSettingsBean {
  private String myAccount;
  private String myUsername;
  private String myTeamRoomName;
  private String myEncryptedPassword;

  public VSONotificationUserSettingsBean(@NotNull String account, @NotNull String teamRoomName, @NotNull String username, @NotNull String encryptedPassword) {
    myAccount = account;
    myTeamRoomName = teamRoomName;
    myUsername = username;
    myEncryptedPassword = encryptedPassword;
  }

  @NotNull
  public static VSONotificationUserSettingsBean forUser(@NotNull User user) {
    final String account = VSOUserProperties.getAccount(user);
    final String teamRoomName = VSOUserProperties.getTeamRoomName(user);
    final String username = VSOUserProperties.getUsername(user);
    final String password = VSOUserProperties.getPassword(user);
    return new VSONotificationUserSettingsBean(account, teamRoomName, username, password);
  }

  public String getAccount() {
    return myAccount;
  }

  public String getUsername() {
    return myUsername;
  }

  public String getTeamRoomName() {
    return myTeamRoomName;
  }

  public String getEncryptedPassword() {
    return myEncryptedPassword;
  }
}
