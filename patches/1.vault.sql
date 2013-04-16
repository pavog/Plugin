-- -----------------------------------------------------
-- Table `$dbname`.`$prefix_vaults`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `$dbname`.`$prefix_vaults` ;
CREATE  TABLE IF NOT EXISTS `$dbname`.`$prefix_vaults` (
  `vault_id` INT NOT NULL AUTO_INCREMENT ,
  `player_id` INT NOT NULL ,
  `group` VARCHAR(255) NOT NULL DEFAULT 'none' ,
  `balance` FLOAT NOT NULL DEFAULT 0.0 ,
  PRIMARY KEY (`vault_id`) ,
  INDEX `fk_player_id20_idx` (`player_id` ASC) ,
  CONSTRAINT `fk_player_id20`
    FOREIGN KEY (`player_id` )
    REFERENCES `$dbname`.`$prefix_players` (`player_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);