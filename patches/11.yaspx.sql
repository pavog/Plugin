SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- prepare Players table to store UUID's

ALTER TABLE `$prefix_players` ADD uuid VARCHAR(50) AFTER `player_id`; 
DROP INDEX `name` ON `$prefix_players`;
CREATE UNIQUE INDEX `uuid` ON `$prefix_players` (`uuid`);

-- prepare misc_info_players tabe to store total XP

ALTER TABLE `$prfix_misc_info_players` MODIFY `exp_total` bigint(20);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;