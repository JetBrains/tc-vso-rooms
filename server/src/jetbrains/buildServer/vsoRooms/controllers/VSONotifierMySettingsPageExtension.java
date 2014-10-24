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

import jetbrains.buildServer.notification.NotificationRulesManager;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.users.UserNotFoundException;
import jetbrains.buildServer.vsoRooms.Constants;
import jetbrains.buildServer.vsoRooms.notificator.VSONotificatorConfig;
import jetbrains.buildServer.vsoRooms.notificator.VSONotificatorConfigHolder;
import jetbrains.buildServer.vsoRooms.notificator.VSOUserProperties;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import jetbrains.buildServer.web.util.WebUtil;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotifierMySettingsPageExtension extends SimplePageExtension {

  private final VSONotificatorConfig myConfig;
  private final UserModel myUserModel;
  private final NotificationRulesManager myRulesManager;

  public VSONotifierMySettingsPageExtension(@NotNull WebControllerManager manager,
                                            @NotNull NotificationRulesManager rulesManager,
                                            @NotNull UserModel userModel,
                                            @NotNull PluginDescriptor pluginDescriptor,
                                            @NotNull VSONotificatorConfigHolder configHolder) {
    super(manager);
    myRulesManager = rulesManager;
    myUserModel = userModel;
    myConfig = configHolder.getConfig();

    setPluginName(Constants.NOTIFICATOR_TYPE);
    setIncludeUrl(pluginDescriptor.getPluginResourcesPath("viewUserSettings.jsp"));
    setPlaceId(PlaceId.MY_SETTINGS_NOTIFIER_SECTION);
    register();
  }

  @Override
  public boolean isAvailable(@NotNull HttpServletRequest request) {
    return "/profile.html".equals(WebUtil.getPathWithoutContext(request)) || getPluginName().equals(request.getParameter("notificatorType"));
  }

  @Override
  public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
    User user = SessionUser.getUser(request);
    final String userIdStr = request.getParameter("userId");
    if (userIdStr != null) {
      long userId = Long.parseLong(userIdStr);
      user = myUserModel.findUserById(userId);
      if (user == null) throw new UserNotFoundException(userId, "User with id " + userIdStr + " does not exist");
    }
    model.put("showPausedWarning", myConfig.isPaused());
    boolean showTeamRoomNotConfiguredWarning = false;
    boolean showCredentialsNotConfiguredWarning = false;
    if (myRulesManager.isRulesWithEventsConfigured(user.getId(), getPluginName())) {
      showTeamRoomNotConfiguredWarning = !VSOUserProperties.isTargetTeamRoomConfigured(user);
      showCredentialsNotConfiguredWarning = !VSOUserProperties.isCredentialsConfigured(user);
    }
    model.put("showTeamRoomNotConfiguredWarning", showTeamRoomNotConfiguredWarning);
    model.put("showCredentialsNotConfiguredWarning", showCredentialsNotConfiguredWarning);

    model.put("settingsBean", VSONotificationUserSettingsBean.createFromUserSettings(user));
  }
}
