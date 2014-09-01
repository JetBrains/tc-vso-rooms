package jetbrains.buildServer.notification.vsoRooms;/*
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

import jetbrains.buildServer.notification.NotificatorAdapter;
import jetbrains.buildServer.notification.NotificatorRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * @author Evgeniy.Koshkin
 */
public class VSORoomsNotificator extends NotificatorAdapter {

  private static final String VSO_ROOMS_NOTIFICATOR_TYPE = "vso-rooms";

  public VSORoomsNotificator(@NotNull NotificatorRegistry registry) {
    registry.register(this);
  }

  @NotNull
  public String getNotificatorType() {
    return VSO_ROOMS_NOTIFICATOR_TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Visual Studio Online Notifier";
  }

  private void sendMessage(){

  }
}
