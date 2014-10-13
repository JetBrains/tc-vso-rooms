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
  /css/admin/adminMain.css
  /css/admin/serverConfig.css
  /plugins/vso-rooms/css/editSettings.css
</bs:linkCSS>
<bs:linkScript>
  /js/bs/testConnection.js
  /plugins/vso-rooms/js/editSettings.js
</bs:linkScript>

<jsp:useBean id="vsoRoomsSettings" scope="request" type="jetbrains.buildServer.vsoRooms.controllers.VSONotificatorSettingsBean"/>

<script type="text/javascript">
  document.observe("dom:loaded", function() {
    VSOTeamRooms.SettingsForm.setupEventHandlers();
    $('account').focus();
  });
</script>

<c:url value="/vso/notificatorSettings.html?edit=1" var="url"/>

<div id="settingsContainer">
  <form action="${url}" method="post" onsubmit="return VSOTeamRooms.SettingsForm.submitSettings()" autocomplete="off">
    <div class="editNotificatorSettingsPage">
      <c:choose>
        <c:when test="${vsoRoomsSettings.paused}">
          <div class="pauseNote" style="margin-bottom: 1em;">
            The notifier is <strong>disabled</strong>. All team room messages are suspended&nbsp;&nbsp;<a class="btn btn_mini" href="#" id="enable-btn">Enable</a>
          </div>
        </c:when>
        <c:otherwise>
          <div style="margin-left: 0.6em;">
            The notifier is <strong>enabled</strong>&nbsp;&nbsp;<a class="btn btn_mini" href="#" id="disable-btn">Disable</a>
          </div>
        </c:otherwise>
      </c:choose>

      <bs:messages key="settingsSaved"/>

      <table class="runnerFormTable">
        <tr>
          <th><label for="account">Account: <l:star/></label></th>
          <td>
            <forms:textField name="account" value="${vsoRoomsSettings.account}"/>
            <span class="error" id="errorAccount"></span>
          </td>
        </tr>
        <tr>
          <th><label for="username">Username: <l:star/></label></th>
          <td>
            <forms:textField name="username" value="${vsoRoomsSettings.username}"/>
            <span class="error" id="errorUsername"></span>
          </td>
        </tr>
        <tr>
          <th><label for="password">Password: <l:star/></label></th>
          <td>
            <forms:passwordField name="password" encryptedPassword="${vsoRoomsSettings.encryptedPassword}"/>
            <span class="error" id="errorPassword"></span>
          </td>
        </tr>
      </table>

      <div class="saveButtonsBlock">
        <forms:submit label="Save"/>
        <forms:submit id="testConnection" type="button" label="Test connection"/>
        <input type="hidden" id="submitSettings" name="submitSettings" value="store"/>
        <input type="hidden" id="publicKey" name="publicKey" value="<c:out value='${vsoRoomsSettings.hexEncodedPublicKey}'/>"/>
        <forms:saving/>
      </div>

    </div>

  </form>
</div>

<bs:dialog dialogId="testConnectionDialog" title="Test Connection" closeCommand="BS.TestConnectionDialog.close();"
           closeAttrs="showdiscardchangesmessage='false'">
  <div id="testConnectionStatus"></div>
  <div id="testConnectionDetails" class="mono"></div>
</bs:dialog>

<forms:modified/>

<script type="text/javascript">
  (function($) {
    var sendAction = function(enable) {
      $.post("${url}&action=" + (enable ? 'enable' : 'disable'), function() {
        BS.reload(true);
      });
      return false;
    };

    $("#enable-btn").click(function() {
      return sendAction(true);
    });

    $("#disable-btn").click(function() {
      if (!confirm("Visual Studio Team Rooms notifications will not be sent until enabled. Disable the notifier?")) return false;
      return sendAction(false);
    })
  })(jQuery);
</script>