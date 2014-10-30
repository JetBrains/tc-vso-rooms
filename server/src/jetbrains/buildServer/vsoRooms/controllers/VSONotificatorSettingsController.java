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

import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.admin.NotifierSettingsTab;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.vsoRooms.notificator.VSONotificatorConfig;
import jetbrains.buildServer.vsoRooms.notificator.VSONotificatorConfigHolder;
import jetbrains.buildServer.vsoRooms.notificator.VSOUserProperties;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificatorSettingsController extends NotifierSettingsTab<VSONotificatorSettingsBean> {

  private static final String SETTINGS_BEAN_KEY = "vsoRoomsSettings";
  private static final String EDIT_SETTINGS_URL = "/vso/notificatorSettings.html";

  @NotNull private final VSONotificatorConfig myConfig;
  @NotNull private final UserModel myUsers;

  public VSONotificatorSettingsController(@NotNull PluginDescriptor pluginDescriptor,
                                          @NotNull WebControllerManager webControllerManager,
                                          @NotNull VSONotificatorConfigHolder configHolder,
                                          @NotNull UserModel users) {
    super(pluginDescriptor, webControllerManager, SETTINGS_BEAN_KEY, "VS Online Notifier");
    myUsers = users;
    myConfig = configHolder.getConfig();
    registerDisableActions();
  }

  @Override
  protected void registerController(WebControllerManager webControllerManager) {
    webControllerManager.registerController(EDIT_SETTINGS_URL, this);
  }

  @Override
  protected VSONotificatorSettingsBean createSettingsBean(HttpServletRequest httpServletRequest) {
    return new VSONotificatorSettingsBean(myConfig.isPaused(), getNumberOfAffectedUsers());
  }

  @Override
  protected ActionErrors validate(VSONotificatorSettingsBean settingsBean) {
    return new ActionErrors();
  }

  @Override
  protected String testSettings(VSONotificatorSettingsBean settingsBean, HttpServletRequest httpServletRequest) {
    return null;
  }

  @Override
  protected void saveSettings(VSONotificatorSettingsBean settingsBean) {
  }

  private void registerDisableActions() {
    addAction("enable", new Action() {
      public void run(HttpServletRequest request) throws Exception {
        myConfig.setPaused(false);
        myConfig.save();
      }
    });
    addAction("disable", new Action() {
      public void run(HttpServletRequest request) throws Exception {
        myConfig.setPaused(true);
        myConfig.save();
      }
    });
  }

  private int getNumberOfAffectedUsers() {
    int result = 0;
    for(SUser user : myUsers.getAllUsers().getUsers()){
      if(VSOUserProperties.isTargetTeamRoomConfigured(user) && VSOUserProperties.isCredentialsConfigured(user))
        result++;
    }
    return result;
  }
}
