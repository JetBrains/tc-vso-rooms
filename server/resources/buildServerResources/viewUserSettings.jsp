

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