# --- !Ups

ALTER TABLE `Service` ADD COLUMN `serviceNumber` INT DEFAULT 1;
ALTER TABLE `Bank` MODIFY `id` BIGINT(20) NOT NULL AUTO_INCREMENT;

CREATE TABLE `TaxTracking` (
                            `id` BIGINT AUTO_INCREMENT NOT NULL,
                            `period` VARCHAR(255) NOT NULL,
                            `taxAmount` FLOAT NOT NULL,
                            `paymentDate` DATETIME NOT NULL,
                            PRIMARY KEY (`id`)
);

CREATE TABLE `InvoiceTaxTracking`(
                                    `taxId` BIGINT NOT NULL,
                                    `invoiceId` BIGINT(20) NOT NULL,
                                    CONSTRAINT `fk_invoiceTaxTracking_taxId` FOREIGN KEY (`taxId`) REFERENCES TaxTracking(`id`),
                                    CONSTRAINT `fk_invoiceTaxTracking_invoiceId` FOREIGN KEY (`invoiceId`) REFERENCES Invoice(`id`)
);

# --- !Downs

ALTER TABLE `Service` DROP COLUMN `serviceNumber`;
DROP TABLE `TaxTracking`;
DROP TABLE `InvoiceTaxTracking`;