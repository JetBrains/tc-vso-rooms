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

package jetbrains.buildServer.vsoRooms.controllers;

import jetbrains.buildServer.controllers.RememberState;
import jetbrains.buildServer.serverSide.crypt.RSACipher;
import jetbrains.buildServer.util.StringUtil;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificatorSettingsBean extends RememberState {
  private String myAccount;
  private String myUsername;
  private String myPassword;
  private String myEncryptedPassword;

  public boolean isPaused() {
    return false;
  }

  public String getAccount() {
    return myAccount;
  }

  public String getUsername() {
    return myUsername;
  }

  public String getHexEncodedPublicKey() {
    return RSACipher.getHexEncodedPublicKey();
  }

  public String getEncryptedPassword() {
    return StringUtil.isEmpty(myPassword) ? "" : RSACipher.encryptDataForWeb(myPassword);
  }
}
