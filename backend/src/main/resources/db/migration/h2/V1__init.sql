CREATE TABLE IF NOT EXISTS authorities (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  username char(64) NOT NULL,
  authority char(64) NOT NULL
);

CREATE TABLE persistent_logins (
  username char(64) NOT NULL,
  series char(64) NOT NULL PRIMARY KEY,
  token char(64) NOT NULL,
  last_used DATETIME NOT NULL
);

CREATE TABLE users (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  username char(64) NOT NULL UNIQUE,
  password char(128) DEFAULT NULL,
  enabled boolean DEFAULT FALSE
);

INSERT INTO users (username, password, enabled)
VALUES
('admin', 'c4c4ff004dde5bb78fa00480b5b88c68628cf7410bee1eadc1a9cf5277bc4f73e3a20784bb7e2a0a', 1);

INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
