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

import freemarker.template.TemplateException;
import jetbrains.buildServer.notification.FreeMarkerHelper;
import jetbrains.buildServer.notification.NotificatorAdapter;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.notification.TemplateMessageBuilder;
import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.vsoRooms.Constants;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificator extends NotificatorAdapter {

  private static final Logger LOG = Logger.getLogger(VSONotificator.class);
  private static final String BUILD_FAILED_EVENT = "build_failed";
  private static final String BUILD_TYPE_RESPONSIBILITY_ASSIGNED_TO_ME = "build_type_responsibility_assigned_to_me";

  private final static List<UserPropertyInfo> USER_PROPERTIES = new ArrayList<UserPropertyInfo>();
  static {
    USER_PROPERTIES.add(new UserPropertyInfo(Constants.VSO_TEAM_ROOM_NAME_USER_PROPERTY_NAME, "Team room name"));
    USER_PROPERTIES.add(new UserPropertyInfo(Constants.VSO_USER_NAME_USER_PROPERTY_NAME, "VSO Account"));
  }

  private final TemplateMessageBuilder myMessageBuilder;
  private final VSONotificatorConfig myConfig;
  private final VSOTeamRoomIdsCache myTeamRoomIdsCache;

  public VSONotificator(@NotNull final NotificatorRegistry registry,
                        @NotNull final TemplateMessageBuilder builder,
                        @NotNull final VSONotificatorConfigHolder configHolder,
                        @NotNull final VSOTeamRoomIdsCache teamRoomIdsCache) throws IOException {
    myMessageBuilder = builder;
    myTeamRoomIdsCache = teamRoomIdsCache;
    myConfig = configHolder.getConfig();
    registry.register(this, USER_PROPERTIES);
  }

  @NotNull
  public String getNotificatorType() {
    return Constants.NOTIFICATOR_TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "VSO Notifier";
  }

  @Override
  public void notifyBuildFailed(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildFailedMap(build, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_FAILED_EVENT);
  }

  @Override
  public void notifyResponsibleAssigned(@NotNull SBuildType buildType, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildTypeResponsibilityAssignedMap(buildType, users);
    final ResponsibilityEntry responsibility = (ResponsibilityEntry) root.get("responsibility");
    root.put("responsibleUser", getUserName(responsibility.getResponsibleUser()));
    root.put("reporterUser", getUserName(responsibility.getReporterUser()));
    sendNotification(getTargetRoomIds(users), root, BUILD_TYPE_RESPONSIBILITY_ASSIGNED_TO_ME);
  }

  private String getUserName(User user) {
    final String vsoUserName = UserPropertiesUtil.getVSOUserName(user);
    if(vsoUserName != null){
      return "@" + vsoUserName;
    }
    return user.getDescriptiveName();
  }

  private void sendNotification(Set<Long> roomIds, Map<String, Object> root, String event) {
    if(myConfig.isPaused()){
      LOG.debug("Skip sending message. Notifier is disabled.");
      return;
    }

    if(roomIds.isEmpty()){
      LOG.debug("Skip sending message. No target team rooms found.");
      return;
    }

    final Map<String, String> map;
    try {
      map = FreeMarkerHelper.processTemplate(myConfig.getTemplate(event), root);
    } catch (IOException e) {
      LOG.warn(String.format("Failed to create message for event %s", event), e);
      return;
    } catch (TemplateException e) {
      LOG.warn(String.format("Failed to create message for event %s", event), e);
      return;
    }
    final String message = map.get("message");

    final VSOTeamRoomsAPIConnection connection = getApiConnection();
    for (Long roomId : roomIds){
      try{
        connection.sendMessageToRoom(myConfig.getAccount(), roomId, message);
      } catch (Exception ex){
        LOG.warn("Failed to send message to the team room with ID " + roomId, ex);
      }
    }
  }

  private VSOTeamRoomsAPIConnection getApiConnection() {
    return VSOTeamRoomsAPI.createConnection(myConfig.getUser(), myConfig.getPassword());
  }

  private Set<Long> getTargetRoomIds(Set<SUser> users) {
    final Set<Long> roomIds = new HashSet<Long>();
    for (SUser user : users){
      final String roomName = UserPropertiesUtil.getVSOTeamRoomName(user);
      if (roomName != null) {
        final Long roomId = myTeamRoomIdsCache.getOrResolveRoomId(roomName);
        if(roomId != null){
          roomIds.add(roomId);
        }
      }
    }
    return roomIds;
  }
}
