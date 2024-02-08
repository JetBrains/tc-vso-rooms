

package jetbrains.buildServer.vsoRooms.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jetbrains.buildServer.vsoRooms.rest.TeamRoomList;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * @author Evgeniy.Koshkin
 */
public class JsonTeamRoomListConverter extends AbstractHttpMessageConverter<TeamRoomList> {

  private final ObjectMapper myMapper = new ObjectMapper();

  public JsonTeamRoomListConverter() {
    super(MediaType.APPLICATION_JSON);
  }

  @Override
  protected boolean supports(Class<?> aClass) {
    return TeamRoomList.class.equals(aClass);
  }

  @Override
  protected TeamRoomList readInternal(Class<? extends TeamRoomList> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
    return myMapper.readValue(httpInputMessage.getBody(), TeamRoomList.class);
  }

  @Override
  protected void writeInternal(TeamRoomList teamRoomList, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {

  }
}