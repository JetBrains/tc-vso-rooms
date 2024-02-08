

package jetbrains.buildServer.vsoRooms.notificator;

import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.ServerPaths;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Evgeniy.Koshkin
 */
public class VSONotificatorConfigHolder {
  private VSONotificatorConfig myConfig;

  public VSONotificatorConfigHolder(@NotNull ServerPaths serverPaths, @NotNull SBuildServer server) throws IOException {
    myConfig = new VSONotificatorConfig(serverPaths);
    server.addListener(new BuildServerAdapter() {
      @Override
      public void serverShutdown() {
        myConfig.dispose();
      }
    });
  }

  @NotNull
  public VSONotificatorConfig getConfig(){
    return myConfig;
  }
}