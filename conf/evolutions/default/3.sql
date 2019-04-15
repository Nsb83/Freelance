# Invoice schema

# --- !Ups

CREATE TABLE `Service`(
                        `id` VARCHAR(255) NOT NULL,
                        `invoiceId` BIGINT(20) NOT NULL,
                        `serviceName` TEXT NOT NULL,
                        `quantity` DECIMAL NOT NULL,
                        `unitPrice` DECIMAL NOT NULL,
                        `VATRate` DECIMAL NOT NULL,
                        `totalDutyFreePrice` DECIMAL,
                        `VATTotal` DECIMAL,
                        `totalPrice` DECIMAL,
                        PRIMARY KEY (`id`),
                        CONSTRAINT FOREIGN KEY (`invoiceId`) REFERENCES `Invoice` (`id`)
);


# --- !Downs
DROP TABLE `Service`;
