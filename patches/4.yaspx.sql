-- New values for the server statistics

INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("os.name", "");
INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("os.version", "");
INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("os.arch", "");

INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("java.version", "");
INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("java.vendor", "");
INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("java.vendor.url", "");

INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("java.vm.version", "");
INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("java.vm.vendor", "");
INSERT INTO `$prefix_server_statistics` (`key` , `value`) VALUES ("java.vm.name", "");

-- Option to stop tracking player stats when vanish is active

INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("hook.vanish.no_tracking", "0");