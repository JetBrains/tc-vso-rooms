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

import com.google.common.collect.Lists;
import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.vsoRooms.notificator.VSOTeamRoomIdsCache;
import jetbrains.buildServer.vsoRooms.rest.TeamRoom;
import jetbrains.buildServer.vsoRooms.rest.VSOTeamRoomsAPIConnection;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Evgeniy.Koshkin
 */
public class VSOTeamRoomIdsCacheTest extends BaseTestCase {

  private VSOTeamRoomIdsCache myCache;
  private Mockery myMockery;

  @Override
  @BeforeMethod
  public void setUp() throws Exception {
    super.setUp();
    myCache = new VSOTeamRoomIdsCache();
    myMockery = new Mockery();
  }

  @Test
  public void test_room_names_collision_in_separate_accounts() throws Exception {
    final VSOTeamRoomsAPIConnection apiConnection = myMockery.mock(VSOTeamRoomsAPIConnection.class);
    myMockery.checking(new Expectations() {{
      one(apiConnection).getListOfRooms("accountA");
      will(returnValue(Lists.newArrayList(new TeamRoom(1L, "room", "accountA room"))));
      one(apiConnection).getListOfRooms("accountB");
      will(returnValue(Lists.newArrayList(new TeamRoom(2L, "room", "accountB room"))));
    }});
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("accountA", "room", apiConnection));
    assertEquals(Long.valueOf(2), myCache.getOrResolveRoomId("accountB", "room", apiConnection));
    myMockery.assertIsSatisfied();
  }

  @Test
  public void should_be_case_insensitive_for_team_room_name() throws Exception {
    final VSOTeamRoomsAPIConnection apiConnection = myMockery.mock(VSOTeamRoomsAPIConnection.class);
    myMockery.checking(new Expectations() {{
      one(apiConnection).getListOfRooms("account");
      will(returnValue(Lists.newArrayList(new TeamRoom(1L, "Room", "account room"))));
    }});
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("account", "ROOM", apiConnection));
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("account", "room", apiConnection));
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("account", "Room", apiConnection));
    myMockery.assertIsSatisfied();
  }

  @Test
  public void should_be_case_insensitive_for_account() throws Exception {
    final VSOTeamRoomsAPIConnection apiConnection = myMockery.mock(VSOTeamRoomsAPIConnection.class);
    myMockery.checking(new Expectations() {{
      one(apiConnection).getListOfRooms("account");
      will(returnValue(Lists.newArrayList(new TeamRoom(1L, "room", "account room"))));
    }});
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("account", "room", apiConnection));
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("Account", "room", apiConnection));
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("ACCOUNT", "room", apiConnection));
    myMockery.assertIsSatisfied();
  }

  @Test
  public void should_resolve_all_account_room_ids_on_first_call() throws Exception {
    final VSOTeamRoomsAPIConnection apiConnection = myMockery.mock(VSOTeamRoomsAPIConnection.class);
    myMockery.checking(new Expectations() {{
      one(apiConnection).getListOfRooms("account");
      will(returnValue(Lists.newArrayList(new TeamRoom(1L, "room1", "account room 1"), new TeamRoom(2L, "room2", "account room 2"))));
    }});
    assertEquals(Long.valueOf(1), myCache.getOrResolveRoomId("account", "room1", apiConnection));
    assertEquals(Long.valueOf(2), myCache.getOrResolveRoomId("account", "room2", apiConnection));
  }
}
