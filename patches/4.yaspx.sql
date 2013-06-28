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
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("merged_data_tracking", "0");

-- Alter existing tables

ALTER TABLE `$prefix_player_inventories` ADD `selected_item` SMALLINT UNSIGNED NOT NULL DEFAULT 0;

-- Create new tables

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

SET storage_engine=InnoDB;

CREATE SCHEMA IF NOT EXISTS `$dbname` DEFAULT CHARACTER SET utf8 ;
USE `$dbname` ;

-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_player_locations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_player_locations` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_player_locations` (
  `player_id` INT NOT NULL ,
  `world` VARCHAR(255) NULL DEFAULT NULL ,
  `x` INT NULL DEFAULT NULL ,
  `y` INT NULL DEFAULT NULL ,
  `z` INT NULL DEFAULT NULL ,
  `biome` VARCHAR(32) NULL DEFAULT NULL ,
  `humidity` FLOAT NOT NULL DEFAULT 0.0 ,
  PRIMARY KEY (`player_id`) ,
  CONSTRAINT `fk_player_id20`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
    
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- Materials for 1.6
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("43:8", "smooth_stone_slab_double");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("43:9", "smooth_sandstone_slab_double");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("145:1", "anvil_slightly_damaged");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("145:2", "anvil_very_damaged");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:0", "stained_clay_white");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:1", "stained_clay_orange");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:2", "stained_clay_magenta");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:3", "stained_clay_light_blue");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:4", "stained_clay_yellow");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:5", "stained_clay_lime");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:6", "stained_clay_pink");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:7", "stained_clay_gray");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:8", "stained_clay_light_gray");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:9", "stained_clay_cyan");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:10", "stained_clay_purple");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:11", "stained_clay_blue");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:12", "stained_clay_brown");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:13", "stained_clay_green");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:14", "stained_clay_red");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("159:15", "stained_clay_black");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("170:0", "hay_bale");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:0", "carpet_white");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:1", "carpet_orange");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:2", "carpet_magenta");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:3", "carpet_light_blue");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:4", "carpet_yellow");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:5", "carpet_lime");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:6", "carpet_pink");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:7", "carpet_grey");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:8", "carpet_light_gray");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:9", "carpet_cyan");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:10", "carpet_purple");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:11", "carpet_blue");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:12", "carpet_brown");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:13", "carpet_green");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:14", "carpet_red");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("171:15", "carpet_black");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("172:0", "hardened_clay");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("173:0", "block_of_coal");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:8289", "regeneration_potion_ii_1");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:8290", "swiftness_potion_ii_4");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:8292", "poison_potion_ii_1");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:8297", "strength_potion_ii_4");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:16481", "regeneration_splash_ii_45");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:16482", "swiftness_splash_ii_3");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:16484", "poison_splash_ii_45");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("373:16489", "strength_splash_ii_3");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("383:100", "spawn_egg_horse");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("417:0", "iron_horse_armor");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("418:0", "gold_horse_armor");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("419:0", "diamond_horse_armor");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("420:0", "lead");
INSERT INTO `$prefix_materials` (`material_id`, `tp_name`) VALUES ("421:0", "name_tag");
