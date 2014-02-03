-- Fix normal horses 100
DELETE FROM `$prefix_etities` WHERE `etities_id` IS "100:0";
INSERT INTO `$prefix_etities` (`etities_id`, `tp_name`) VALUES ("100", "horse") ON DUPLICATE KEY UPDATE `tp_name` = "horse";
