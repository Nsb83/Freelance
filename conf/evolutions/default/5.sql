# Bank schema

# --- !Ups

CREATE TABLE `Bank`(
                          `id` BIGINT(20) NOT NULL,
                          `bankName` VARCHAR(100) NOT NULL,
                          `BICNumber` VARCHAR(40) NOT NULL,
                          `IBANNumber` VARCHAR(40) NOT NULL,
                          `userId` VARCHAR(40) NOT NULL,
                          PRIMARY KEY (`id`),
                          CONSTRAINT FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);


# --- !Downs
DROP TABLE `Bank`;
