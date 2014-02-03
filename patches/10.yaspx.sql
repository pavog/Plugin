-- Fix normal horses 100
INSERT INTO `$prefix_etities` (`etities_id`, `tp_name`) VALUES ("100", "horse") ON DUPLICATE KEY UPDATE `tp_name` = "horse";
