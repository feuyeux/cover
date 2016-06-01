DROP TABLE IF EXISTS `tAdminUser`;
CREATE TABLE `tAdminUser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `loginname` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `rolename` varchar(255) DEFAULT NULL,
  `ruleallow` varchar(255) DEFAULT NULL,
  `ruledeny` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`loginname`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO tAdminUser(loginname,name,password,rolename,ruleallow,ruledeny) VALUES('a','a','a','ROLE_A,ROLE_B','ALL',NULL);
