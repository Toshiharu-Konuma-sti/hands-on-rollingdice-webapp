CREATE DATABASE IF NOT EXISTS hands_on_db;
USE hands_on_db;

DROP TABLE IF EXISTS user;

CREATE TABLE dice(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  value INT NOT NULL,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARACTER SET=utf8mb4;

CREATE USER IF NOT EXISTS 'mywebapp'@'%' IDENTIFIED BY 'mypass';
GRANT select,insert,update,delete ON hands_on_db.* TO 'mywebapp'@'%';

GRANT SELECT ON performance_schema.* TO 'myuser'@'%';
GRANT PROCESS, REPLICATION CLIENT ON *.* TO 'myuser'@'%';
