

import com.google.common.collect.Lists;
import jetbrains.buildServer.serverSide.impl.BaseServerTestCase;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.vsoRooms.notificator.VSOMessageSender;
import jetbrains.buildServer.vsoRooms.notificator.VSOUserProperties;
import jetbrains.buildServer.vsoRooms.rest.TeamRoom;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPI;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOMessageSenderTest extends BaseServerTestCase {
  @Test
  public void test_send_messages_to_two_rooms_in_a_single_account() throws Exception {
    Set<SUser> users = new HashSet<SUser>();

    final SUser user1 = createUser("user1");
    VSOUserProperties.setAccount(user1, "account");
    VSOUserProperties.setTeamRoomName(user1, "room1");
    VSOUserProperties.setUsername(user1, "username1");
    VSOUserProperties.setPassword(user1, "password1");
    users.add(user1);

    final SUser user2 = createUser("user2");
    VSOUserProperties.setAccount(user2, "account");
    VSOUserProperties.setTeamRoomName(user2, "room2");
    VSOUserProperties.setUsername(user2, "username2");
    VSOUserProperties.setPassword(user2, "password2");
    users.add(user2);

    final Mockery m = new Mockery();

    final VSOTeamRoomsAPIConnection apiConnectionMock = m.mock(VSOTeamRoomsAPIConnection.class);
    m.checking(new Expectations() {{
      allowing(apiConnectionMock).getListOfRooms("account");
      will(returnValue(Lists.newArrayList(new TeamRoom(1L, "room1", "test room 1"), new TeamRoom(2L, "room2", "test room 2"))));
      one(apiConnectionMock).sendMessageToRoom("account", 1L, "test");
      one(apiConnectionMock).sendMessageToRoom("account", 2L, "test");
    }});

    final VSOTeamRoomsAPI apiMock = m.mock(VSOTeamRoomsAPI.class);
    m.checking(new Expectations() {{
      allowing(apiMock).createConnection("username1", "password1");
      will(returnValue(apiConnectionMock));
      allowing(apiMock).createConnection("username2", "password2");
      will(returnValue(apiConnectionMock));
    }});

    new VSOMessageSender(apiMock).sendMessageOnBehalfOfUsers(users, "test");

    m.assertIsSatisfied();
  }

  @Test
  public void test_send_messages_to_rooms_with_the_same_name_in_separate_accounts() throws Exception {
    Set<SUser> users = new HashSet<SUser>();

    final SUser user1 = createUser("user1");
    VSOUserProperties.setAccount(user1, "account1");
    VSOUserProperties.setTeamRoomName(user1, "room");
    VSOUserProperties.setUsername(user1, "username1");
    VSOUserProperties.setPassword(user1, "password1");
    users.add(user1);

    final SUser user2 = createUser("user2");
    VSOUserProperties.setAccount(user2, "account2");
    VSOUserProperties.setTeamRoomName(user2, "room");
    VSOUserProperties.setUsername(user2, "username2");
    VSOUserProperties.setPassword(user2, "password2");
    users.add(user2);

    final Mockery m = new Mockery();

    final VSOTeamRoomsAPIConnection apiConnectionMock = m.mock(VSOTeamRoomsAPIConnection.class);
    m.checking(new Expectations() {{
      allowing(apiConnectionMock).getListOfRooms("account1");
      will(returnValue(Collections.singletonList(new TeamRoom(1L, "room", "test room"))));
      allowing(apiConnectionMock).getListOfRooms("account2");
      will(returnValue(Collections.singletonList(new TeamRoom(1L, "room", "test room"))));
      one(apiConnectionMock).sendMessageToRoom("account1", 1L, "test");
      one(apiConnectionMock).sendMessageToRoom("account2", 1L, "test");
    }});

    final VSOTeamRoomsAPI apiMock = m.mock(VSOTeamRoomsAPI.class);
    m.checking(new Expectations() {{
      allowing(apiMock).createConnection("username1", "password1");
      will(returnValue(apiConnectionMock));
      allowing(apiMock).createConnection("username2", "password2");
      will(returnValue(apiConnectionMock));
    }});

    new VSOMessageSender(apiMock).sendMessageOnBehalfOfUsers(users, "test");

    m.assertIsSatisfied();
  }

  @Test
  public void should_merge_messages_sent_to_the_same_room() throws Exception {
    Set<SUser> users = new HashSet<SUser>();

    final SUser user1 = createUser("user1");
    VSOUserProperties.setAccount(user1, "account");
    VSOUserProperties.setTeamRoomName(user1, "room");
    VSOUserProperties.setUsername(user1, "username1");
    VSOUserProperties.setPassword(user1, "password1");
    users.add(user1);

    final SUser user2 = createUser("user2");
    VSOUserProperties.setAccount(user2, "account");
    VSOUserProperties.setTeamRoomName(user2, "room");
    VSOUserProperties.setUsername(user2, "username2");
    VSOUserProperties.setPassword(user2, "password2");
    users.add(user2);

    final Mockery m = new Mockery();

    final VSOTeamRoomsAPIConnection apiConnectionMock = m.mock(VSOTeamRoomsAPIConnection.class);
    m.checking(new Expectations() {{
      allowing(apiConnectionMock).getListOfRooms("account");
      will(returnValue(Collections.singletonList(new TeamRoom(1L, "room", "test room"))));
      one(apiConnectionMock).sendMessageToRoom("account", 1L, "test");
    }});

    final VSOTeamRoomsAPI apiMock = m.mock(VSOTeamRoomsAPI.class);
    m.checking(new Expectations() {{
      allowing(apiMock).createConnection("username1", "password1");
      will(returnValue(apiConnectionMock));
      allowing(apiMock).createConnection("username2", "password2");
      will(returnValue(apiConnectionMock));
    }});

    new VSOMessageSender(apiMock).sendMessageOnBehalfOfUsers(users, "test");

    m.assertIsSatisfied();
  }
}