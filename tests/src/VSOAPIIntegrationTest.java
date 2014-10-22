import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.vsoRooms.rest.TeamRoom;
import jetbrains.buildServer.vsoRooms.rest.TeamRoomMessage;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VSOAPIIntegrationTest extends BaseTestCase {

  private static final String TESTS_CREDENTIALS_TXT = "tests-credentials.txt";

  private VSOTeamRoomsAPIConnection myAPIConnection;
  private String myAccount;

  @Override
  @BeforeMethod
  public void setUp() throws Exception {
    super.setUp();

    String username;
    String password;

    final Map<String, String> localCredentials = new HashMap<String, String>();

    final File localCredentialsStorage = new File(TESTS_CREDENTIALS_TXT);
    if(localCredentialsStorage.isFile()){
      for(String line : FileUtil.readFile(localCredentialsStorage)){
        final int separatorLocation = line.indexOf(" ");
        if(separatorLocation == -1) continue;
        localCredentials.put(line.substring(0, separatorLocation).trim(), line.substring(separatorLocation).trim());
      }
    }

    myAccount = localCredentials.containsKey("TEST_VSO_ACCOUNT") ? localCredentials.get("TEST_VSO_ACCOUNT") : System.getenv("TC_TEST_VSO_ACCOUNT");
    assertNotNull(myAccount);

    username = localCredentials.containsKey("TEST_VSO_USERNAME") ? localCredentials.get("TEST_VSO_USERNAME") : System.getenv("TC_TEST_VSO_USERNAME");
    assertNotNull(username);

    password = localCredentials.containsKey("TEST_VSO_PASSWORD") ? localCredentials.get("TEST_VSO_PASSWORD") : System.getenv("TC_TEST_VSO_PASSWORD");
    assertNotNull(password);

    myAPIConnection = VSOTeamRoomsAPI.createConnection(username, password);
  }

  @Test
  public void testSendMessage() {
    final TeamRoomMessage message = myAPIConnection.sendMessageToRoom(myAccount, 16343L, "hello!");
    assertNotNull(message);
    assertEquals("hello!", message.getContent());
  }

  @Test
  public void testGetListOfRooms() {
    final Collection<TeamRoom> rooms = myAPIConnection.getListOfRooms(myAccount);
    assertNotEmpty(rooms);
    for (TeamRoom room : rooms){
      log(room.getName() + " " + room.getId());
    }
  }

  @Test
  public void testGetListOfRoomsOnBehalfOfUnknownUser() throws Exception {
    assertEmpty(VSOTeamRoomsAPI.createConnection("unknown_user", "some_password").getListOfRooms(myAccount));
  }
}
