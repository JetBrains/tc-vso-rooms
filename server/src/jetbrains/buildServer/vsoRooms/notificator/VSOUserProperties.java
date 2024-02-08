

package jetbrains.buildServer.vsoRooms.notificator;

import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.SUser;
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
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME, "Team Room Name"));
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_USERNAME_USER_PROPERTY_NAME, "Username"));
    USER_PROPERTIES.add(new UserPropertyInfo(VSO_PASSWORD_USER_PROPERTY_NAME, "Password"));
    //USER_PROPERTIES.add(new UserPropertyInfo(VSO_USER_DISPLAY_NAME_USER_PROPERTY_NAME, " My User Display Name (optional)"));
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

  public static void setAccount(@NotNull final SUser tcUser, @NotNull final String account) {
    tcUser.setUserProperty(ourVSOAccountUserPropKey, account);
  }

  @Nullable
  public static String getTeamRoomName(@NotNull final User tcUser) {
    return tcUser.getPropertyValue(ourVSOTeamRoomNameUserPropKey);
  }

  public static void setTeamRoomName(@NotNull final SUser tcUser, @NotNull final String teamRoomName) {
    tcUser.setUserProperty(ourVSOTeamRoomNameUserPropKey, teamRoomName);
  }

  @Nullable
  public static String getUsername(@NotNull final User tcUser) {
    return tcUser.getPropertyValue(ourUsernameUserPropKey);
  }

  public static void setUsername(@NotNull final SUser tcUser, @NotNull final String username) {
    tcUser.setUserProperty(ourUsernameUserPropKey, username);
  }

  @Nullable
  public static String getPassword(@NotNull final User tcUser) {
    return tcUser.getPropertyValue(ourPasswordUserPropKey);
  }

  public static void setPassword(@NotNull final SUser tcUser, @NotNull final String password) {
    tcUser.setUserProperty(ourPasswordUserPropKey, password);
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

  public static boolean isCredentialsConfigured(@NotNull User user) {
    final String userName = user.getPropertyValue(new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_USERNAME_USER_PROPERTY_NAME));
    final String password = user.getPropertyValue(new NotificatorPropertyKey(Constants.NOTIFICATOR_TYPE, VSO_PASSWORD_USER_PROPERTY_NAME));
    return userName != null && !userName.isEmpty() && password != null && !password.isEmpty();
  }
}