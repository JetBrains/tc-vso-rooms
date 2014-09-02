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

  public VSONotificatorSettingsController(@NotNull PluginDescriptor pluginDescriptor,
                                          @NotNull WebControllerManager webControllerManager) {
    super(pluginDescriptor, webControllerManager, SETTINGS_BEAN_KEY, "Visual Studio Online Notifier");

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
  protected ActionErrors validate(VSONotificatorSettingsBean vsoNotificatorSettingsBean) {
    return null;
  }

  @Override
  protected String testSettings(VSONotificatorSettingsBean vsoNotificatorSettingsBean, HttpServletRequest httpServletRequest) {
    return null;
  }

  @Override
  protected void saveSettings(VSONotificatorSettingsBean vsoNotificatorSettingsBean) {

  }
}
