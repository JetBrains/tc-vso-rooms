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
