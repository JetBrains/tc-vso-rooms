

package jetbrains.buildServer.vsoRooms.controllers;

import jetbrains.buildServer.controllers.RememberState;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificatorSettingsBean extends RememberState {
  private boolean myPaused;
  private final int myNumberOfAffectedUsers;

  public VSONotificatorSettingsBean(boolean paused, int numberOfAffectedUsers) {
    myPaused = paused;
    myNumberOfAffectedUsers = numberOfAffectedUsers;
    rememberState();
  }

  public boolean isPaused() {
    return myPaused;
  }

  public void setPaused(boolean paused) {
    myPaused = paused;
  }

  public int getNumberOfAffectedUsers() {
    return myNumberOfAffectedUsers;
  }
}