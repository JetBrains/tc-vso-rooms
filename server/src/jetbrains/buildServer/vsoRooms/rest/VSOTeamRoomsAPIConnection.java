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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOTeamRoomsAPIConnection {

  private static final Logger LOG = Logger.getLogger(VSOTeamRoomsAPIConnection.class);

  private final String myAccount;
  private final RestTemplate myRestTemplate;

  public VSOTeamRoomsAPIConnection(@NotNull String account, @NotNull String user, @NotNull String password) {
    myAccount = account;
    myRestTemplate = createRestTemplate(user, password);
  }

  @Nullable
  public String sendMessageToRoom(String roomId, String messageContent){
    final String url = getRoomMessagesUrl(roomId);
    final Map<String, String> request = new HashMap<String, String>();
    request.put("content", messageContent);
    try{
      final ResponseEntity<String> responseEntity = myRestTemplate.postForEntity(url, request, String.class);
      return responseEntity.getBody();
    } catch (RestClientException ex){
      LOG.warn(String.format("Failed to send message to the room with id %s.", roomId), ex);
      return null;
    }
  }

  private String getRoomMessagesUrl(String roomId) {
    return String.format("https://%s.visualstudio.com/defaultcollection/_apis/chat/rooms/%s/messages", myAccount, roomId);
  }

  private RestTemplate createRestTemplate(String username, String password) {
    return new RestTemplate(createSecureTransport(username, password));
  }

  private ClientHttpRequestFactory createSecureTransport(String username, String password){
    HttpClient client = new HttpClient();
    client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
    return new CommonsClientHttpRequestFactory(client);
  }
}
