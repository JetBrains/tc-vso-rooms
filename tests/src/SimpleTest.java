import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import org.testng.annotations.Test;

public class SimpleTest extends BaseTestCase {
  @Test
  public void Test() {
    assertEquals("jhopa", VSOTeamRoomsAPI.connect("ekoshkin", "ekoshkin", "R42apfoo").sendMessageToRoom("11141", "hello!"));
  }
}
