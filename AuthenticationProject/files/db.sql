CREATE DATABASE db;
USE db;
CREATE TABLE User(
    ID int(5) unsigned auto_increment Primary key,
    username varchar(15) not null,
    hashedPassword varchar(50) not null,
    salt varchar(50) not null
);
INSERT INTO User(username, hashedPassword, salt) values ('alice23','r9zwUzv7zAA5LSbrv/NIaMqPHcNfay8NSoVWYJ5VE1I=' ,'MMq/BHKa5Lva/OwRduHi7g==');