

package jetbrains.buildServer.vsoRooms.rest.impl;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy.Koshkin
 */
public class StringJsonConverter implements HttpMessageConverter<String> {

  public boolean canRead(Class<?> aClass, MediaType mediaType) {
    return false;
  }

  public boolean canWrite(Class<?> aClass, MediaType mediaType) {
    return aClass.equals(String.class) && mediaType.equals(MediaType.APPLICATION_JSON);
  }

  public List<MediaType> getSupportedMediaTypes() {
    return Collections.singletonList(MediaType.APPLICATION_JSON);
  }

  public String read(Class<? extends String> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
    return "";
  }

  public void write(String teamRoomMessage, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
    httpOutputMessage.getBody().write(teamRoomMessage.getBytes());
  }
}