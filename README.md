# moarTP v0.71

Author: Nathan Bossart  
Website: <http://ofallonminecraft.com/>  
Contact: <info@ofallonminecraft.com>  


# Description:
The moarTP plugin adds 11 new commands for convenient, customized teleportation. Instead of dealing with coordinates or depending on other players, users may save locations to the server for easy teleportation later. This provides for easy navigation between player creations.


The 11 commands added by this plugin are:

    /tpto        [location] (password)                          : teleport to a custom location
    /claim       [location] (["description"])                   : define a custom location (where you're standing)
    /claimsecret [location] (password) (["description"])        : define a custom location encrypted with a passphrase
    /describe    [location] ["description"]                     : alter or add a description to a custom location
    /unclaim     [location]                                     : remove a custom location
    /view        (player)                                       : view all locations (or locations made by a player)
    /about       [location]                                     : view location information
    /move [player1(,player2,player3,...)] [location] (password) : teleport other player(s) to a custom location
    /sethome     [location]                                     : set your personal home location
    /gohome                                                     : go to your personal home location
    /whereis     [player]                                       : retrieve the nearest public location to a player


# Important Note:
It is very important that servers running previous versions (<0.71) of this plugin update to the newest version of moarTP!  Minecraft is beginning to allow players to change usernames, which could make it possible for players to alter or delete other players' locations.  As of v0.71, moarTP will automatically switch to using unique player identifiers, which prevents this kind of abuse.  The plugin will appear the same to users and can continue to be used in the same way as before.


# Instructions for use:

The newest version of the plugin requires a MySQL database to operate.  If you have existing locations on your server, they will be automatically sent to the database upon configuration.  Download the plugin from http://dev.bukkit.org/server-mods/moarTP.  Place in your plugins folder for your Bukkit server, and reload the server.  Open moarTP_db.config, insert database connection details, and save the file.  Then, reload the server once more.  You should be ready to go!
