

<%@ include file="/include.jsp" %>

<bs:linkCSS dynamic="${true}">
  /css/admin/adminMain.css
  /css/admin/serverConfig.css
  /plugins/vso-rooms/css/editSettings.css
</bs:linkCSS>

<jsp:useBean id="vsoRoomsSettings" scope="request" type="jetbrains.buildServer.vsoRooms.controllers.VSONotificatorSettingsBean"/>

<c:url value="/vso/notificatorSettings.html?edit=1" var="url"/>

<div id="settingsContainer">
  <form action="${url}" method="post" autocomplete="off">
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

      <table class="runnerFormTable">
        <tr class="noBorder">
          <td>
            <b>${vsoRoomsSettings.numberOfAffectedUsers}</b> user<bs:s val="${vsoRoomsSettings.numberOfAffectedUsers}"/> configured VS Online notification rules.
          </td>
        </tr>
        <tr class="noBorder">
          <td>
            The templates for Visual Studio Online notifications <a target="_blank" href="<bs:helpUrlPrefix/>/Customizing+Notifications" showdiscardchangesmessage="false">can be customized</a>.
          </td>
        </tr>
      </table>

    </div>

  </form>
</div>

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