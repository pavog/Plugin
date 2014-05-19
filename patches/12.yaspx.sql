-- -----------------------------------------------------
-- Create new server_statistics table
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_server_statistics` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_server_statistics` (
  `server_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `server_name` TEXT NOT NULL,
  `server_ip` TEXT NOT NULL,
  `server_motd` TEXT NOT NULL ,
  `server_port` INT UNSIGNED NOT NULL,
  `bukkit_version` TEXT NOT NULL,
  `current_uptime` INT(11)UNSIGNED NOT NULL,
  `first_startup` INT(11) UNSIGNED NOT NULL,
  `free_memory` INT(11) UNSIGNED NOT NULL,
  `java.vendor` TEXT NOT NULL,
  `java.vendor.url` TEXT NOT NULL,
  `java.version` TEXT NOT NULL,
  `java.vm.name` TEXT NOT NULL,
  `java.vm.vendor` TEXT NOT NULL,
  `java.vm.version` TEXT NOT NULL,
  `last_shutdown` INT(11) UNSIGNED NOT NULL,
  `last_startup` INT(11) UNSIGNED NOT NULL,
  `max_players_online` INT UNSIGNED NOT NULL,
  `max_players_online_time` INT(11) UNSIGNED NOT NULL,
  `os.arch` TEXT NOT NULL,
  `os.name` TEXT NOT NULL,
  `os.version` TEXT NOT NULL,
  `players_allowed` INT UNSIGNED NOT NULL,
  `plugins` INT UNSIGNED NOT NULL,
  `server_time` INT(11) UNSIGNED NOT NULL,
  `ticks_per_second` INT UNSIGNED NOT NULL,
  `total_memory` INT(11) UNSIGNED NOT NULL,
  `total_uptime` INT(11) UNSIGNED NOT NULL,
  `weather` TEXT NOT NULL,
  `weather_duration` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`player_id`);
