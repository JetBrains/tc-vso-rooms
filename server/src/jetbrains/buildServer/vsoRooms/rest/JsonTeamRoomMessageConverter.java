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

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class JsonTeamRoomMessageConverter implements HttpMessageConverter<TeamRoomMessage> {

  private final ObjectMapper myMapper = new ObjectMapper();

  public boolean canRead(Class<?> aClass, MediaType mediaType) {
    return aClass.equals(TeamRoomMessage.class);
  }

  public boolean canWrite(Class<?> aClass, MediaType mediaType) {
    return false;
  }

  public List<MediaType> getSupportedMediaTypes() {
    return Collections.singletonList(MediaType.APPLICATION_JSON);
  }

  public TeamRoomMessage read(Class<? extends TeamRoomMessage> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
    return myMapper.readValue(httpInputMessage.getBody(), TeamRoomMessage.class);
  }

  public void write(TeamRoomMessage teamRoomMessage, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
  }
}
