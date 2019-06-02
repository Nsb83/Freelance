# User schema

# --- !Ups

CREATE TABLE `User`(
                       `id` VARCHAR(40) NOT NULL,
                       `firstName` VARCHAR(255),
                       `lastName` VARCHAR(255),
                       `fullName` VARCHAR(255),
                       `email` VARCHAR(255),
                       `phoneNumber` VARCHAR(40),
                       `address` TEXT,
                       `postalCode` VARCHAR(40),
                       `city` VARCHAR(100),
                       `SIRENNumber` VARCHAR(100),
                       PRIMARY KEY (`id`)
);

CREATE TABLE `Logininfo`(
                            `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                            `providerID` VARCHAR(255) NOT NULL,
                            `providerKey` VARCHAR(255) NOT NULL,
                            PRIMARY KEY (`id`)
);

CREATE TABLE `UserLoginInfo` (
                                 `userID` VARCHAR(40) NOT NULL,
                                 `loginInfoId` BIGINT(20) NOT NULL
);

CREATE TABLE `PasswordInfo` (
                                `id` BIGINT(20)   NOT NULL AUTO_INCREMENT,
                                `hasher` VARCHAR(255) NOT NULL,
                                `password` VARCHAR(255) NOT NULL,
                                `salt` VARCHAR(255),
                                `loginInfoId` BIGINT(20) NOT NULL,
                                PRIMARY KEY (`id`),
                                CONSTRAINT FOREIGN KEY (`loginInfoId`) REFERENCES `LoginInfo` (`id`)
);



# --- !Downs
DROP TABLE `PasswordInfo`;
DROP TABLE `UserLoginInfo`;
DROP TABLE `LoginInfo`;
DROP TABLE `User`;
