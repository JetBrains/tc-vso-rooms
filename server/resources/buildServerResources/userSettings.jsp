<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%--
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

<jsp:useBean id="showTeamRoomNotConfiguredWarning" type="java.lang.Boolean" scope="request"/>
<jsp:useBean id="showCredentialsNotConfiguredWarning" type="java.lang.Boolean" scope="request"/>
<jsp:useBean id="showPausedWarning" type="java.lang.Boolean" scope="request"/>

<c:choose>
  <c:when test="${showPausedWarning}">
  <div class="attentionComment attentionCommentNotifier">
      Notification rules will not work because Visual Studio Online notifier is disabled.
  </div>
  </c:when>
  <c:when test="${showTeamRoomNotConfiguredWarning}">
    <div class="attentionComment attentionCommentNotifier">
      Notification rules will not work until you set up target Visual Studio Online Account and Team Room name.
     </div>
  </c:when>
  <c:when test="${showCredentialsNotConfiguredWarning}">
    <div class="attentionComment attentionCommentNotifier">
      Notification rules will not work until you set up Username and Password.
    </div>
  </c:when>
</c:choose>