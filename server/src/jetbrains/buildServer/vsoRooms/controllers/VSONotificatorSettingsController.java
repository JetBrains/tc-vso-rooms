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
import jetbrains.buildServer.vsoRooms.notificator.VSONotificatorConfig;
import jetbrains.buildServer.vsoRooms.notificator.VSONotificatorConfigHolder;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
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

  private final VSONotificatorConfig myConfig;

  public VSONotificatorSettingsController(@NotNull PluginDescriptor pluginDescriptor,
                                          @NotNull WebControllerManager webControllerManager,
                                          @NotNull VSONotificatorConfigHolder configHolder) {
    super(pluginDescriptor, webControllerManager, SETTINGS_BEAN_KEY, "VSO Notifier");
    myConfig = configHolder.getConfig();
  }

  @Override
  protected void registerController(WebControllerManager webControllerManager) {
    webControllerManager.registerController(EDIT_SETTINGS_URL, this);
  }

  @Override
  protected VSONotificatorSettingsBean createSettingsBean(HttpServletRequest httpServletRequest) {
    return new VSONotificatorSettingsBean();
  }

  @Override
  protected ActionErrors validate(VSONotificatorSettingsBean settingsBean) {
    final ActionErrors errors = new ActionErrors();
    if (settingsBean.getAccount() == null || settingsBean.getAccount().trim().length() == 0) {
      errors.addError("emptyAccount", "Account must not be empty");
    }
    if (settingsBean.getUsername() == null || settingsBean.getUsername().trim().length() == 0) {
      errors.addError("emptyUsername", "Username must not be empty");
    }
    if (settingsBean.getPassword() == null || settingsBean.getPassword().trim().length() == 0) {
      errors.addError("emptyPassword", "Password must not be empty");
    }
    return errors;
  }

  @Override
  protected String testSettings(VSONotificatorSettingsBean settingsBean, HttpServletRequest httpServletRequest) {
    return VSOTeamRoomsAPI.testConnection(settingsBean.getAccount(), settingsBean.getUsername(), settingsBean.getPassword());
  }

  @Override
  protected void saveSettings(VSONotificatorSettingsBean settingsBean) {
    myConfig.setAccount(settingsBean.getAccount());
    myConfig.setUser(settingsBean.getUsername());
    myConfig.setPassword(settingsBean.getPassword());
    myConfig.save();
  }
}
