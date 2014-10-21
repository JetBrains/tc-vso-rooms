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
import jetbrains.buildServer.Build;
import jetbrains.buildServer.notification.FreeMarkerHelper;
import jetbrains.buildServer.notification.NotificatorAdapter;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.notification.TemplateMessageBuilder;
import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import jetbrains.buildServer.responsibility.TestNameResponsibilityEntry;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.mute.MuteInfo;
import jetbrains.buildServer.serverSide.problems.BuildProblemInfo;
import jetbrains.buildServer.tests.TestName;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.vcs.VcsRoot;
import jetbrains.buildServer.vsoRooms.Constants;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificator extends NotificatorAdapter {

  private static final Logger LOG = Logger.getLogger(VSONotificator.class);

  private static final String BUILD_STARTED_EVENT = "build_started";
  private static final String BUILD_FAILED_EVENT = "build_failed";
  private static final String BUILD_SUCCESSFUL_EVENT = "build_successful";
  private static final String BUILD_FAILED_TO_START_EVENT = "build_failed_to_start";
  private static final String LABELING_FAILED_EVENT = "labeling_failed";
  private static final String BUILD_FAILING_EVENT = "build_failing";
  private static final String BUILD_HANGING_EVENT = "build_probably_hanging";
  private static final String BUILD_TYPE_RESPONSIBILITY_ASSIGNED = "build_type_responsibility_assigned";
  private static final String BUILD_TYPE_RESPONSIBILITY_CHANGED = "build_type_responsibility_changed";
  private static final String TEST_RESPONSIBILITY_ASSIGNED = "test_responsibility_assigned";
  private static final String TEST_RESPONSIBILITY_CHANGED = "test_responsibility_changed";
  private static final String MULTIPLE_TEST_RESPONSIBILITY_ASSIGNED = "multiple_test_responsibility_assigned";
  private static final String MULTIPLE_TEST_RESPONSIBILITY_CHANGED = "multiple_test_responsibility_changed";
  private static final String BUILD_PROBLEM_RESPONSIBILITY_ASSIGNED = "build_problem_responsibility_assigned";
  private static final String BUILD_PROBLEM_RESPONSIBILITY_CHANGED = "build_problem_responsibility_changed";
  private static final String TEST_MUTED_EVENT = "tests_muted";
  private static final String TEST_UNMUTED_EVENT = "tests_unmuted";
  private static final String BUILD_PROBLEMS_MUTED_EVENT = "build_problems_muted";
  private static final String BUILD_PROBLEMS_UNMUTED_EVENT = "build_problems_unmuted";

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
  public void notifyBuildStarted(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildStartedMap(build, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_STARTED_EVENT);
  }

  @Override
  public void notifyBuildSuccessful(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildSuccessfulMap(build, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_SUCCESSFUL_EVENT);
  }

  @Override
  public void notifyBuildFailedToStart(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildFailedToStartMap(build, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_FAILED_TO_START_EVENT);
  }

  @Override
  public void notifyBuildFailed(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildFailedMap(build, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_FAILED_EVENT);
  }

  @Override
  public void notifyLabelingFailed(@NotNull Build build, @NotNull VcsRoot vcsRoot, @NotNull Throwable exception, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getLabelingFailedMap((SBuild) build, vcsRoot, exception, users);
    sendNotification(getTargetRoomIds(users), root, LABELING_FAILED_EVENT);
  }

  @Override
  public void notifyBuildFailing(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildFailingMap(build, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_FAILING_EVENT);
  }

  @Override
  public void notifyBuildProbablyHanging(@NotNull SRunningBuild build, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildProbablyHangingMap(build, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_HANGING_EVENT);
  }

  @Override
  public void notifyResponsibleAssigned(@NotNull SBuildType buildType, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildTypeResponsibilityAssignedMap(buildType, users);
    final ResponsibilityEntry responsibility = (ResponsibilityEntry) root.get("responsibility");
    root.put("responsibleUser", getUserName(responsibility.getResponsibleUser()));
    root.put("reporterUser", getUserName(responsibility.getReporterUser()));
    sendNotification(getTargetRoomIds(users), root, BUILD_TYPE_RESPONSIBILITY_ASSIGNED);
  }

  @Override
  public void notifyResponsibleChanged(@NotNull SBuildType buildType, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildTypeResponsibilityChangedMap(buildType, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_TYPE_RESPONSIBILITY_CHANGED);
  }

  @Override
  public void notifyResponsibleAssigned(@Nullable TestNameResponsibilityEntry oldValue, @NotNull TestNameResponsibilityEntry newValue, @NotNull SProject project, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getTestResponsibilityAssignedMap(newValue, oldValue, project, users);
    root.put("responsibleUser", getUserName(newValue.getResponsibleUser()));
    root.put("reporterUser", getUserName(newValue.getReporterUser()));
    sendNotification(getTargetRoomIds(users), root, TEST_RESPONSIBILITY_ASSIGNED);
  }

  @Override
  public void notifyResponsibleChanged(@Nullable TestNameResponsibilityEntry oldValue, @NotNull TestNameResponsibilityEntry newValue, @NotNull SProject project, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getTestResponsibilityChangedMap(newValue, oldValue, project, users);
    sendNotification(getTargetRoomIds(users), root, TEST_RESPONSIBILITY_CHANGED);
  }

  @Override
  public void notifyResponsibleAssigned(@NotNull Collection<TestName> testNames, @NotNull ResponsibilityEntry entry, @NotNull SProject project, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getTestResponsibilityAssignedMap(testNames, entry, project, users);
    root.put("responsibleUser", getUserName(entry.getResponsibleUser()));
    root.put("reporterUser", getUserName(entry.getReporterUser()));
    sendNotification(getTargetRoomIds(users), root, MULTIPLE_TEST_RESPONSIBILITY_ASSIGNED);
  }

  @Override
  public void notifyResponsibleChanged(@NotNull Collection<TestName> testNames, @NotNull ResponsibilityEntry entry, @NotNull SProject project, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getTestResponsibilityChangedMap(testNames, entry, project, users);
    sendNotification(getTargetRoomIds(users), root, MULTIPLE_TEST_RESPONSIBILITY_CHANGED);
  }

  @Override
  public void notifyBuildProblemResponsibleAssigned(@NotNull Collection<BuildProblemInfo> buildProblems, @NotNull ResponsibilityEntry entry, @NotNull SProject project, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildProblemsResponsibilityAssignedMap(buildProblems, entry, project, users);
    root.put("responsibleUser", getUserName(entry.getResponsibleUser()));
    root.put("reporterUser", getUserName(entry.getReporterUser()));
    sendNotification(getTargetRoomIds(users), root, BUILD_PROBLEM_RESPONSIBILITY_ASSIGNED);
  }

  @Override
  public void notifyBuildProblemResponsibleChanged(@NotNull Collection<BuildProblemInfo> buildProblems, @NotNull ResponsibilityEntry entry, @NotNull SProject project, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildProblemsResponsibilityChangedMap(buildProblems, entry, project, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_PROBLEM_RESPONSIBILITY_CHANGED);
  }

  @Override
  public void notifyTestsMuted(@NotNull Collection<STest> tests, @NotNull MuteInfo muteInfo, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getTestsMutedMap(tests, muteInfo, users);
    root.put("mutingUser", getUserName(muteInfo.getMutingUser()));
    sendNotification(getTargetRoomIds(users), root, TEST_MUTED_EVENT);
  }

  @Override
  public void notifyTestsUnmuted(@NotNull Collection<STest> tests, @NotNull MuteInfo muteInfo, @Nullable SUser user, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getTestsUnmutedMap(tests, muteInfo, user, users);
    sendNotification(getTargetRoomIds(users), root, TEST_UNMUTED_EVENT);
  }

  @Override
  public void notifyBuildProblemsMuted(@NotNull Collection<BuildProblemInfo> buildProblems, @NotNull MuteInfo muteInfo, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildProblemsMutedMap(buildProblems, muteInfo, users);
    root.put("mutingUser", getUserName(muteInfo.getMutingUser()));
    sendNotification(getTargetRoomIds(users), root, BUILD_PROBLEMS_MUTED_EVENT);
  }

  @Override
  public void notifyBuildProblemsUnmuted(@NotNull Collection<BuildProblemInfo> buildProblems, @NotNull MuteInfo muteInfo, @Nullable SUser user, @NotNull Set<SUser> users) {
    final Map<String, Object> root = myMessageBuilder.getBuildProblemsUnmutedMap(buildProblems, muteInfo, user, users);
    sendNotification(getTargetRoomIds(users), root, BUILD_PROBLEMS_UNMUTED_EVENT);
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

    final VSOTeamRoomsAPIConnection connection = VSOTeamRoomsAPI.createConnection(myConfig.getUser(), myConfig.getPassword());
    for (Long roomId : roomIds){
      try{
        connection.sendMessageToRoom(myConfig.getAccount(), roomId, message);
      } catch (Exception ex){
        LOG.warn("Failed to send message to the team room with ID " + roomId, ex);
      }
    }
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

  private String getUserName(User user) {
    final String vsoUserName = UserPropertiesUtil.getVSOUserName(user);
    if(vsoUserName != null){
      return "@" + vsoUserName;
    }
    return user.getDescriptiveName();
  }
}
