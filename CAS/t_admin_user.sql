# Host: 192.168.66.142  (Version: 5.5.44-MariaDB)
# Date: 2016-05-25 16:37:38
# Generator: MySQL-Front 5.3  (Build 4.234)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "tAdminUser"
#

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

#
# Data for table "tAdminUser"
#

INSERT INTO `tAdminUser` VALUES (1,'a@a.a','a','a','a','ROLE_TEST','zhangsan,lisi','zhaowu'),(2,'b@b.b','b','b','b',NULL,NULL,NULL),(3,NULL,'joe','joe','joe',NULL,NULL,NULL);
