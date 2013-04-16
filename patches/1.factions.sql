-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_factions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_factions` ;

CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_factions` (
  `player_id` INT NOT NULL ,
  `faction` VARCHAR(255) NOT NULL DEFAULT 'none' ,
  `current_position` VARCHAR(255) NULL ,
  PRIMARY KEY (`player_id`) ,
  CONSTRAINT `fk_player_id22`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);