-- Correct the names of some 1.8 blocks
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("1:5", "andesite") ON DUPLICATE KEY UPDATE `tp_name` = "andesite";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("1:6", "polished_andesite") ON DUPLICATE KEY UPDATE `tp_name` = "polished_andesite"

-- Add Wall Banners
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:0", "black_banner") ON DUPLICATE KEY UPDATE `tp_name` = "black_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:1", "red_banner") ON DUPLICATE KEY UPDATE `tp_name` = "red_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:2", "green_banner") ON DUPLICATE KEY UPDATE `tp_name` = "green_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:3", "brown_banner") ON DUPLICATE KEY UPDATE `tp_name` = "brown_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:4", "blue_banner") ON DUPLICATE KEY UPDATE `tp_name` = "blue_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:5", "purple_banner") ON DUPLICATE KEY UPDATE `tp_name` = "purple_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:6", "cyan_banner") ON DUPLICATE KEY UPDATE `tp_name` = "cyan_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:7", "light_gray_banner") ON DUPLICATE KEY UPDATE `tp_name` = "light_gray_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:8", "gray_banner") ON DUPLICATE KEY UPDATE `tp_name` = "gray_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:9", "pink_banner") ON DUPLICATE KEY UPDATE `tp_name` = "pink_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:10", "lime_banner") ON DUPLICATE KEY UPDATE `tp_name` = "lime_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:11", "yellow_banner") ON DUPLICATE KEY UPDATE `tp_name` = "yellow_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:12", "light_blue_banner") ON DUPLICATE KEY UPDATE `tp_name` = "light_blue_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:13", "magenta_banner") ON DUPLICATE KEY UPDATE `tp_name` = "magenta_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:14", "orange_banner") ON DUPLICATE KEY UPDATE `tp_name` = "orange_banner";
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("177:15", "white_banner") ON DUPLICATE KEY UPDATE `tp_name` = "white_banner";

-- Remove dublicate Entries
set session old_alter_table=1;
ALTER IGNORE TABLE `$prefix_total_blocks` ADD UNIQUE INDEX `$prefix_un_blocks_id1_idx` (`material_id`, `player_id`);
ALTER IGNORE TABLE `$prefix_total_items` ADD UNIQUE INDEX `$prefix_un_items_id1_idx` (`material_id`, `player_id`);
ALTER IGNORE TABLE `$prefix_total_deaths` ADD UNIQUE INDEX `$prefix_un_deaths_id1_idx` (`player_id`, `cause`);
ALTER IGNORE TABLE `$prefix_total_pve_kills` ADD UNIQUE INDEX `$prefix_un_pve_kills_id1_idx` (`material_id`, `player_id`, `entity_id`);
ALTER IGNORE TABLE `$prefix_total_pvp_kills` ADD UNIQUE INDEX `$prefix_un_pvp_kills_id1_idx` (`material_id`, `player_id`, `victim_id`);
set session old_alter_table=0;