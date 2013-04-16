-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_wg_regions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_wg_regions` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_wg_regions` (
  `player_id` INT NOT NULL ,
  `regions` TEXT NULL ,
  `flags` TEXT NULL ,
  PRIMARY KEY (`player_id`) ,
  CONSTRAINT `fk_player_id21`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;