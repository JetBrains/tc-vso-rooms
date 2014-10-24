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

import jetbrains.buildServer.Used;
import jetbrains.buildServer.controllers.RememberState;
import jetbrains.buildServer.serverSide.crypt.RSACipher;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.vsoRooms.notificator.VSOUserProperties;
import org.jetbrains.annotations.NotNull;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificationUserSettingsBean extends RememberState {

  private String myAccount;
  private String myTeamRoomName;
  private String myUsername;
  private String myPassword;

  public VSONotificationUserSettingsBean() {
  }

  public VSONotificationUserSettingsBean(@NotNull String account, @NotNull String teamRoomName, @NotNull String username, @NotNull String password) {
    myAccount = account;
    myTeamRoomName = teamRoomName;
    myUsername = username;
    myPassword = password;
    rememberState();
  }

  @NotNull
  public static VSONotificationUserSettingsBean createFromUserSettings(@NotNull User user) {
    final String account = StringUtil.emptyIfNull(VSOUserProperties.getAccount(user));
    final String teamRoomName = StringUtil.emptyIfNull(VSOUserProperties.getTeamRoomName(user));
    final String username = StringUtil.emptyIfNull(VSOUserProperties.getUsername(user));
    final String password = StringUtil.emptyIfNull(VSOUserProperties.getPassword(user));
    return new VSONotificationUserSettingsBean(account, teamRoomName, username, password);
  }

  public static void saveAsUserSettings(@NotNull VSONotificationUserSettingsBean settings, @NotNull SUser user) {
    VSOUserProperties.setAccount(user, settings.getAccount());
    VSOUserProperties.setTeamRoomName(user, settings.getTeamRoomName());
    VSOUserProperties.setUsername(user, settings.getUsername());
    VSOUserProperties.setPassword(user, settings.getPassword());
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

  public String getHexEncodedPublicKey() {
    return RSACipher.getHexEncodedPublicKey();
  }

  public String getEncryptedPassword() {
    return StringUtil.isEmpty(myPassword) ? "" : RSACipher.encryptDataForWeb(myPassword);
  }

  @Used("jsp")
  public void setEncryptedPassword(final String encrypted) {
    myPassword = RSACipher.decryptWebRequestData(encrypted);
  }

  public String getPassword() {
    return myPassword;
  }

  public void setAccount(String account) {
    myAccount = account;
  }

  public void setTeamRoomName(String teamRoomName) {
    myTeamRoomName = teamRoomName;
  }

  public void setUsername(String username) {
    myUsername = username;
  }

  public void setPassword(String password) {
    myPassword = password;
  }

  public boolean isWellFormed() {
    return StringUtil.isNotEmpty(myAccount) && StringUtil.isNotEmpty(myTeamRoomName) && StringUtil.isNotEmpty(myUsername) && StringUtil.isNotEmpty(myPassword);
  }
}
