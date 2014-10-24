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

import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.users.UserNotFoundException;
import jetbrains.buildServer.vsoRooms.Constants;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import jetbrains.buildServer.web.util.WebUtil;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotifierSettingsPageExtension extends SimplePageExtension {

  private static final Logger LOG = Logger.getLogger(VSONotifierSettingsPageExtension.class);

  private final UserModel myUserModel;

  public VSONotifierSettingsPageExtension(@NotNull WebControllerManager web,
                                          @NotNull UserModel userModel,
                                          @NotNull PluginDescriptor pluginDescriptor) {
    super(web);
    myUserModel = userModel;

    setPluginName(Constants.NOTIFICATOR_TYPE);
    setIncludeUrl(pluginDescriptor.getPluginResourcesPath("editUserSettings.jsp"));
    setPlaceId(PlaceId.NOTIFIER_SETTINGS_FRAGMENT);
    register();

    web.registerController("/vso/userSettings.html", new BaseFormXmlController() {
      @Override
      protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        return null;
      }

      @Override
      protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
        LOG.debug("called");
      }
    });
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

    model.put("settingsBean", VSONotificationUserSettingsBean.forUser(user));
  }
}
