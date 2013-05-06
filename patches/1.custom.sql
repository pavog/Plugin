ALTER TABLE `$dbname`.`$prefix_misc_info_players` ADD `armor_rating` SMALLINT UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE `$dbname`.`$prefix_total_items` ADD `repaired` INT UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE `$dbname`.`$prefix_players` ADD `longest_session` BIGINT(11) NULL DEFAULT 0;