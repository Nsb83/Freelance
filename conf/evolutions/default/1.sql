# Client schema

# --- !Ups

CREATE TABLE `Client`(
                       `id` VARCHAR(255) NOT NULL PRIMARY KEY,
                       `companyName` VARCHAR(100),
                       `referentFirstName` VARCHAR(100),
                       `referentLastName` VARCHAR (100),
                       `adress` VARCHAR(255),
                       `postalCode` VARCHAR (50),
                       `city` VARCHAR (100),
                       `email` VARCHAR(100),
                       `phoneNumber` VARCHAR(100),
                       `VATNumber` VARCHAR(100),
                       `isActive` BOOLEAN
);


# --- !Downs
DROP TABLE `Client`;
