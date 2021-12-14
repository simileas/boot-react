CREATE TABLE IF NOT EXISTS `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` char(64) DEFAULT NULL,
  `authority` char(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_auth_username` (`username`,`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `persistent_logins` (
  `username` char(64) NOT NULL,
  `series` char(64) NOT NULL,
  `token` char(64) NOT NULL,
  `last_used` datetime NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` char(64) NOT NULL,
  `password` char(128) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT IGNORE INTO users (username, password, enabled)
VALUES
('admin', 'c4c4ff004dde5bb78fa00480b5b88c68628cf7410bee1eadc1a9cf5277bc4f73e3a20784bb7e2a0a', 1);

INSERT IGNORE INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
