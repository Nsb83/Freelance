# Invoice schema

# --- !Ups

CREATE TABLE `Invoice`(
                          `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                          `publicId` VARCHAR(255) NOT NULL,
                          `clientId` VARCHAR(255) NOT NULL,
                          `date` DATETIME NOT NULL,
                          `number` VARCHAR(255) NOT NULL,
                          `userId` VARCHAR(255) NOT NULL,

                          PRIMARY KEY (`id`),
                          CONSTRAINT FOREIGN KEY (`clientId`) REFERENCES `Client` (`id`),
                          CONSTRAINT FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);


# --- !Downs
DROP TABLE `Invoice`;
