TeamCity server plugin which allows to send notifications to [Visual Studio Online rooms] (http://www.visualstudio.com/en-us/get-started/collaborate-in-the-team-room-vs.aspx).

User should enable ‘alternate credentials’ in her VSO account. After that notifier will be able to call VSO API.
Notifier settings are pretty simple – VSO account + user name + password
Separate TeamCity account expected to be created in order to send messages “on behalf”. Team room where all the messages will appear should be specified in user settings of newly created user.
Optionally – some of the users could specify their VSO user names in their settings. If specified those user names will be used to mention those users in messages (e.g. @evgeniy koshkin is responsible for global warming).

Public CI [build] (https://teamcity.jetbrains.com/viewType.html?buildTypeId=TeamCityPluginsByJetBrains_Unsorted_VsoTeamRoomsNotifie)
