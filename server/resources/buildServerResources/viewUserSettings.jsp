<%--
  ~ Copyright 2000-2014 JetBrains s.r.o.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%@ include file="/include.jsp" %>

<bs:linkCSS dynamic="${true}">
  /plugins/vso-rooms/css/editSettings.css
</bs:linkCSS>

<jsp:useBean id="showPausedWarning" type="java.lang.Boolean" scope="request"/>
<jsp:useBean id="settingsBean" type="jetbrains.buildServer.vsoRooms.controllers.VSONotificationUserSettingsBean" scope="request"/>

<p class="notificationRulesMessage">
  Allows to monitor events in <a href="http://www.visualstudio.com/en-us/get-started/collaborate-in-the-team-room-vs.aspx">Microsoft Visual Studio Online</a> team rooms.
</p>

<c:choose>
  <c:when test="${showPausedWarning}">
  <div class="attentionComment attentionCommentNotifier">
      Notification rules will not work because Visual Studio Online notifier is disabled.
  </div>
  </c:when>
</c:choose>

<c:choose>
  <c:when test="${settingsBean.wellFormed}">
    <div>
      <p><label class="tableLabel" for="vso-account">Account: </label><span id="vso-account"><c:out value="${settingsBean.account}" /></span></p>
      <p><label class="tableLabel" for="team-room">Team Room Name: </label><span id="team-room"><c:out value="${settingsBean.teamRoomName}" /></span></p>
      <p><label class="tableLabel" for="vso-username">Username: </label><span id="vso-username"><c:out value="${settingsBean.username}" /></span></p>
    </div>
  </c:when>
  <c:otherwise>
    <div class="noRules">
      You have not configured VSO notifications yet.
    </div>
  </c:otherwise>
</c:choose>

