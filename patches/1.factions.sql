-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_factions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_factions` ;
CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_factions` (
  `faction_id` INT NOT NULL ,
  `player_id` INT NOT NULL ,
  `faction` VARCHAR(255) NOT NULL DEFAULT 'none' ,
  `current_position` VARCHAR(255) NULL ,
  PRIMARY KEY (`faction_id`) ,
  INDEX `fk_player_id22_idx` (`player_id` ASC) ,
  CONSTRAINT `fk_player_id22`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);