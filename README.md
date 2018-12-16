## Hello people from the future
Hello, If you are reading this looking for a plugin for your minecraft server then look elsewhere as this plugin is no longer maintained and is not compatible with minecraft's UUID structure. A good starting point would be [here](https://www.spigotmc.org/resources/reviewme-reloaded.2896/). The original first version of this plugin was released on Aug 13th , 2013! for CraftBukkit 1.6.2. This version is written for CB 1.6.4-R1.0.

I have uploaded this plugin to Github as there are a few copycats. I have therefore decided to make this plugin open source.

## ReviewMe
ReviewMe is a simple and easy to use management system for your players, it allows them to submit their creations and allow them to be assessed, If you have a creative server that asks that players show off what they can build to "rank up" then this plugin is for you. The plugin comes with many features and is very light on server resources, Some players often want their build to be checked, but no admin's are online, this can be frustrating... but this plugin fixes that,It also stops chat being spammed with "someone look at my build" as players can only do it once until it is assessed. with one simple command server staff can view submitted builds and teleport to the build locations even when the player is not online, players can also check to see if the build has been assessed, Server staff can also choose to Approve or deny the build. Optionally server staff can be notified if a build has been submitted for them to asses.It also can help server staff avoid confrontation and remain anonymous. Once the creation is assessed, the player can have their next creation reviewed straight away.

### Basic Commands
- /rm help = View the plugins help page (same as /rm)
- /rm gethelp = Request help from server staff
- /rm review = Submit a build for review
- /rm noreview = Cancel your build submission
- /rm list [A, P, D] = List builds that are Approved,denied or Pending review. (Or lists based on their status)
- /rm goto <username> = Teleport to a submitted build
- /rm complete <username> = Approve a build
- /rm deny <username> = Deny a build
- /rm check = Check to see if your build has been reviewed

### Permissions

- reviewme.review | Allow players to submit their build for review | /rm review
- reviewme.alert | Have an Alert message if a player has submitted a build for review
- reviewme.view | Allow a player to Teleport to a build to assess it's potential
- reviewme.gethelp | Allow a player to ask for help
- reviewme.list | Allow a player to list builds that are Approved,denied or Pending review.
- reviewme.complete | Allow players to complete a build | /rm complete <username>
- reviewme.deny |Allow players to deny a build | /rm deny <username>
  
### Known Bugs
None of late!
