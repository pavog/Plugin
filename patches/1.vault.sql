-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_vaults`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_vaults` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_vaults` (
  `player_id` INT NOT NULL ,
  `group` TEXT NOT NULL DEFAULT '' ,
  `balance` FLOAT NOT NULL DEFAULT 0.0 ,
  PRIMARY KEY (`player_id`) ,
  CONSTRAINT `fk_player_id20`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
