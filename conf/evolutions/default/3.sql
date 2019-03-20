# Invoice schema

# --- !Ups

CREATE TABLE `Service`(
                        `id` VARCHAR(255) NOT NULL,
                        `invoiceId` VARCHAR(255) NOT NULL,
                        `serviceName` TEXT NOT NULL,
                        `quantity` DECIMAL NOT NULL,
                        `unitPrice` DECIMAL NOT NULL,
                        `VATRate` DECIMAL NOT NULL,
                        `totalDutyFreePrice` DECIMAL NOT NULL,
                        `VATTotal` DECIMAL NOT NULL,
                        `totalPrice` DECIMAL NOT NULL,
                        PRIMARY KEY (`id`),
                        CONSTRAINT FOREIGN KEY (`invoiceId`) REFERENCES `Invoice` (`id`)
);


# --- !Downs
DROP TABLE `Service`;
