[![official JetBrains project](http://jb.gg/badges/official-plastic.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) <img src="https://teamcity.jetbrains.com/app/rest/builds/buildType:TeamCityPluginsByJetBrains_Unsorted_VsoTeamRoomsNotifie/statusIcon.svg"/>

TeamCity server plugin which allows sending notifications to [Visual Studio Online rooms](http://www.visualstudio.com/en-us/get-started/collaborate-in-the-team-room-vs.aspx).

‘alternate credentials’ must be enabled in the Visual Studio Online account.
A separate TeamCity account has to be created in order to send messages “on behalf of”. 
The target team room and Visual Studio Online account as well as "on behalf of" Visual Studio Online user's alternate credentials should be specified in the user settings of the newly created TeamCity user.

Notifications about a particular event sent on behalf of several users to one room will be merged.

Official [plugin page](https://confluence.jetbrains.com/display/TW/Visual+Studio+Online+Team+Rooms+Notifier).
