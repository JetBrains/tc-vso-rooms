

package jetbrains.buildServer.vsoRooms.controllers;

import jetbrains.buildServer.controllers.*;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.users.UserNotFoundException;
import jetbrains.buildServer.vsoRooms.Constants;
import jetbrains.buildServer.vsoRooms.rest.impl.VSOTeamRoomsAPIImpl;
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

    web.registerController("/vso/userSettings.html", new EditUserSettingsController());
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

    model.put("settingsBean", VSONotificationUserSettingsBean.createFromUserSettings(user));
  }

  private class EditUserSettingsController extends BaseFormXmlController {
    @Override
    protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
      return null;
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
      if (PublicKeyUtil.isPublicKeyExpired(request)) {
        PublicKeyUtil.writePublicKeyExpiredError(xmlResponse);
        return;
      }

      final VSONotificationUserSettingsBean bean = new VSONotificationUserSettingsBean();
      FormUtil.bindFromRequest(request, bean);

      if (isStoreInSessionRequest(request)) {
        XmlResponseUtil.writeFormModifiedIfNeeded(xmlResponse, bean);
        return;
      }

      ActionErrors errors = validate(bean);
      if (errors.hasNoErrors()) {
        if (isTestConnectionRequest(request)) {
          String testResult = testSettings(bean);
          XmlResponseUtil.writeTestResult(xmlResponse, testResult);
        }
        else {

          SUser user = SessionUser.getUser(request);
          final String userIdStr = request.getParameter("userId");
          if (userIdStr != null) {
            long userId = Long.parseLong(userIdStr);
            user = myUserModel.findUserById(userId);
            if (user == null) throw new UserNotFoundException(userId, "User with id " + userIdStr + " does not exist");
          }

          VSONotificationUserSettingsBean.saveAsUserSettings(bean, user);
          FormUtil.removeAllFromSession(request.getSession(), bean.getClass());
          writeRedirect(xmlResponse, request.getContextPath() + "/profile.html?tab=userNotifications&notificatorType=" + Constants.NOTIFICATOR_TYPE);
        }
      }

      writeErrors(xmlResponse, errors);
    }

    private boolean isStoreInSessionRequest(final HttpServletRequest request) {
      return "storeInSession".equals(request.getParameter("submitSettings"));
    }

    private boolean isTestConnectionRequest(final HttpServletRequest request) {
      return "testConnection".equals(request.getParameter("submitSettings"));
    }

    private ActionErrors validate(VSONotificationUserSettingsBean settingsBean) {
      final ActionErrors errors = new ActionErrors();
      if (settingsBean.getAccount() == null || settingsBean.getAccount().trim().length() == 0) {
        errors.addError("emptyAccount", "Account must not be empty");
      }
      if (settingsBean.getTeamRoomName() == null || settingsBean.getTeamRoomName().trim().length() == 0) {
        errors.addError("emptyTeamRoomName", "Team room name must not be empty");
      }
      if (settingsBean.getUsername() == null || settingsBean.getUsername().trim().length() == 0) {
        errors.addError("emptyUsername", "Username must not be empty");
      }
      if (settingsBean.getPassword() == null || settingsBean.getPassword().trim().length() == 0) {
        errors.addError("emptyPassword", "Password must not be empty");
      }
      return errors;
    }

    private String testSettings(@NotNull VSONotificationUserSettingsBean settingsBean) {
      return new VSOTeamRoomsAPIImpl().testConnection(settingsBean.getAccount(), settingsBean.getUsername(), settingsBean.getPassword());
    }
  }
}