SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

SET storage_engine=InnoDB;

CREATE SCHEMA IF NOT EXISTS `$dbname` DEFAULT CHARACTER SET utf8 ;
USE `$dbname` ;

-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_modules`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_modules` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_modules` (
  `module_id` INT NOT NULL AUTO_INCREMENT ,
  `module_name` VARCHAR(16) NOT NULL ,
  `is_enabled` TINYINT(1) NOT NULL DEFAULT 0 ,
  `module_type` VARCHAR(16) NOT NULL ,
  `load_order` TINYINT(1) NOT NULL DEFAULT 0 ,
  `version` INT(11) NULL DEFAULT 0 ,
  PRIMARY KEY (`module_id`),
  UNIQUE KEY `module_name` (`module_name`) );
    
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;