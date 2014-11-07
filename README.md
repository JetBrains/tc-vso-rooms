TeamCity server plugin which allows to send notifications to [Visual Studio Online rooms] (http://www.visualstudio.com/en-us/get-started/collaborate-in-the-team-room-vs.aspx).

User should enable ‘alternate credentials’ in her Visual Studio Online account.
Separate TeamCity account expected to be created in order to send messages “on behalf”. 
Target team room and Visual Studio Online account as well as "on behalf of" Visual Studio Online user's alternate credentials should be specified in user settings of newly created TeamCity user.

Notifications about particular event sent on behalf of several users to one room whould be merged.

Public CI [build] (https://teamcity.jetbrains.com/viewType.html?buildTypeId=TeamCityPluginsByJetBrains_Unsorted_VsoTeamRoomsNotifie)
