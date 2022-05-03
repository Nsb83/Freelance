# --- !Ups

ALTER TABLE `Service` MODIFY `unitPrice` DECIMAL(10, 2) NOT NULL;
ALTER TABLE `Service` MODIFY `VATRate` DECIMAL(10, 1) NOT NULL;
ALTER TABLE `Service` MODIFY `quantity` DECIMAL(10, 1) NOT NULL;

# --- !Downs