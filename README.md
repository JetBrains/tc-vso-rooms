TeamCity server plugin which allows sending notifications to [Visual Studio Online rooms] (http://www.visualstudio.com/en-us/get-started/collaborate-in-the-team-room-vs.aspx).

‘alternate credentials’ must be enagled in the Visual Studio Online account.
A separate TeamCity account has to be created in order to send messages “on behalf of”. 
The target team room and Visual Studio Online account as well as "on behalf of" Visual Studio Online user's alternate credentials should be specified in user settings of the newly created TeamCity user.

Notifications about a particular event sent on behalf of several users to one room will be merged.

Official [plugin page] (https://confluence.jetbrains.com/display/TW/Visual+Studio+Online+Team+Rooms+Notifier)

Public CI [build] (https://teamcity.jetbrains.com/viewType.html?buildTypeId=TeamCityPluginsByJetBrains_Unsorted_VsoTeamRoomsNotifie)
