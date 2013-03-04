![YASP](http://stats.wolvencraft.com/src/img/plugin_logo.png)

**Y**et **A**nother **S**tatistics **P**lugin is a ground-up rewrite of a popular Minecraft plugin called **[Statistician](http://dev.bukkit.org/server-mods/statisticianv2/)**, first made by ChaseHQ and continued by Coryf88, Crimsonfoxy, and Dazzel_.
Starting up as an unofficial fork of the original project, YASP grew to the point where it contains almost no traces of the original code, both in Bukkit plugin and in web portal.
While Statistician does a decent job of logging some player and server statistics, YASP takes it a step further and completely overhauls the stats tracking, bringing the amount of information collected to a staggering amount, in addition to featuring a brand new modern web portal.
YASP was made to be highly customizable; it will do only what you tell it to do.

## Features ##

### Store various types of statistics ###

- Server statistics
    - Server startup and shutdown times, in log and graph forms
    - Current status (online / offline), current and total uptime
    - Players that are currently online
    - Maximum number of players and the time when that maximum was reached

- World statistics
    - PVP, PVE, and Natural deaths
    - Blocks placed and destroyed
    - Items picked up and dropped, used and crafted

- Player statistics
    - Player information (health, hunger, experience, gamemode)
    - Current status (online / offline), login and logout times
    - Player permissions group _(requires Vault)_
    - Player economy data: balance and transactions _(requires Vault)_
    - Detailed participation information
    - PVP, PVE, and Natural deaths
    - Blocks placed and destroyed
    - Items picked up and dropped, used and crafted
    - Distance traveled through various means

### Built-in Web Portal###
A fully functional portal is included with the plugin, featuring a sleek modern design and an incredible level of customization. The web portal will look the way you want it to look and do what you want it to do, nothing more, nothing less.

### Permissions Integration ###
The plugin will automatically integrate with any permissions plugin that you have installed. Add `stats.exempt` permission to people who you do not want to track.

## Requirements ##
In order to track statistics, you will need a web-server that fulfills the following requirements:

- MySQL v.5 or higher
- PHP 5.4.x

The Bukkit plugin was designed to work regardless of the Bukkit version, however, due to the sheer complexity of the code, it is only guaranteed to work with the version it was built with.

## Installation ##
Before you begin the installation process, you need to have full access to a MySQL database. It can be an existing database, or you can create a new one; the later is preferable, but not required.

1. Copy the contents of the `web` archive to the desired directory on your web server. For example, `/home/public_html/stats`.
2. Proceed to the URL associated with that directory and follow the instructions on screen. For example `http://wolvencraft.com/stats/`.
3. After you finish the web portal installation, the web page should be reporting the lack of connection to the server. This is normal, since the reporting plugin itself was not installed yet.
4. Copy the plugin `.jar` file into the Minecraft server's `/plugins/` folder and restart / reload the server. The plugin should generate a configuration file and report the lack of connection to the database.
5. Edit plugin's `config.yml` to match the details of your database. Reload the plugins if it is safe to do so, restart the server otherwise.
6. If the connection details in the configuration file are correct, the plugin should connect to the database and begin tracking data. Feel free to proceed to the web portal and edit what data you want to track; everything is enabled by default.
7. The plugin setup is complete.
