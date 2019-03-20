# Invoice schema

# --- !Ups

CREATE TABLE `Invoice`(
                       `id` VARCHAR(255) NOT NULL,
                       `clientId` VARCHAR(255) NOT NULL,
                       `date` DATETIME NOT NULL,
                       `number` VARCHAR(255) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT FOREIGN KEY (`clientId`) REFERENCES `Client` (`id`)
);


# --- !Downs
DROP TABLE `Invoice`;
