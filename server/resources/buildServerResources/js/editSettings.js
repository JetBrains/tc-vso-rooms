

/**
 * Created by Evgeniy.Koshkin on 01.09.2014.
 */

VSOTeamRooms = {};

VSOTeamRooms.UserSettingsForm = OO.extend(BS.AbstractPasswordForm, {

  setupEventHandlers: function() {
    var that = this;
    $('test-connection-btn').on('click', this.testConnection.bindAsEventListener(this));
  },

  submitSettings: function() {
    console.info("submit settings called");
    var that = this;
    $("submitSettings").value = 'store';
    this.removeUpdateStateHandlers();
    BS.PasswordFormSaver.save(this, this.formElement().action, OO.extend(BS.ErrorsAwareListener, this.createErrorListener()));
    return false;
  },

  testConnection: function (){
    $("submitSettings").value = 'testConnection';
    var listener = OO.extend(BS.ErrorsAwareListener, this.createErrorListener());
    var oldOnCompleteSave = listener['onCompleteSave'];
    listener.onCompleteSave = function(form, responseXML, err) {
      oldOnCompleteSave(form, responseXML, err);
      if (!err) {
        form.enable();
        if (responseXML) {
          var res = responseXML.getElementsByTagName("testConnectionResult");
          if (res.length > 0) {   // trouble
            BS.TestConnectionDialog.show(false, res[0].firstChild.nodeValue, $('testConnection'));
          } else {
            BS.TestConnectionDialog.show(true, "", $('testConnection'));
          }
        }
      }
    };

    BS.PasswordFormSaver.save(this, this.formElement().action, listener);
  },

  createErrorListener: function() {
    var that = this;
    return {
      onException: function(form, e) {
        console.trace(e);
      },

      onEmptyAccountError: function(elem) {
        $("errorAccount").innerHTML = elem.firstChild.nodeValue;
        that.highlightErrorField($("account"));
      },

      onEmptyTeamRoomNameError: function(elem) {
        $("errorTeamRoomName").innerHTML = elem.firstChild.nodeValue;
        that.highlightErrorField($("teamRoomName"));
      },

      onEmptyUsernameError: function(elem) {
        $("errorUsername").innerHTML = elem.firstChild.nodeValue;
        that.highlightErrorField($("username"));
      },

      onEmptyPasswordError: function(elem) {
        $("errorPassword").innerHTML = elem.firstChild.nodeValue;
        that.highlightErrorField($("password"));
      },

      onCompleteSave: function(form, responseXML, err) {
        BS.ErrorsAwareListener.onCompleteSave(form, responseXML, err);
        if (!err) {
          BS.XMLResponse.processRedirect(responseXML);
        } else {
          that.setupEventHandlers();
        }
      }
    };
  }
});