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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Evgeniy.Koshkin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRoom {
  private long myId;
  private String myName;
  private String myDescription;

  public TeamRoom() {
  }

  public TeamRoom(long id, String name, String description) {
    myId = id;
    myName = name;
    myDescription = description;
  }

  public long getId() {
    return myId;
  }

  public String getName() {
    return myName;
  }

  public String getDescription() {
    return myDescription;
  }

  public void setId(long id) {
    myId = id;
  }

  public void setName(String name) {
    myName = name;
  }

  public void setDescription(String description) {
    myDescription = description;
  }
}
