JCore
=====

Core plugin containing methods required by some of my plugins. However it does have extra functionality that you can use without extra plugins.

**Functionality**:
* Backups
* Chat / Command logging
* Play music discs
* Play midi files

Bukkit Dev Link: http://dev.bukkit.org/bukkit-plugins/jcore-juicydev/


MCStats: [Stats for JCore](http://mcstats.org/plugin/JCore)  
[![MCStats for JCore](http://api.mcstats.org/signature/JCore.png)](http://mcstats.org/plugin/JCore)




Download
--------


Version:
  
* [v2.0.1](http://dev.bukkit.org/media/files/791/131/JCore_v2.0.1.jar) for CB 1.7.9 and higher




Permissions
-----------



|*Permission*|*Command*|*=Description*|*Default*|
|jcore.playdisc|/playdisc <disc> [all/player] or /playdisc list|Play a music disc to a player or globally|Op|
|jcore.playmidi|/playmidi <all/player> <filename> or /playmidi list|Play a midi file to a player or globally|Op|
|jcore.backup|/backup|Run a backup|Op|
|jcore.debug|/debug [on/off]|Enable/Disable debug info for my plugins|Op|




Config
------



|=Option|=Description|=Default|
|debug|Show debug information on the console|false|
|logging-enabled|Enable logging of player chat and commands|false|
|backup-enabled|Enable backing up of the server|false|
|backup-delay|The time between backups in minutes|30|
