SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

SET storage_engine=InnoDB;

CREATE SCHEMA IF NOT EXISTS `$dbname` DEFAULT CHARACTER SET utf8 ;
USE `$dbname` ;

-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_settings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_settings` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_settings` (
  `key` VARCHAR(64) NOT NULL ,
  `value` TEXT NOT NULL ,
  PRIMARY KEY (`key`) );

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- Settings
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("version", "0");
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("ping", "120");
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("log_delay", "0");
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("show_welcome_messages", "1");
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("welcome_message", "Welcome, <PLAYER>!");
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("show_first_join_message", "1");
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("first_join_message", "Welcome, <PLAYER>! Your statistics on this server are now being tracked.");
INSERT INTO `$prefix_settings` (`key` , `value`) VALUES ("patched", "0");
