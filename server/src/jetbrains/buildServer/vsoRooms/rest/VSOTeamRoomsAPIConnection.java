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

package jetbrains.buildServer.vsoRooms.rest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOTeamRoomsAPIConnection {

  private static final Logger LOG = Logger.getLogger(VSOTeamRoomsAPIConnection.class);

  private final String myAccount;
  @NotNull
  private final String myUser;
  @NotNull
  private final String myPassword;

  public VSOTeamRoomsAPIConnection(@NotNull String account, @NotNull String user, @NotNull String password) {
    myAccount = account;
    myUser = user;
    myPassword = password;
  }

  @Nullable
  public String sendMessageToRoom(String roomId, String messageContent){
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + getEncodedCreds(myUser, myPassword));
    headers.add("Content-type", "application/json");
    HttpEntity<String> request = new HttpEntity<String>(getBody(messageContent), headers);
    final RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(getRoomMessagesUrl(roomId), HttpMethod.POST, request, String.class);
    return response.getBody();
  }

  private String getBody(String messageContent) {
    return String.format("{ \"content\": \"%s\" }", messageContent);
  }

  private static String getEncodedCreds(String user, String password) {
    String plainCreds = String.format("%s:%s", user, password);
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    return new String(base64CredsBytes);
  }

  private String getRoomMessagesUrl(String roomId) {
    return String.format("https://%s.visualstudio.com/defaultcollection/_apis/chat/rooms/%s/messages", myAccount, roomId);
  }
}
