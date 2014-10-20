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

import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.vsoRooms.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Evgeniy.Koshkin
 */
public class UserPropertiesUtil {
  private static final NotificatorPropertyKey ourVCOTeamRoomNameUserPropKey = new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, Constants.VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME);
  private static final NotificatorPropertyKey ourVCOAccountNameUserPropKey = new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, Constants.VSO_USER_NAME_USER_PROPERTY_NAME);

  @Nullable
  public static String getVSOUserName(@NotNull final User tcUser){
    return tcUser.getPropertyValue(ourVCOAccountNameUserPropKey);
  }

  @Nullable
  public static String getVSOTeamRoomName(@NotNull final User tcUser) {
    return tcUser.getPropertyValue(ourVCOTeamRoomNameUserPropKey);
  }
}
