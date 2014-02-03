-- Fix normal horses 100
DELETE FROM `$prefix_etities` WHERE `etities_id` IS "100:0";
INSERT INTO `$prefix_etities` (`etities_id`, `tp_name`) VALUES ("100", "horse") ON DUPLICATE KEY UPDATE `tp_name` = "horse";

-- Add missing grassless dirt
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("3:1", "grassless_dirt") ON DUPLICATE KEY UPDATE `tp_name` = "grassless_dirt";

--Add missing potions
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:8205", "water_breathing_3") ON DUPLICATE KEY UPDATE `tp_name` = "water_breathing_3";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:8269", "water_breathing_8") ON DUPLICATE KEY UPDATE `tp_name` = "water_breathing_8";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:16397", "water_breathing_splash_215") ON DUPLICATE KEY UPDATE `tp_name` = "water_breathing_splash_215";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:16461", "water_breathing_splash_6") ON DUPLICATE KEY UPDATE `tp_name` = "water_breathing_splash_6";
