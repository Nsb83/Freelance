# --- !Ups

ALTER TABLE `Service` ADD COLUMN `serviceNumber` INT DEFAULT 1;
ALTER TABLE `Bank` MODIFY `id` BIGINT(20) NOT NULL AUTO_INCREMENT;

# --- !Downs

ALTER TABLE `Service` DROP COLUMN `serviceNumber`;