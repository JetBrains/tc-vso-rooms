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

import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.vsoRooms.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOUserProperties {

  private static final String VSO_ACCOUNT_USER_PROPERTY_NAME = "vso-account";
  private static final String VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME = "vso-team-room-name";
  private static final String VSO_USERNAME_USER_PROPERTY_NAME = "vso-username";
  private static final String VSO_PASSWORD_USER_PROPERTY_NAME = "vso-password";
  private static final String VSO_USER_DISPLAY_NAME_USER_PROPERTY_NAME = "vso-user-display-name";

  public final static List<UserPropertyInfo> USER_PROPERTIES = new ArrayList<UserPropertyInfo>();
  static {
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_ACCOUNT_USER_PROPERTY_NAME, "Account"));
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME, "Team room name"));
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME, "Username"));
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME, "Password"));
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME, "User display name"));
  }

  private static final NotificatorPropertyKey ourVSOAccountUserPropKey = new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_ACCOUNT_USER_PROPERTY_NAME);
  private static final NotificatorPropertyKey ourVSOTeamRoomNameUserPropKey = new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME);
  private static final NotificatorPropertyKey ourUsernameUserPropKey = new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_USERNAME_USER_PROPERTY_NAME);
  private static final NotificatorPropertyKey ourPasswordUserPropKey = new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_PASSWORD_USER_PROPERTY_NAME);
  private static final NotificatorPropertyKey ourUserDisplayNameUserPropKey = new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_USER_DISPLAY_NAME_USER_PROPERTY_NAME);

  @Nullable
  public static String getAccount(@NotNull final User tcUser){
    return tcUser.getPropertyValue(ourVSOAccountUserPropKey);
  }

  @Nullable
  public static String getTeamRoomName(@NotNull final User tcUser) {
    return tcUser.getPropertyValue(ourVSOTeamRoomNameUserPropKey);
  }

  @Nullable
  public static String getUsername(@NotNull final User tcUser) {
    return tcUser.getPropertyValue(ourUsernameUserPropKey);
  }

  @Nullable
  public static String getPassword(@NotNull final User tcUser) {
    return tcUser.getPropertyValue(ourPasswordUserPropKey);
  }

  @Nullable
  public static String getUserDisplayName(@NotNull final User tcUser){
    return tcUser.getPropertyValue(ourUserDisplayNameUserPropKey);
  }

  public static boolean isTargetTeamRoomConfigured(@NotNull User user) {
    final String vsoAccount = user.getPropertyValue(new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_ACCOUNT_USER_PROPERTY_NAME));
    final String teamRoomName = user.getPropertyValue(new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME));
    return vsoAccount != null && !vsoAccount.isEmpty() && teamRoomName != null && !teamRoomName.isEmpty();
  }
}
