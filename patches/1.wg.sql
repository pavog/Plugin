-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_wg_regions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_wg_regions` ;
CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_wg_regions` (
  `wg_region_id` INT NOT NULL AUTO_INCREMENT ,
  `player_id` INT NOT NULL ,
  `regions` TEXT NULL ,
  `flags` TEXT NULL ,
  INDEX `fk_player_id21_idx` (`player_id` ASC) ,
  CONSTRAINT `fk_player_id21`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);