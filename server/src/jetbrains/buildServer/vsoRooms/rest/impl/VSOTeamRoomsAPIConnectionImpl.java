

package jetbrains.buildServer.vsoRooms.rest.impl;

import jetbrains.buildServer.vsoRooms.rest.*;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOTeamRoomsAPIConnectionImpl implements VSOTeamRoomsAPIConnection {

  @NotNull private final String myUser;
  @NotNull private final String myPassword;
  @NotNull private final RestTemplate myRestTemplate;

  public VSOTeamRoomsAPIConnectionImpl(@NotNull String user, @NotNull String password) {
    myUser = user;
    myPassword = password;
    myRestTemplate = new RestTemplate();
    List<HttpMessageConverter<?>> customConverters = new ArrayList<HttpMessageConverter<?>>();
    customConverters.add(new JsonTeamRoomListConverter());
    customConverters.add(new JsonTeamRoomMessageConverter());
    customConverters.add(new StringJsonConverter());
    myRestTemplate.setMessageConverters(customConverters);
  }

  @Nullable
  public TeamRoomMessage sendMessageToRoom(@NotNull String account, @NotNull Long roomId, @NotNull String messageContent){
    final HttpHeaders requestHeaders = getRequestHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    final HttpEntity<String> request = new HttpEntity<String>(getMessageBody(messageContent), requestHeaders);
    final ResponseEntity<TeamRoomMessage> responseEntity = myRestTemplate.postForEntity(getRoomMessagesUrl(account, roomId), request, TeamRoomMessage.class);
    return responseEntity.getBody();
  }

  @NotNull
  public Collection<TeamRoom> getListOfRooms(@NotNull String account) {
    final HttpEntity<String> request = new HttpEntity<String>(getRequestHeaders());
    final ResponseEntity<TeamRoomList> responseEntity = myRestTemplate.exchange(getListOfRoomsUrl(account), HttpMethod.GET, request, TeamRoomList.class);
    return responseEntity.getBody().getRooms();
  }

  private HttpHeaders getRequestHeaders() {
    final HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + getEncodedCreds(myUser, myPassword));
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    return headers;
  }

  private static String getEncodedCreds(String user, String password) {
    String plainCreds = String.format("%s:%s", user, password);
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    return new String(base64CredsBytes);
  }

  private String getListOfRoomsUrl(String account) {
    return String.format("https://%s.visualstudio.com/defaultcollection/_apis/chat/rooms?api-version=1.0", account);
  }

  private String getRoomMessagesUrl(String account, Long roomId) {
    return String.format("https://%s.visualstudio.com/defaultcollection/_apis/chat/rooms/%d/messages?api-version=1.0", account, roomId);
  }

  private String getMessageBody(String messageContent) {
    return String.format("{ \"content\" : \"%s\" }", messageContent);
  }
}