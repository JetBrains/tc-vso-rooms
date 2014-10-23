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

package jetbrains.buildServer.vsoRooms.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jetbrains.buildServer.vsoRooms.rest.TeamRoomMessage;
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
public class JsonTeamRoomMessageConverter extends AbstractHttpMessageConverter<TeamRoomMessage> {

  private final ObjectMapper myMapper = new ObjectMapper();

  public JsonTeamRoomMessageConverter() {
    super(MediaType.APPLICATION_JSON);
  }

  @Override
  protected boolean supports(Class<?> aClass) {
    return TeamRoomMessage.class.equals(aClass);
  }

  @Override
  protected TeamRoomMessage readInternal(Class<? extends TeamRoomMessage> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
    return myMapper.readValue(httpInputMessage.getBody(), TeamRoomMessage.class);
  }

  @Override
  protected void writeInternal(TeamRoomMessage teamRoomMessage, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
  }
}
